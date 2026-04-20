package ru.daniil4jk.strongram.core.report.exception;

import java.util.function.Function;

public interface ExceptionFormatter extends Function<Throwable, String> {
}
