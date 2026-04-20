package ru.daniil4jk.strongram.core.response.responder.factory;

import lombok.RequiredArgsConstructor;
import ru.daniil4jk.strongram.core.context.request.TelegramUUID;
import ru.daniil4jk.strongram.core.response.responder.smart.SmartResponder;
import ru.daniil4jk.strongram.core.response.responder.smart.SmartResponderImpl;

@RequiredArgsConstructor
public class SmartResponderFactoryImpl extends ResponserFactoryImpl implements SmartResponderFactory {

    @Override
    public SmartResponder createSmart(TelegramUUID uuid) {
        return new SmartResponderImpl(uuid, create());
    }
}
