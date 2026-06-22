package ru.daniil4jk.strongram.core.unboxer.finder.uuid.finders;

import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import ru.daniil4jk.strongram.core.context.request.TelegramUUID;
import ru.daniil4jk.strongram.core.unboxer.finder.TelegramObjectFinderException;

/**
 * Extracts the user identity from an {@link InlineQuery} as a {@link TelegramUUID}.
 */
public class InlineQueryTelegramUUIDFinder extends TelegramUUIDFinder<InlineQuery> {
    @Override
    public Class<InlineQuery> getInputClass() {
        return InlineQuery.class;
    }

    @Override
    public TelegramUUID parse(InlineQuery t) throws TelegramObjectFinderException {
        return new TelegramUUID(new Chat(t.getFrom().getId(), t.getChatType()), t.getFrom());
    }
}
