package ru.daniil4jk.strongram.core.unboxer.finder.uuid.finders;

import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.daniil4jk.strongram.core.context.request.TelegramUUID;
import ru.daniil4jk.strongram.core.unboxer.finder.TelegramObjectFinderException;

/**
 * Extracts the chat and user identity from a {@link Message} as a {@link TelegramUUID}.
 */
public class MessageTelegramUUIDFinder extends TelegramUUIDFinder<Message> {
    @Override
    public Class<Message> getInputClass() {
        return Message.class;
    }

    @Override
    public TelegramUUID parse(Message t) throws TelegramObjectFinderException {
        return new TelegramUUID(t.getChat(), t.getFrom());
    }
}
