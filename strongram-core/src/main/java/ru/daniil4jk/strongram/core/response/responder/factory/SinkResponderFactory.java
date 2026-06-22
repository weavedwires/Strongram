package ru.daniil4jk.strongram.core.response.responder.factory;

/**
 * Combines {@link ResponderFactory} and {@link SinkPipe} into a single
 * interface, allowing creation and dynamic re-routing of sinks.
 */
public interface SinkResponderFactory extends ResponderFactory, SinkPipe{
}
