package ru.daniil4jk.strongram.core.chain.configurator;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Ordered list builder for handler chains. Supports positional insertion
 * via {@link #add}, {@link #addBefore}, and {@link #addAfter} relative to
 * handlers of a given type.
 *
 * @param <T> the handler type being ordered
 */
@ToString
@EqualsAndHashCode
public class OrderConfigurator<T> {
    private final List<T> chain = new ArrayList<>();

    public OrderConfigurator<T> add(T handler) {
        this.chain.add(handler);
        return this;
    }


    public OrderConfigurator<T> addBefore(Class<? extends T> beforeWhat, T toAdd) {
        addConditionalInternal(0, beforeWhat, toAdd);
        return this;
    }


    public OrderConfigurator<T> addAfter(Class<? extends T> afterWhat, T toAdd) {
        addConditionalInternal(1, afterWhat, toAdd);
        return this;
    }

    private void addConditionalInternal(
            int padding,
            Class<? extends T> finding,
            T toAdd
    ) {
        int i = 0;
        for (
                Iterator<T> iterator = chain.iterator();
                iterator.hasNext();
                i++
        ) {
            T exist = iterator.next();
            if (finding.isInstance(exist)) {
                chain.add(i + padding, toAdd);
                break;
            }
        }
    }

    public List<T> asList() {
        return chain;
    }
}
