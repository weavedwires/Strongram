package ru.daniil4jk.strongram.core.util.message;

import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import ru.daniil4jk.strongram.core.util.Lazy;

import java.util.List;
import java.util.Objects;

/**
 * Represents a long text message that may exceed Telegram's per-message limit.
 * Splits the original message into legal-length chunks on first access and caches the result.
 */
@ToString
public class LongMessage {
    @Getter
    private final String originalMessage;
    private final Lazy<List<String>> messages = new Lazy<>(this::split);

    public LongMessage(String originalMessage) {
        this.originalMessage = originalMessage;
    }

    private @NotNull @Unmodifiable List<String> split() {
        var splitter = new Splitter(originalMessage);
        splitter.split();
        return splitter.getSplat();
    }

    public List<String> asLegalLengthMessageList() {
        return messages.initOrGet();
    }

    @Override
    @SuppressWarnings("SimplifiableConditionalExpression")
    public boolean equals(Object o) {
        if (!(o instanceof LongMessage that)) return false;

        return Objects.equals(this.getOriginalMessage(), that.getOriginalMessage()) &&
                this.messages.isInitialized() == that.messages.isInitialized() &&
                (this.messages.isInitialized() ?
                        Objects.equals(this.asLegalLengthMessageList(), that.asLegalLengthMessageList()) :
                        true);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOriginalMessage(), messages);
    }
}

