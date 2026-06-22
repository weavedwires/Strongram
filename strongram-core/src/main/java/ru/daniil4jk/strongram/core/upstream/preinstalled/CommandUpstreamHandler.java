package ru.daniil4jk.strongram.core.upstream.preinstalled;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.daniil4jk.strongram.core.command.EachCommandHandler;
import ru.daniil4jk.strongram.core.context.request.RequestContext;
import ru.daniil4jk.strongram.core.filter.Filter;
import ru.daniil4jk.strongram.core.filter.Filters;
import ru.daniil4jk.strongram.core.unboxer.As;
import ru.daniil4jk.strongram.core.upstream.FilteredUpstreamHandler;
import ru.daniil4jk.strongram.core.util.Lazy;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Base for handlers that route slash commands (e.g. /start, /help@bot) to
 * {@link EachCommandHandler} callbacks. Supports bot-username disambiguation
 * in group chats via the /command@bot pattern.
 */
@NoArgsConstructor
public abstract class CommandUpstreamHandler extends FilteredUpstreamHandler {
    private static final String EMPTY = "";
    private static final String WHITESPACE = " ";
    private static final String DOG = "@";
    private static final String SLASH = "/";

    private final Lazy<Map<String, EachCommandHandler>> commands = new Lazy<>(this::formatCommands);

    private Map<String, EachCommandHandler> formatCommands() {
        return getCommands().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> formatCommand(entry.getKey()),
                        Map.Entry::getValue
                ));
    }

    protected abstract Map<String, EachCommandHandler> getCommands();

    @Override
    protected final @NotNull Filter getFilter() {
        return Filters.isCommand();
    }

    @Override
    protected final void processFiltered(@NotNull RequestContext ctx) {
        var username = formatUsername(ctx.getBotUsername());
        var msg = ctx.getRequest(As.message());
        var text = msg.getText();

        try {
            processCommand(ctx, text);
            return;
        } catch (CommandNotFoundException e) { /* продолжаем выполнение */ }

        try {
            processGroupCommand(ctx, text, username);
            return;
        } catch (CommandNotFoundException ee) { /* продолжаем выполнение */ }

        processNext(ctx);
    }

    private void processGroupCommand(RequestContext ctx, @NotNull String text, String username) {
        String groupText = text.replace(username, EMPTY);
        processCommand(ctx, groupText);
    }

    private void processCommand(@NotNull RequestContext ctx, @NotNull String text) {
        String[] cmdAndArgs = text.split(WHITESPACE, 2);
        String command = cmdAndArgs[0];
        String[] args = cmdAndArgs.length > 1 ? cmdAndArgs[1].split(WHITESPACE) : ArrayUtils.EMPTY_STRING_ARRAY;
        parseCommand(command).accept(ctx, args);
    }

    private @NotNull EachCommandHandler parseCommand(String rawCommand) {
        String command = formatCommand(rawCommand);
        return Optional.ofNullable(commands.initOrGet().get(command))
                .orElseThrow(() -> new CommandNotFoundException(command));
    }

    @Contract(pure = true)
    private static @NotNull String formatUsername(@NotNull String rawUsername) {
        rawUsername = rawUsername.trim().toLowerCase();
        if (rawUsername.startsWith(DOG)) return rawUsername;
        return DOG + rawUsername;
    }

    @Contract(pure = true)
    private static @NotNull String formatCommand(@NotNull String rawCommand) {
        rawCommand = rawCommand.trim();
        if (rawCommand.contains(WHITESPACE)) {
            throw new IllegalArgumentException("Command should not contain whitespace");
        }
        rawCommand = rawCommand.toLowerCase();
        if (rawCommand.startsWith(SLASH)) return rawCommand;
        return SLASH + rawCommand;
    }

    private static class CommandNotFoundException extends RuntimeException {
        public CommandNotFoundException(String command) {
            super("Команда %s не найдена".formatted(command));
        }
    }
}
