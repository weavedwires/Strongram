package ru.daniil4jk.strongram.core.response.dto;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@ToString
@EqualsAndHashCode
public class ForgetResponse<
        Method extends PartialBotApiMethod<?>
        > implements Response<Method> {

    private final Method message;
    private final SendFunction<?> send;

    @SuppressWarnings("rawtypes")
    public ForgetResponse(Method message, SendFunction function) {
        this.message = message;
        this.send = function;
    }

    @Override
    public Method getEntry() {
        return message;
    }

    @Override
    public void sendUsing(TelegramClient client) {
        try {
            send.apply(client);
        } catch (TelegramApiException e) {
            log.warn("cannot send message", e);
        }
    }

    @Override
    public boolean isObjectRequired() {
        return false;
    }

    @Override
    public String getChatId() {
        try {
            var method = message.getClass().getMethod("getChatId");
            var chatId = method.invoke(message);
            return chatId != null ? chatId.toString() : "unknown";
        } catch (Exception e) {
            log.warn(
                    "Cannot extract chatId from message: {}",
                    message.getClass().getSimpleName()
            );
            return "unknown";
        }
    }
}
