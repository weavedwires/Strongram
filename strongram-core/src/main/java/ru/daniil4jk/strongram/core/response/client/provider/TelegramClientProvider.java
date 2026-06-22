package ru.daniil4jk.strongram.core.response.client.provider;

import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * Manages a {@link org.telegram.telegrambots.meta.generics.TelegramClient}
 * instance, providing access, existence check, and mutability control.
 */
public interface TelegramClientProvider {
    TelegramClient getClient();

    boolean hasClient();

    void setClient(TelegramClient client);
}
