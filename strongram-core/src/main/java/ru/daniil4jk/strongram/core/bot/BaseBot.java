package ru.daniil4jk.strongram.core.bot;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.daniil4jk.strongram.core.response.responder.factory.SinkResponderFactory;
import ru.daniil4jk.strongram.core.response.responder.factory.SinkResponderFactoryImpl;
import ru.daniil4jk.strongram.core.response.sender.ResponseSink;

/**
 * Abstract base implementation of {@link Bot}. Provides automatic
 * {@code @}-prefixing of the username, a shared {@link SinkResponderFactory},
 * and storage for a default callback. Subclasses need only implement the
 * update-processing logic.
 */
@Getter
@Slf4j
@ToString
@EqualsAndHashCode
public abstract class BaseBot implements Bot {
    private static final String WHITESPACE = " ";
    private static final String DOG = "@";

    @Getter
    private final SinkResponderFactory responderFactory = new SinkResponderFactoryImpl();
    @Getter
    private final String username;
    @Setter
    @Getter
    private ResponseSink defaultCallback;

    public BaseBot(@Nullable String username) {
        this.username = username != null ? formatUsername(username) : null;
    }

    private static @NotNull String formatUsername(@NotNull String raw) {
        raw = raw.trim().toLowerCase();
        if (raw.split(WHITESPACE).length > 1) {
            throw new IllegalArgumentException(
                    "Username %s contains whitespace".formatted(raw)
            );
        }
        if (!raw.startsWith(DOG)) {
            raw = DOG + raw;
        }
        return raw;
    }
}