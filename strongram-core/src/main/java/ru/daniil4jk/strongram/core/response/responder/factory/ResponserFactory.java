package ru.daniil4jk.strongram.core.response.responder.factory;

import ru.daniil4jk.strongram.core.response.responder.sink.SinkResponder;

public interface ResponserFactory {
    SinkResponder create();
}
