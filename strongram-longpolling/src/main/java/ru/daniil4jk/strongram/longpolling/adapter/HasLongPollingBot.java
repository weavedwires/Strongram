package ru.daniil4jk.strongram.longpolling.adapter;

import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import ru.daniil4jk.strongram.core.response.client.provider.TelegramClientProvider;
import ru.daniil4jk.strongram.longpolling.adapter.provider.TokenProvider;

public interface HasLongPollingBot extends
        LongPollingUpdateConsumer,
        TelegramClientProvider,
        TokenProvider {
}
