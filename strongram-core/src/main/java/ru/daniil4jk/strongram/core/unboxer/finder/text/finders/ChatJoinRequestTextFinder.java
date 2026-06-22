package ru.daniil4jk.strongram.core.unboxer.finder.text.finders;

import org.telegram.telegrambots.meta.api.objects.ChatJoinRequest;
import ru.daniil4jk.strongram.core.unboxer.finder.TelegramObjectFinderException;

/**
 * Extracts the bio text from a {@link ChatJoinRequest}.
 */
public class ChatJoinRequestTextFinder extends TextFinder<ChatJoinRequest> {
    @Override
    public Class<ChatJoinRequest> getInputClass() {
        return ChatJoinRequest.class;
    }

    @Override
    public String parse(ChatJoinRequest t) throws TelegramObjectFinderException {
        return t.getBio();
    }
}
