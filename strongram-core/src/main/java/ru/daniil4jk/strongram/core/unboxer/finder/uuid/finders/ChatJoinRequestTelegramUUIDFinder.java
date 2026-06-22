package ru.daniil4jk.strongram.core.unboxer.finder.uuid.finders;

import org.telegram.telegrambots.meta.api.objects.ChatJoinRequest;
import ru.daniil4jk.strongram.core.context.request.TelegramUUID;
import ru.daniil4jk.strongram.core.unboxer.finder.TelegramObjectFinderException;

/**
 * Extracts the chat and user identity from a {@link ChatJoinRequest} as a {@link TelegramUUID}.
 */
public class ChatJoinRequestTelegramUUIDFinder extends TelegramUUIDFinder<ChatJoinRequest> {
    @Override
    public Class<ChatJoinRequest> getInputClass() {
        return ChatJoinRequest.class;
    }

    @Override
    public TelegramUUID parse(ChatJoinRequest t) throws TelegramObjectFinderException {
        return new TelegramUUID(t.getChat(), t.getUser());
    }
}
