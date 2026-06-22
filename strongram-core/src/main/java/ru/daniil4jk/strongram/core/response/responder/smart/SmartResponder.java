package ru.daniil4jk.strongram.core.response.responder.smart;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.daniil4jk.strongram.core.response.responder.Responder;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * High-level responder that extends {@link Responder} with convenience
 * methods for sending text, files, and keyboards to a chat, as well as
 * answering callback queries. Long messages are automatically split into
 * Telegram-legal chunks.
 */
public interface SmartResponder extends Responder {
    void send(String text);

    void send(String text, File file, MediaType type);

    void send(String text, ReplyKeyboard keyboard);

    void send(String text, File file, MediaType type, ReplyKeyboard keyboard);

    CompletableFuture<List<Message>> sendForObject(String text);

    CompletableFuture<List<Message>> sendForObject(String text, File file, MediaType type);

    CompletableFuture<List<Message>> sendForObject(String text, ReplyKeyboard keyboard);

    CompletableFuture<List<Message>> sendForObject(String text, File file, MediaType type, ReplyKeyboard keyboard);

    void send(Long id, String text);

    void send(Long id, String text, File file, MediaType type);

    void send(Long id, String text, ReplyKeyboard keyboard);

    void send(Long id, String text, File file, MediaType type, ReplyKeyboard keyboard);

    CompletableFuture<List<Message>> sendForObject(Long id, String text);

    CompletableFuture<List<Message>> sendForObject(Long id, String text, File file, MediaType type);

    CompletableFuture<List<Message>> sendForObject(Long id, String text, ReplyKeyboard keyboard);

    CompletableFuture<List<Message>> sendForObject(Long id, String text, File file, MediaType type, ReplyKeyboard keyboard);

    enum MediaType {
        Photo,
        Video,
        Audio,
        Voice,
        Document
    }

    void answerCallbackQuery(CallbackQuery query);

    void answerCallbackQuery(CallbackQuery query, String userNotification);
}
