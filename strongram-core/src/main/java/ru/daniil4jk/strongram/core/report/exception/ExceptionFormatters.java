package ru.daniil4jk.strongram.core.report.exception;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Static factory for predefined {@link ExceptionFormatter} implementations:
 * {@link #info()} for concise user-friendly messages and {@link #debug()} for
 * developer-oriented output with full stack traces.
 */
public class ExceptionFormatters {
    @Contract(value = " -> new", pure = true)
    public static @NotNull ExceptionFormatter info() {
        return new InfoExceptionFormatter();
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull ExceptionFormatter debug() {
        return new DebugExceptionFormatter();
    }
}
