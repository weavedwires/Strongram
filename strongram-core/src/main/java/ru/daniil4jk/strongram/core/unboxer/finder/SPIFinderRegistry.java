package ru.daniil4jk.strongram.core.unboxer.finder;

import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;

import java.util.Collection;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * Singleton that discovers {@link Finder} implementations via Java {@link java.util.ServiceLoader}
 * and provides lookup by input or output class.
 */
public class SPIFinderRegistry {
    private static final SPIFinderRegistry me = new SPIFinderRegistry();
    @SuppressWarnings("rawtypes")
    private static final ServiceLoader<Finder> loader = ServiceLoader.load(Finder.class);

    private SPIFinderRegistry() {
    }

    public static SPIFinderRegistry getInstance() {
        return me;
    }

    @SuppressWarnings("unchecked")
    public <O> Collection<? extends Finder<?, O>> getByOutputClass(Class<O> outputClass) {
        return loader.stream()
                .filter(p -> Objects.equals(p.get().getOutputClass(), outputClass))
                .map(parserProvider -> (Finder<? extends BotApiObject, O>) parserProvider.get())
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public <I extends BotApiObject> Collection<? extends Finder<I, ?>> getByInputClass(Class<I> inputClass) {
        return loader.stream()
                .filter(p -> Objects.equals(p.get().getInputClass(), inputClass))
                .map(parserProvider -> (Finder<I, ? extends BotApiMethod<?>>) parserProvider.get())
                .collect(Collectors.toList());
    }
}
