package ru.daniil4jk.strongram.core.context.storage;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import ru.daniil4jk.strongram.core.util.Lazy;

import java.util.*;

/**
 * In-memory implementation of {@link Storage} backed by a lazily-initialized
 * {@link HashMap}. Supports add, set, get, and collection retrieval operations.
 */
@ToString
@EqualsAndHashCode
public class InMemoryStorage implements Storage {
    private final Lazy<Map<String, Object>> inherit = new Lazy<>(HashMap::new);

    @Override
    public <T> void add(String newKey, T newValue) {
        inherit.initOrGet().compute(
                newKey,
                (existingKey, existingValue) -> {
                    if (existingValue != null) {
                        throwKeyExistException(existingKey, existingValue);
                    }
                    return newValue;
                }
        );
    }

    @Override
    public <T> void set(String newKey, T newValue) {
        inherit.initOrGet().compute(
                newKey,
                (existingKey, existingValue) -> newValue
        );
    }

    private void throwKeyExistException(String keyName, Object value) {
        throw new IllegalArgumentException(
                "Key %s already exist in map with value %s"
                        .formatted(keyName, value)
        );
    }

    @Override
    public <T> T get(Class<T> classOfReturnValue, String key) {
        return get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        if (!inherit.isInitialized()) return null;
        return (T) inherit.get().get(key);
    }

    @Override
    public <T> @NotNull Collection<T> getCollection(Class<T> classOfReturnEntryValue, String key) {
        return getCollection(key);
    }

    @Override
    public Map<String, Object> getAll() {
        return inherit.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> @NotNull Collection<T> getCollection(String key) {
        if (!inherit.isInitialized()) return List.of();

        Object value = inherit.get().get(key);

        if (value == null) {
            return List.of();
        }

        try {
            return (Collection<T>) value;
        } catch (ClassCastException e) { /*continue*/ }

        try {
            return Arrays.asList((T[]) value);
        } catch (ClassCastException e) { /*continue*/ }

        try {
            return List.of((T) value);
        } catch (ClassCastException e) { /*continue*/ }

        throw new IllegalStateException("can`t findIn field %s in context".formatted(key));
    }
}
