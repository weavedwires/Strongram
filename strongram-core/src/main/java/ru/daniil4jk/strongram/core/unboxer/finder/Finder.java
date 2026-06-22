package ru.daniil4jk.strongram.core.unboxer.finder;

import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;

/**
 * Contract for parsing an output value of type {@code O} from a Telegram Bot API object of type {@code I}.
 *
 * @param <I> the input {@link BotApiObject} type
 * @param <O> the output type produced by this finder
 */
public interface Finder<I extends BotApiObject, O> {
    Class<I> getInputClass();

    Class<O> getOutputClass();

    O parse(I t) throws TelegramObjectFinderException;
}
