package ru.daniil4jk.strongram.core.unboxer;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Function;

/**
 * Functional interface that extracts a value of type {@code O} from a Telegram {@link Update}.
 *
 * @param <O> the type of value extracted from the update
 */
public interface Unboxer<O> extends Function<Update, O> {
    @Override
    @NotNull
    default <V> Unboxer<V> andThen(@NotNull Function<? super O, ? extends V> after) {
        return update -> after.apply(apply(update));
    }
}
