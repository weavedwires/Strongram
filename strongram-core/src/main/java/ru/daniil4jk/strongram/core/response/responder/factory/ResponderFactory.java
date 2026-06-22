package ru.daniil4jk.strongram.core.response.responder.factory;

import ru.daniil4jk.strongram.core.response.responder.sink.SinkResponder;

/**
 * Factory contract for creating {@link ru.daniil4jk.strongram.core.response.responder.sink.SinkResponder}
 * instances per request.
 */
public interface ResponderFactory {
    SinkResponder create();
}
