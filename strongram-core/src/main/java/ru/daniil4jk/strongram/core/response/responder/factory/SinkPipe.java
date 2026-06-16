package ru.daniil4jk.strongram.core.response.responder.factory;

import ru.daniil4jk.strongram.core.response.sender.ResponseSink;

public interface SinkPipe {
    void setPermanentCallback(ResponseSink callback);
    void setTempCallback(ResponseSink callback);
    void resetTempCallback();
    ResponseSink getCurrentCallback();
}
