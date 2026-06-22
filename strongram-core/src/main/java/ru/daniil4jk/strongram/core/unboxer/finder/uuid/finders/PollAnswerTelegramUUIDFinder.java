package ru.daniil4jk.strongram.core.unboxer.finder.uuid.finders;

import org.telegram.telegrambots.meta.api.objects.polls.PollAnswer;
import ru.daniil4jk.strongram.core.context.request.TelegramUUID;
import ru.daniil4jk.strongram.core.unboxer.finder.TelegramObjectFinderException;

/**
 * Extracts the voter or chat identity from a {@link PollAnswer} as a {@link TelegramUUID}.
 */
public class PollAnswerTelegramUUIDFinder extends TelegramUUIDFinder<PollAnswer> {

    @Override
    public Class<PollAnswer> getInputClass() {
        return PollAnswer.class;
    }

    @Override
    public TelegramUUID parse(PollAnswer t) throws TelegramObjectFinderException {
        return new TelegramUUID(t.getVoterChat(), t.getUser());
    }
}
