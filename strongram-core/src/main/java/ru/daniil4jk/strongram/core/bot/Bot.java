package ru.daniil4jk.strongram.core.bot;

import ru.daniil4jk.strongram.core.response.responder.factory.SmartResponderFactory;

public interface Bot extends UpdateProcessor {
    String getUsername();
    SmartResponderFactory getResponderFactory();
}
