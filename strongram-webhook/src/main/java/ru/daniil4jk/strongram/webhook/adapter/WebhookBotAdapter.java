package ru.daniil4jk.strongram.webhook.adapter;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.webhook.TelegramWebhookBot;
import ru.daniil4jk.strongram.core.bot.Bot;
import ru.daniil4jk.strongram.core.response.client.provider.MutableTelegramClientProvider;
import ru.daniil4jk.strongram.core.response.client.provider.TelegramClientProvider;
import ru.daniil4jk.strongram.core.response.dto.Response;
import ru.daniil4jk.strongram.core.util.DefaultExecutor;
import ru.daniil4jk.strongram.webhook.AddressUtils;
import ru.daniil4jk.strongram.webhook.WebhookSender;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
public class WebhookBotAdapter implements TelegramWebhookBot, TelegramClientProvider {
    private final TelegramClientProvider provider = new MutableTelegramClientProvider(
            this::createClient
    );
    private final String path;
    private final String token;
    private final SetWebhook setWebhookMethod;
    private final Bot bot;
    private final WebhookSender sender;

    public WebhookBotAdapter(@NotNull URL fullUrl,
                             @NotNull String token,
                             @NotNull Bot bot) {
        this(
                fullUrl,
                token,
                bot,
                DefaultExecutor.initOrGet(WebhookBotAdapter.class.getName())
        );
    }

    public WebhookBotAdapter(@NotNull URL fullUrl,
                             @NotNull String token,
                             @NotNull Bot bot,
                             @NotNull ScheduledExecutorService sendExecutor) {
        this(
                new SetWebhook(
                        AddressUtils.constructSetWebhookAddress(fullUrl)
                ),
                fullUrl.getPath(),
                token,
                bot,
                sendExecutor
        );
    }

    public WebhookBotAdapter(@NotNull SetWebhook setWebhookMethod,
                             @Nullable String path,
                             @NotNull String token,
                             @NotNull Bot bot) {
        this(
                setWebhookMethod,
                path,
                token,
                bot,
                DefaultExecutor.initOrGet(WebhookBotAdapter.class.getName())
        );
    }

    public WebhookBotAdapter(@NotNull SetWebhook setWebhookMethod,
                             @Nullable String path,
                             @NotNull String token,
                             @NotNull Bot bot,
                             @NotNull ScheduledExecutorService sendExecutor) {
        this.setWebhookMethod = setWebhookMethod;
        this.token = token;
        this.path = Optional.ofNullable(path).orElse("/");
        this.bot = bot;
        this.sender = new WebhookSender(() -> sendExecutor, provider);
        setBotCallback();
    }

    private void setBotCallback() {
        bot.setDefaultCallback(sender::sendUsingClient);
    }

    @Override
    public void runSetWebhook() {
        try {
            getClient().execute(setWebhookMethod);
        } catch (TelegramApiException e) {
            log.error("Can`t setWebhookMethod to bot on the path {}", path, e);
        }
    }

    @Override
    public void runDeleteWebhook() {
        try {
            getClient().execute(new DeleteWebhook());
        } catch (TelegramApiException e) {
            log.warn("Can`t deleteWebhook to bot on the path {}", path, e);
        }
    }

    @Override
    public BotApiMethod<?> consumeUpdate(Update update) {
        try {
            List<Response<?>> collector = new ArrayList<>();
            bot.accept(update, collector::add);
            return sender.sendAllWebhook(collector);
        } catch (Exception e) {
            log.error("Error occurred while webhook bot processing update", e);
            return null;
        }
    }

    @Override
    public String getBotPath() {
        return path;
    }

    @Override
    public TelegramClient getClient() {
        return provider.getClient();
    }

    @Override
    public void setClient(TelegramClient client) {
        provider.setClient(client);
    }

    @Override
    public boolean hasClient() {
        return provider.hasClient();
    }

    @Contract(" -> new")
    private @NotNull TelegramClient createClient() {
        return new OkHttpTelegramClient(token);
    }
}
