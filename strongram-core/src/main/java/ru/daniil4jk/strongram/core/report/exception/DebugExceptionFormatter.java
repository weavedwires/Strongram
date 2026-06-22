package ru.daniil4jk.strongram.core.report.exception;

import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Extends {@link InfoExceptionFormatter} by appending the full stack trace
 * for developer diagnostics alongside the user-friendly message.
 */
class DebugExceptionFormatter extends InfoExceptionFormatter {
    @Override
    public @NotNull String apply(Throwable th) {
        return super.apply(th) +
                Strings.repeat(System.lineSeparator(), 2) +
                assemblyDeveloperMessage(th);
    }

    private @NotNull String assemblyDeveloperMessage(@NotNull Throwable th) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        th.printStackTrace(new PrintStream(out));
        String stackTraceInString = out.toString();

        return "Информация ДЛЯ РАЗРАБОТЧИКОВ:" +
                System.lineSeparator() +
                stackTraceInString;
    }
}
