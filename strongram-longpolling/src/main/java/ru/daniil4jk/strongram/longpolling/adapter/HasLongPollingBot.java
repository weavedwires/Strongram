package ru.daniil4jk.strongram.longpolling.adapter;

import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import ru.daniil4jk.strongram.core.response.client.provider.TelegramClientProvider;
import ru.daniil4jk.strongram.longpolling.adapter.provider.TokenProvider;

/**
 * Combines the contracts of a long-polling update consumer, a Telegram client provider,
 * and a token provider. Implementations can be registered with {@link LongPollingBotApplication}.
 */
public interface HasLongPollingBot extends
        LongPollingUpdateConsumer,
        TelegramClientProvider,
        TokenProvider {
}
