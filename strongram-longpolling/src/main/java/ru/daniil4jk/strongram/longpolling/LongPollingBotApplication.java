package ru.daniil4jk.strongram.longpolling;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.interfaces.BackOff;
import org.telegram.telegrambots.longpolling.util.ExponentialBackOff;
import org.telegram.telegrambots.longpolling.util.TelegramOkHttpClientFactory;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.daniil4jk.strongram.longpolling.adapter.HasLongPollingBot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

public class LongPollingBotApplication extends TelegramBotsLongPollingApplication {
    public LongPollingBotApplication() {
        super(ObjectMapper::new);
    }

    public LongPollingBotApplication(Supplier<ObjectMapper> objectMapperSupplier) {
        super(objectMapperSupplier, new TelegramOkHttpClientFactory.DefaultOkHttpClientCreator());
    }

    public LongPollingBotApplication(Supplier<ObjectMapper> objectMapperSupplier, Supplier<OkHttpClient> okHttpClientCreator) {
        super(objectMapperSupplier, okHttpClientCreator, Executors::newSingleThreadScheduledExecutor);
    }

    public LongPollingBotApplication(Supplier<ObjectMapper> objectMapperSupplier,
                                     Supplier<OkHttpClient> okHttpClientCreator,
                                     Supplier<ScheduledExecutorService> executorSupplier) {
        super(objectMapperSupplier, okHttpClientCreator, executorSupplier, ExponentialBackOff::new);
    }

    public LongPollingBotApplication(Supplier<ObjectMapper> objectMapperSupplier,
                                     Supplier<OkHttpClient> okHttpClientCreator,
                                     Supplier<ScheduledExecutorService> executorSupplier,
                                     Supplier<BackOff> backOffSupplier) {
        super(objectMapperSupplier, okHttpClientCreator, executorSupplier, backOffSupplier);
    }

    public BotSession registerBot(@NotNull HasLongPollingBot adapter) throws TelegramApiException {
        String token = adapter.getToken();
        BotSession session = super.registerBot(token, adapter);
        if (!adapter.hasClient()) {
            adapter.setClient(new OkHttpTelegramClient(session.getOkHttpClient(), token));
        }
        return session;
    }
}
