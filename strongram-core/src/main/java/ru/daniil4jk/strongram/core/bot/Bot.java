package ru.daniil4jk.strongram.core.bot;

import ru.daniil4jk.strongram.core.response.responder.factory.SinkResponderFactory;

public interface Bot extends UpdateProcessor {
    String getUsername();

    SinkResponderFactory getResponderFactory();
}
