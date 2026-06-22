package ru.daniil4jk.strongram.core.unboxer.finder.text.finders;

import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import ru.daniil4jk.strongram.core.unboxer.finder.Finder;
import ru.daniil4jk.strongram.core.unboxer.finder.TelegramObjectFinderException;

/**
 * Base class for {@link Finder} implementations that extract textual data from a {@link BotApiObject}.
 *
 * @param <I> the input {@link BotApiObject} type
 */
public abstract class TextFinder<I extends BotApiObject> implements Finder<I, String> {
    @Override
    public Class<String> getOutputClass() {
        return String.class;
    }

    protected void throwNotContainsStringException(Class<?> clazz) {
        throw new TelegramObjectFinderException("%s has`nt String payload"
                .formatted(clazz.getName()));
    }
}
