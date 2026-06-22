package ru.daniil4jk.strongram.core.chain.configurator;

/**
 * Strategy interface for configuring an {@link OrderConfigurator}.
 * Implementations define the order and set of handlers in a chain.
 *
 * @param <T> the handler type being configured
 */
public interface OrderConfiguratorTuner<T> {
    void apply(OrderConfigurator<T> orderConfigurator);
}
