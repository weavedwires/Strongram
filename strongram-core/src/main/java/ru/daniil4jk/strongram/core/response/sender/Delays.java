package ru.daniil4jk.strongram.core.response.sender;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

import static java.time.temporal.ChronoUnit.MILLIS;

public class Delays {
    public static final Delay NULL_DELAY = new Delay(0, Duration.ZERO);
    public static final Delay[] DELAYS = new Delay[]{ //todo протестировать задержки
            NULL_DELAY,
            new Delay(30, Duration.of(300, MILLIS)),
            new Delay(100, Duration.of(500, MILLIS)),
    };

    static {
        Arrays.sort(
                DELAYS,
                (o1, o2) -> Integer.compare(o2.minMessages, o1.minMessages)
        );
    }

    public static long getOptimalDelayInMills(@NotNull Collection<?> collection) {
        return getOptimalDelay(collection).delayBetweenMessages().toMillis();
    }

    public static Delay getOptimalDelay(@NotNull Collection<?> collection) {
        int messages = collection.size();
        for (Delay delay : DELAYS) {
            if (messages > delay.minMessages) {
                return delay;
            }
        }
        return NULL_DELAY;
    }

    public record Delay(
            int minMessages,
            Duration delayBetweenMessages
    ) {
    }
}
