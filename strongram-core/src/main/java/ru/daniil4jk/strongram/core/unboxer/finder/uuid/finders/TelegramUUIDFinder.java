package ru.daniil4jk.strongram.core.unboxer.finder.uuid.finders;

import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import ru.daniil4jk.strongram.core.context.request.TelegramUUID;
import ru.daniil4jk.strongram.core.unboxer.finder.Finder;

/**
 * Base class for {@link Finder} implementations that extract a {@link TelegramUUID} from a {@link BotApiObject}.
 *
 * @param <I> the input {@link BotApiObject} type
 */
public abstract class TelegramUUIDFinder<I extends BotApiObject> implements Finder<I, TelegramUUID> {
    @Override
    public Class<TelegramUUID> getOutputClass() {
        return TelegramUUID.class;
    }
}
