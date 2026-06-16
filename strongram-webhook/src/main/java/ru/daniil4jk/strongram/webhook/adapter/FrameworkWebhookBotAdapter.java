package ru.daniil4jk.strongram.webhook.adapter;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.daniil4jk.strongram.core.bot.Bot;
import ru.daniil4jk.strongram.core.response.client.provider.ImmutableTelegramClientProvider;
import ru.daniil4jk.strongram.core.response.client.provider.TelegramClientProvider;
import ru.daniil4jk.strongram.core.response.dto.Response;
import ru.daniil4jk.strongram.webhook.AddressUtils;
import ru.daniil4jk.strongram.webhook.WebhookSender;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public class FrameworkWebhookBotAdapter {
    private final TelegramClientProvider provider = new ImmutableTelegramClientProvider(null);
    private final SetWebhook setWebhookMethod;
    private final Bot bot;
    private final WebhookSender sender;

    public FrameworkWebhookBotAdapter(@NotNull String token,
                                      @NotNull String yourServerAddress,
                                      @NotNull Bot bot,
                                      @NotNull ScheduledExecutorService sendExecutor) {
        this(
                createClient(token),
                createSetWebhook(yourServerAddress),
                bot,
                sendExecutor
        );
    }

    public FrameworkWebhookBotAdapter(@NotNull TelegramClient client,
                                      @NotNull String yourServerAddress,
                                      @NotNull Bot bot,
                                      @NotNull ScheduledExecutorService sendExecutor) {
        this(
                client,
                createSetWebhook(yourServerAddress),
                bot,
                sendExecutor
        );
    }

    public FrameworkWebhookBotAdapter(@NotNull String token,
                                      @NotNull SetWebhook setWebhookMethod,
                                      @NotNull Bot bot,
                                      @NotNull ScheduledExecutorService sendExecutor) {
        this(
                createClient(token),
                setWebhookMethod,
                bot,
                sendExecutor
        );
    }

    public FrameworkWebhookBotAdapter(@NotNull TelegramClient client,
                                      @NotNull SetWebhook setWebhookMethod,
                                      @NotNull Bot bot,
                                      @NotNull ScheduledExecutorService sendExecutor) {
        this.provider.setClient(client);
        this.setWebhookMethod = setWebhookMethod;
        this.bot = bot;
        this.sender = new WebhookSender(() -> sendExecutor, provider);
        setBotCallback();
    }

    private void setBotCallback() {
        bot.setDefaultCallback(sender::sendUsingClient);
    }

    public void register() throws TelegramApiException {
        provider.getClient().execute(setWebhookMethod);
    }

    public BotApiMethod<?> consumeUpdate(Update update) {
        List<Response<?>> collector = new ArrayList<>();
        bot.accept(update, collector::add);
        return sender.sendAllWebhook(collector);
    }

    public void unregister() throws TelegramApiException {
        provider.getClient().execute(new DeleteWebhook());
    }

    private static @NotNull TelegramClient createClient(String token) {
        return new OkHttpTelegramClient(token);
    }

    private static @NotNull SetWebhook createSetWebhook(String address) {
        return new SetWebhook(AddressUtils.constructSetWebhookAddress(address));
    }
}