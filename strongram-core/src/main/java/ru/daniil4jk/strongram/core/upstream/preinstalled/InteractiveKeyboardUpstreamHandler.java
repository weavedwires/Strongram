package ru.daniil4jk.strongram.core.upstream.preinstalled;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.daniil4jk.strongram.core.context.request.RequestContext;
import ru.daniil4jk.strongram.core.filter.Filter;
import ru.daniil4jk.strongram.core.keyboard.InteractiveKeyboardHolder;
import ru.daniil4jk.strongram.core.keyboard.button.InteractiveButton;
import ru.daniil4jk.strongram.core.unboxer.As;
import ru.daniil4jk.strongram.core.unboxer.Unboxer;
import ru.daniil4jk.strongram.core.upstream.FilteredUpstreamHandler;
import ru.daniil4jk.strongram.core.util.Lazy;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Routes button presses from an {@link InteractiveKeyboardHolder} to the corresponding
 * {@link ru.daniil4jk.strongram.core.keyboard.button.InteractiveButton} actions.
 * Supports both {@link org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup}
 * and {@link org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup}.
 */
public class InteractiveKeyboardUpstreamHandler extends FilteredUpstreamHandler {
    private final Lazy<Map<String, InteractiveButton>> buttons = new Lazy<>(this::parseKeyboard);
    private final InteractiveKeyboardHolder holder;

    public InteractiveKeyboardUpstreamHandler(ReplyKeyboardMarkup keyboard) {
        this.holder = new InteractiveKeyboardHolder(keyboard);
    }

    public InteractiveKeyboardUpstreamHandler(InlineKeyboardMarkup keyboard) {
        this.holder = new InteractiveKeyboardHolder(keyboard);
    }

    @Override
    protected @NotNull Filter getFilter() {
        return ctx -> buttons.initOrGet()
                .containsKey(ctx.getRequest(asCompatibleText()));
    }

    @Override
    protected void processFiltered(@NotNull RequestContext ctx) {
        buttons.initOrGet()
                .get(ctx.getRequest(asCompatibleText()))
                .accept(ctx);
    }

    private Unboxer<String> asCompatibleText() {
        return switch (holder.getType()) {
            case Reply -> As.messageText();
            case Inline -> As.callbackQueryData();
        };
    }

    protected Map<String, InteractiveButton> parseKeyboard() {
        ReplyKeyboard keyboard = holder.getKeyboard();

        return switch (holder.getType()) {
            case Reply -> collectButtons(((ReplyKeyboardMarkup) keyboard).getKeyboard());
            case Inline -> collectButtons(((InlineKeyboardMarkup) keyboard).getKeyboard());
        };
    }

    private static Map<String, InteractiveButton> collectButtons(List<? extends List<?>> keyboard) {
        Map<String, InteractiveButton> map = keyboard.stream()
                .flatMap(Collection::stream)
                .filter(InteractiveButton::isInstance)
                .map(InteractiveButton::cast)
                .collect(mapCollector);

        if (map.isEmpty()) {
            throw new IllegalArgumentException("keyboard has`nt interactive buttons");
        }
        return map;
    }

    private static final Collector<InteractiveButton, ?, Map<String, InteractiveButton>> mapCollector =
            Collectors.toMap(InteractiveButton::getCallbackData, button -> button);
}
