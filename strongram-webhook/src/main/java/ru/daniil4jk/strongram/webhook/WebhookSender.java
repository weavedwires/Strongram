package ru.daniil4jk.strongram.webhook;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import ru.daniil4jk.strongram.core.response.client.provider.TelegramClientProvider;
import ru.daniil4jk.strongram.core.response.dto.Response;
import ru.daniil4jk.strongram.core.response.sender.Sender;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

/**
 * A {@link ru.daniil4jk.strongram.core.response.sender.Sender} variant for webhook bots.
 * If exactly one {@link org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod}
 * response is produced, it is returned directly for the webhook reply; otherwise responses
 * are sent via the Telegram client.
 */
public class WebhookSender extends Sender {
    public WebhookSender(Supplier<ExecutorService> executor, TelegramClientProvider client) {
        super(executor, client);
    }

    public BotApiMethod<?> sendAllWebhook(List<Response<?>> messages) {
        if (responseCanBeReturned(messages)) {
            return (BotApiMethod<?>) messages.get(0).getEntry();
        }
        sendAllUsingClient(messages);
        return null;
    }

    private static boolean responseCanBeReturned(@NotNull List<Response<?>> list) {
        if (list.size() != 1) return false;
        var response = list.get(0);
        return response.getEntry() instanceof BotApiMethod<?> &&
                !response.isObjectRequired();
    }
}

