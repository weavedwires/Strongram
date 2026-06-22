package ru.daniil4jk.strongram.longpolling.adapter.provider;

/**
 * Provides a Telegram bot token string. Implemented by adapters that need to
 * supply their token for registration or client creation.
 */
public interface TokenProvider {
    String getToken();
}
