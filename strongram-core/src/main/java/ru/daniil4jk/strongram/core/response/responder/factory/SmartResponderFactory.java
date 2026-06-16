package ru.daniil4jk.strongram.core.response.responder.factory;

import org.jetbrains.annotations.Nullable;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.daniil4jk.strongram.core.context.request.TelegramUUID;
import ru.daniil4jk.strongram.core.response.responder.smart.SmartResponder;

public interface SmartResponderFactory extends ResponderFactory {
    SmartResponder createSmart(TelegramUUID uuid, @Nullable CallbackQuery queryToAnswer);
}
