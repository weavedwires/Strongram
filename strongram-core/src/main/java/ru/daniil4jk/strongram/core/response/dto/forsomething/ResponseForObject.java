package ru.daniil4jk.strongram.core.response.dto.forsomething;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.daniil4jk.strongram.core.response.dto.SendFunction;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

/**
 * A {@link ResponseForSomething} that captures a single object result and
 * completes its future when the API call succeeds or fails.
 */
@Slf4j
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ResponseForObject<
        Method extends PartialBotApiMethod<Object>,
        Object extends Serializable
        > implements ResponseForSomething<Method, Object> {

    private final Method message;
    private final SendFunction<Object> send;
    private final CompletableFuture<Object> future = new CompletableFuture<>();

    @Override
    public Method getEntry() {
        return message;
    }

    @Override
    public void sendUsing(TelegramClient client) {
        try {
            Object result = send.apply(client);
            future.complete(result);
        } catch (TelegramApiException e) {
            future.completeExceptionally(e);
        }
    }

    @Override
    public boolean isObjectRequired() {
        return true;
    }

    @Override
    public CompletableFuture<Object> getObject() {
        return future;
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
