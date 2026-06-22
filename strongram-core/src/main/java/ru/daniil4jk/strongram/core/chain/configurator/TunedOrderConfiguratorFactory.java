package ru.daniil4jk.strongram.core.chain.configurator;

/**
 * Factory that creates an {@link OrderConfigurator} by applying a user-supplied
 * {@link OrderConfiguratorTuner} tuning function.
 *
 * @param <T> the handler type produced by the configurator
 */
public class TunedOrderConfiguratorFactory<T> {
    private final OrderConfiguratorTuner<T> userConfiguration;

    public TunedOrderConfiguratorFactory(OrderConfiguratorTuner<T> userConfiguration) {
        this.userConfiguration = userConfiguration;
    }

    public OrderConfigurator<T> get() {
        OrderConfigurator<T> chainOrderConfigurator = new OrderConfigurator<>();
        userConfiguration.apply(chainOrderConfigurator);
        return chainOrderConfigurator;
    }
}
