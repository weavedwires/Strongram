package ru.daniil4jk.strongram.core.response.responder.factory;

import lombok.Setter;
import ru.daniil4jk.strongram.core.response.responder.sink.SinkResponder;
import ru.daniil4jk.strongram.core.response.sender.ResponseSink;

public class SinkResponderFactoryImpl implements SinkResponderFactory {
    private final ThreadLocal<ResponseSink> tempCallback = new ThreadLocal<>();
    @Setter
    private ResponseSink permanentCallback;

    @Override
    public void setTempCallback(ResponseSink callback) {
        tempCallback.set(callback);
    }

    @Override
    public void resetTempCallback() {
        tempCallback.remove();
    }

    @Override
    public ResponseSink getCurrentCallback() {
        var tempCallbackCheckpoint = tempCallback.get();
        return tempCallbackCheckpoint != null ? tempCallbackCheckpoint : permanentCallback;
    }

    @Override
    public SinkResponder create() {
        return new SinkResponder(getCurrentCallback());
    }
}
