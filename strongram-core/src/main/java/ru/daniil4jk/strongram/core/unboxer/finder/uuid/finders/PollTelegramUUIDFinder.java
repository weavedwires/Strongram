package ru.daniil4jk.strongram.core.unboxer.finder.uuid.finders;

import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import ru.daniil4jk.strongram.core.context.request.TelegramUUID;
import ru.daniil4jk.strongram.core.unboxer.finder.TelegramObjectFinderException;


/**
 * {@link TelegramUUIDFinder} for {@link Poll} — always throws because polls carry no user or chat identity.
 */
public class PollTelegramUUIDFinder extends TelegramUUIDFinder<Poll> {
    @Override
    public Class<Poll> getInputClass() {
        return Poll.class;
    }

    @Override
    public TelegramUUID parse(Poll t) throws TelegramObjectFinderException {
        throw new TelegramObjectFinderException("%s has`nt TelegramUUID payload"
                .formatted(t.getClass().getName()));
    }
}
