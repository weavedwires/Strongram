package ru.daniil4jk.strongram.core.filter;

import org.jetbrains.annotations.NotNull;
import ru.daniil4jk.strongram.core.context.request.RequestContext;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * A predicate-based filter over {@link RequestContext} with composable combinators
 * ({@link #and}, {@link #or}, {@link #negate}). Extends {@link Predicate} with
 * filter-specific defaults and static helpers like {@link #not(Filter)}.
 */
public interface Filter extends Predicate<RequestContext> {
    @Override
    @NotNull
    default Filter and(@NotNull Predicate<? super RequestContext> other) {
        return (t) -> test(t) && other.test(t);
    }

    @Override
    @NotNull
    default Filter negate() {
        return (t) -> !test(t);
    }

    @Override
    @NotNull
    default Filter or(@NotNull Predicate<? super RequestContext> other) {
        return (t) -> test(t) || other.test(t);
    }

    static Filter isEqual(Object targetRef) {
        return (null == targetRef)
                ? Objects::isNull
                : targetRef::equals;
    }

    static Filter not(Filter target) {
        Objects.requireNonNull(target);
        return target.negate();
    }
}
