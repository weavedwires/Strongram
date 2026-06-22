package ru.daniil4jk.strongram.core.response.dto;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * A functional interface for executing a Telegram API call and returning a
 * typed result, allowing checked {@link org.telegram.telegrambots.meta.exceptions.TelegramApiException}.
 */
public interface SendFunction<Result> {
    Result apply(TelegramClient client) throws TelegramApiException;
}