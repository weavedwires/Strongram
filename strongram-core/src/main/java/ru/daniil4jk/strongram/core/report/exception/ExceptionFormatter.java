package ru.daniil4jk.strongram.core.report.exception;

import java.util.function.Function;

/**
 * Contract for formatting a {@link Throwable} into a human-readable string message
 * that can be sent to the end user.
 */
public interface ExceptionFormatter extends Function<Throwable, String> {
}
