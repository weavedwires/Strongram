package ru.daniil4jk.strongram.core.response.responder.factory;

import ru.daniil4jk.strongram.core.response.sender.ResponseSink;

/**
 * Defines the lifecycle for managing a {@link ru.daniil4jk.strongram.core.response.sender.ResponseSink}
 * callback with permanent and per-request temporary overrides.
 */
public interface SinkPipe {
    void setPermanentCallback(ResponseSink callback);
    void setTempCallback(ResponseSink callback);
    void resetTempCallback();
    ResponseSink getCurrentCallback();
}
