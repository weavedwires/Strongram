package ru.daniil4jk.strongram.core.response.dto;

import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * Wraps a Telegram API method together with its execution logic. Carries
 * metadata about whether a return value is required and the target chat ID.
 */
public interface Response<Method extends PartialBotApiMethod<?>> {
    Method getEntry();

    void sendUsing(TelegramClient client);

    boolean isObjectRequired();

    String getChatId();

    //todo add thenSend
}
