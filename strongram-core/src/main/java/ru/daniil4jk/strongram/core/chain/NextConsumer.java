package ru.daniil4jk.strongram.core.chain;

/**
 * Contract for an element in a chain of responsibility. Implementing classes
 * accept a reference to the next handler in the chain via {@link #setNext}.
 *
 * @param <T> the handler type (typically the implementing class itself)
 */
public interface NextConsumer<T> {
    void setNext(T next);
}
