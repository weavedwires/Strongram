package ru.daniil4jk.strongram.core.unboxer.finder.text.finders;

import org.telegram.telegrambots.meta.api.objects.polls.PollAnswer;
import ru.daniil4jk.strongram.core.unboxer.finder.TelegramObjectFinderException;

/**
 * Extracts the selected option IDs from a {@link PollAnswer} as a string representation.
 */
public class PollAnswerTextFinder extends TextFinder<PollAnswer> {
    @Override
    public Class<PollAnswer> getInputClass() {
        return PollAnswer.class;
    }

    @Override
    public String parse(PollAnswer t) throws TelegramObjectFinderException {
        return t.getOptionIds().toString();
    }
}
