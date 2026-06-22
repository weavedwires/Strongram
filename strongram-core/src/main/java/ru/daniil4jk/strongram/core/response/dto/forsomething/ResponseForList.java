package ru.daniil4jk.strongram.core.response.dto.forsomething;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.daniil4jk.strongram.core.response.dto.SendFunction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * A {@link ResponseForSomething} that captures a list result (e.g. from
 * {@code SendMediaGroup}) and completes its future when the API call
 * succeeds or fails.
 */
@Slf4j
@ToString
@EqualsAndHashCode
public class ResponseForList<
        Method extends PartialBotApiMethod<ArrayList<Object>>,
        Object extends Serializable
        > implements ResponseForSomething<Method, ArrayList<Object>> {

    private final Method message;
    private final SendFunction<List<Object>> send;
    private final CompletableFuture<ArrayList<Object>> future =
            new CompletableFuture<>();

    public ResponseForList(Method message, SendFunction<List<Object>> send) {
        this.message = message;
        this.send = send;
    }

    @Override
    public Method getEntry() {
        return message;
    }

    @Override
    public void sendUsing(TelegramClient client) {
        try {
            List<Object> list = send.apply(client);
            if (list instanceof ArrayList<Object> arr) {
                future.complete(arr);
            } else {
                future.complete(new ArrayList<>(list));
            }
        } catch (TelegramApiException e) {
            future.completeExceptionally(e);
        }
    }

    @Override
    public CompletableFuture<ArrayList<Object>> getObject() {
        return future;
    }

    @Override
    public boolean isObjectRequired() {
        return true;
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
