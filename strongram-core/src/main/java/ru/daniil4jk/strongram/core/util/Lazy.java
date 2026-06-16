package ru.daniil4jk.strongram.core.util;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
@ToString
@EqualsAndHashCode
public class Lazy<T> implements Supplier<T> {
    private final Supplier<T> creator;
    private volatile T value;

    public Lazy(Supplier<T> creator) {
        this.creator = creator;
    }

    public Lazy(T value) {
        this.creator = null;
        this.value = value;
    }

    public void initIfNeed() {
        if (!isInitialized()) {
            synchronized (this) {
                if (!isInitialized()) {
                    try {
                        assert creator != null;
                        value = creator.get();
                    } catch (Exception e) {
                        log.error("Lazy initialization throws exception", e);
                    }
                }
            }
        }
    }

    public boolean isInitialized() {
        return value != null;
    }

    @Override
    public T get() {
        return value;
    }

    public T initOrGet() {
        initIfNeed();
        return get();
    }
}
