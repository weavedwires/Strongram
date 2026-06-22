package ru.daniil4jk.strongram.webhook;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.webhook.TelegramBotsWebhookApplication;
import org.telegram.telegrambots.webhook.WebhookOptions;
import ru.daniil4jk.strongram.webhook.adapter.WebhookBotAdapter;

/**
 * Entry point for a webhook-based bot. Extends the TelegramBots
 * {@link org.telegram.telegrambots.webhook.TelegramBotsWebhookApplication}
 * and provides convenience constructors.
 */
public class WebhookBotApplication extends TelegramBotsWebhookApplication {
    public WebhookBotApplication() throws TelegramApiException {
    }

    public WebhookBotApplication(WebhookOptions webhookOptions) throws TelegramApiException {
        super(webhookOptions);
    }

    public void registerBot(@NotNull WebhookBotAdapter bot) throws TelegramApiException {
        super.registerBot(bot);
    }
}
