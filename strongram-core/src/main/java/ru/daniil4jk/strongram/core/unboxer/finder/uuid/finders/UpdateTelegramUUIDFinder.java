package ru.daniil4jk.strongram.core.unboxer.finder.uuid.finders;

import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import ru.daniil4jk.strongram.core.context.request.TelegramUUID;
import ru.daniil4jk.strongram.core.unboxer.finder.UpdateFinder;
import ru.daniil4jk.strongram.core.unboxer.finder.uuid.TelegramUUIDFinderService;

/**
 * Extracts a {@link TelegramUUID} from an {@link Update} by delegating to {@link ru.daniil4jk.strongram.core.unboxer.finder.uuid.TelegramUUIDFinderService}.
 */
public class UpdateTelegramUUIDFinder extends UpdateFinder<TelegramUUID> {
    @Override
    public Class<TelegramUUID> getOutputClass() {
        return TelegramUUID.class;
    }

    @Override
    protected <I extends BotApiObject> TelegramUUID parseInternal(I t) {
        return TelegramUUIDFinderService.getInstance().findIn(t);
    }
}
