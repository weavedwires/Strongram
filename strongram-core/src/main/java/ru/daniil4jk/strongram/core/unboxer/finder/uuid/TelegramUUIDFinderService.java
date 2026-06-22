package ru.daniil4jk.strongram.core.unboxer.finder.uuid;

import lombok.Getter;
import ru.daniil4jk.strongram.core.context.request.TelegramUUID;
import ru.daniil4jk.strongram.core.unboxer.finder.FinderService;

/**
 * Singleton {@link FinderService} that extracts {@link TelegramUUID} identifiers from Telegram objects.
 */
public class TelegramUUIDFinderService extends FinderService<TelegramUUID> {
    @Getter
    private static final TelegramUUIDFinderService instance = new TelegramUUIDFinderService();

    private TelegramUUIDFinderService() {
    }

    @Override
    protected Class<TelegramUUID> getOutputClass() {
        return TelegramUUID.class;
    }
}
