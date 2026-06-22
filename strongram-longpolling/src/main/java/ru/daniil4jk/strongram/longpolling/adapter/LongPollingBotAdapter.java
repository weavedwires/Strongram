package ru.daniil4jk.strongram.longpolling.adapter;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.daniil4jk.strongram.core.bot.Bot;
import ru.daniil4jk.strongram.core.response.client.provider.MutableTelegramClientProvider;
import ru.daniil4jk.strongram.core.response.client.provider.TelegramClientProvider;
import ru.daniil4jk.strongram.core.response.dto.Response;
import ru.daniil4jk.strongram.core.response.sender.Sender;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Adapts a {@link ru.daniil4jk.strongram.core.bot.Bot} to the Telegram long-polling model.
 * Consumes updates on a single-thread executor and sends responses via a
 * {@link ru.daniil4jk.strongram.core.response.sender.Sender}.
 */
@Slf4j
public class LongPollingBotAdapter implements HasLongPollingBot {
    private final TelegramClientProvider clientProvider = new MutableTelegramClientProvider();

    @Getter
    private final String token;
    private final Bot bot;
    private final Sender sender;
    private final ExecutorService sendExecutor;

    public LongPollingBotAdapter(String token, Bot bot) {
        this(token, bot, Executors.newSingleThreadScheduledExecutor());
    }

    public LongPollingBotAdapter(
        String token,
        Bot bot,
        ExecutorService sendExecutor
    ) {
        this.token = token;
        this.bot = bot;
        this.sender = new Sender(() -> sendExecutor, clientProvider);
        this.sendExecutor = sendExecutor;

        setBotCallback();
    }

    private void setBotCallback() {
        bot.setDefaultCallback(sender::sendUsingClient);
    }

    @Override
    public void consume(List<Update> updates) {
        updates.forEach(this::pushConsumeSingleToExecutor);
    }

    private void pushConsumeSingleToExecutor(Update update) {
        sendExecutor.execute(() -> consumeSingle(update));
    }

    public void consumeSingle(Update update) {
        try {
            List<Response<?>> collector = new ArrayList<>();
            bot.accept(update, collector::add);
            sender.sendAllUsingClient(collector);
        } catch (Exception e) {
            log.error(
                    "Error occurred while longpolling bot processing update",
                    e
            );
        }
    }

    @Override
    public TelegramClient getClient() {
        return clientProvider.getClient();
    }

    @Override
    public void setClient(TelegramClient client) {
        clientProvider.setClient(client);
    }

    @Override
    public boolean hasClient() {
        return clientProvider.hasClient();
    }
}
