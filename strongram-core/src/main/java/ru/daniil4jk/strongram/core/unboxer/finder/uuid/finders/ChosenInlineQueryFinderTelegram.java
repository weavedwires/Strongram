package ru.daniil4jk.strongram.core.unboxer.finder.uuid.finders;

import org.telegram.telegrambots.meta.api.objects.inlinequery.ChosenInlineQuery;
import ru.daniil4jk.strongram.core.context.request.TelegramUUID;
import ru.daniil4jk.strongram.core.unboxer.finder.TelegramObjectFinderException;

/**
 * Extracts the user identity from a {@link ChosenInlineQuery} as a {@link TelegramUUID}.
 */
public class ChosenInlineQueryFinderTelegram extends TelegramUUIDFinder<ChosenInlineQuery> {
    @Override
    public Class<ChosenInlineQuery> getInputClass() {
        return ChosenInlineQuery.class;
    }

    @Override
    public TelegramUUID parse(ChosenInlineQuery t) throws TelegramObjectFinderException {
        return new TelegramUUID(null, t.getFrom());
    }
}
