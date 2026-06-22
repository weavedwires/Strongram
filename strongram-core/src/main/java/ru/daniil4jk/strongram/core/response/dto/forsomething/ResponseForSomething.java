package ru.daniil4jk.strongram.core.response.dto.forsomething;

import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import ru.daniil4jk.strongram.core.response.dto.Response;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

/**
 * Extends {@link Response} with a {@link CompletableFuture} that will be
 * completed with the API result once the method is executed.
 */
public interface ResponseForSomething<Method extends PartialBotApiMethod<?>, Object extends Serializable>
        extends Response<Method> {
    CompletableFuture<Object> getObject();
}
