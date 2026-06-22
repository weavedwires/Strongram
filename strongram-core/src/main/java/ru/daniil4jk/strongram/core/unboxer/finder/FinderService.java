package ru.daniil4jk.strongram.core.unboxer.finder;

import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Registry and dispatcher that routes a {@link BotApiObject} to the appropriate {@link Finder}
 * to produce an output of type {@code O}.
 *
 * @param <O> the output type produced by all registered finders
 */
public abstract class FinderService<O> {
    //Map<Class<I>, Parser<I, O>>
    private final Map<Class<?>, Finder<?, O>> parserMap = new HashMap<>();

    public FinderService(Collection<Finder<?, O>> finders) {
        this();
        for (Finder<?, O> finder : finders) {
            parserMap.put(finder.getInputClass(), finder);
        }
    }

    public FinderService() {
        for (var parser : SPIFinderRegistry.getInstance().getByOutputClass(getOutputClass())) {
            parserMap.put(parser.getInputClass(), parser);
        }
    }

    public <I extends BotApiObject> void addFinder(Finder<I, O> finder) {
        parserMap.put(finder.getInputClass(), finder);
    }

    @SuppressWarnings("unchecked")
    public <I extends BotApiObject> O findIn(I i) throws TelegramObjectFinderException {
        return ((Finder<I, O>) Optional.ofNullable(parserMap.get(i.getClass()))
                .orElseThrow(() ->
                        new TelegramObjectFinderException("Not enough parser for types I: %s, O: %s"
                                .formatted(i.getClass().getName(), getOutputClass().getName()))))
                .parse(i);
    }

    protected abstract Class<O> getOutputClass();
}
