package ru.daniil4jk.strongram.webhook;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Utility to construct and validate webhook URL addresses. Ensures the address uses
 * HTTPS and normalises the protocol, host, and port components.
 */
public class AddressUtils {
    private static final String PROTOCOL_DIVIDER = "://";
    private static final String HTTPS_PREFIX = "https" + PROTOCOL_DIVIDER;

    public static @NotNull String constructSetWebhookAddress(String address) {
        address = address.trim();
        if (!address.toLowerCase().startsWith(HTTPS_PREFIX)) {
            if (address.contains(PROTOCOL_DIVIDER)) {
                throw new IllegalArgumentException("Server address will start from HTTPS");
            } else {
                address = HTTPS_PREFIX + address;
            }
        }

        try {
            URL url = URI.create(address).toURL();
            return constructSetWebhookAddress(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static @NotNull String constructSetWebhookAddress(URL url) {
        return url.getProtocol() + "://" +
                url.getHost() + ":" +
                (url.getPort() == -1 ? url.getDefaultPort() : url.getPort());
    }
}
