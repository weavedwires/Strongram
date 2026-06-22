package ru.daniil4jk.strongram.core.response.sender;

import ru.daniil4jk.strongram.core.response.dto.Response;

import java.util.function.Consumer;

/**
 * A {@link java.util.function.Consumer} of {@link Response} objects that
 * serves as the terminal sink in the response pipeline, typically executed
 * by a {@link Sender}.
 */
public interface ResponseSink extends Consumer<Response<?>> {
}
