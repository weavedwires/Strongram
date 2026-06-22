package ru.daniil4jk.strongram.core.context.request;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;

/**
 * Uniquely identifies a Telegram conversation participant by associating a
 * {@link Chat} and/or {@link User}. Provides a safe {@link #getReplyChatId}
 * method that resolves the appropriate chat ID from whichever entity is present.
 */
public record TelegramUUID(Chat chat, User user) {

    public TelegramUUID(Chat chat) {
        this(
                chat, null
        );
    }

    public TelegramUUID(User user) {
        this(
                null, user
        );
    }

    public @NotNull Long getReplyChatId() throws IllegalStateException {
        if (chat != null) {
            return chat.getId();
        } else if (user != null) {
            return user.getId();
        } else {
            throw new IllegalStateException("this update contained entity has no user or chat id`s");
        }
    }
}