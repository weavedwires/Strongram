package ru.daniil4jk.strongram.core.unboxer.finder.text.finders;

import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import ru.daniil4jk.strongram.core.unboxer.finder.TelegramObjectFinderException;

/**
 * Extracts the query text from an {@link InlineQuery}.
 */
public class InlineQueryTextFinder extends TextFinder<InlineQuery> {
    @Override
    public Class<InlineQuery> getInputClass() {
        return InlineQuery.class;
    }

    @Override
    public String parse(InlineQuery t) throws TelegramObjectFinderException {
        return t.getQuery();
    }
}
