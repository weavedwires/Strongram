# Strongram

Современный Java-фреймворк для разработки Telegram ботов с поддержкой диалогов, команд, фильтров и гибкой системы обработки сообщений.

## 📖 Описание

Strongram — это мощный и гибкий фреймворк для создания Telegram ботов на Java. Некоторые его возможности:

- **Модульная архитектура** - подключайте только нужные компоненты (Long Polling или Webhook)
- **Цепочки обработчиков** - паттерн Chain of Responsibility для гибкой обработки входящих сообщений
- **Обработка команд** - поддержка команд в личных сообщениях, групповых чатах и inline mode
- **Система диалогов** - легко создавайте многошаговые диалоги с машиной состояний
- **Фильтры** - декларативная фильтрация сообщений перед их попаданием в вашу логику
- **Интерактивные клавиатуры** - объявляйте логику кнопок клавиатуры прямо в кнопках
- **Smart Responder** - отправка сообщений в одну строку, без ручного создания telegram-специфичных DTO

### 📋 Требования

- Java 17 или выше
- Maven 3.6+

### 📦 Установка

Добавьте зависимость в ваш `pom.xml`:

#### Для Long Polling

```xml
<dependency>
    <groupId>ru.daniil4jk</groupId>
    <artifactId>strongram-longpolling</artifactId>
    <version>0.6.0</version>
</dependency>
```

#### Для Webhook

```xml
<dependency>
    <groupId>ru.daniil4jk</groupId>
    <artifactId>strongram-webhook</artifactId>
    <version>0.6.0</version>
</dependency>
```

## 🚀 Примеры использования

### 1. Echo Bot — "Hello World" приложение

Простейший бот, который повторяет все текстовые сообщения пользователя.

```java
import ru.daniil4jk.strongram.core.bot.ChainedBot;
import ru.daniil4jk.strongram.core.chain.configurator.OrderConfigurator;
import ru.daniil4jk.strongram.core.upstream.UpstreamHandler;

public class EchoBot extends ChainedBot {
    
    public EchoBot(String username) {
        super(username);
    }
    
    @Override
    protected void configureUpstream(OrderConfigurator<UpstreamHandler> chain) {
        chain.add(new EchoHandler());
    }
}
```

```java
import ru.daniil4jk.strongram.core.context.request.RequestContext;
import ru.daniil4jk.strongram.core.filter.Filter;
import ru.daniil4jk.strongram.core.filter.Filters;
import ru.daniil4jk.strongram.core.upstream.FilteredUpstreamHandler;
import ru.daniil4jk.strongram.core.unboxer.As;

public class EchoHandler extends FilteredUpstreamHandler {

    @Override
    protected Filter getFilter() {
        return Filters.hasMessageText();
    }

    @Override
    protected void processFiltered(RequestContext ctx) {
        String text = ctx.getRequest(As.messageText());
        ctx.getResponder().send("Вы сказали: " + text);
    }
}
```

### 2. Бот с командой '/hello'

```java
import ru.daniil4jk.strongram.core.bot.ChainedBot;
import ru.daniil4jk.strongram.core.chain.configurator.OrderConfigurator;
import ru.daniil4jk.strongram.core.upstream.UpstreamHandler;

public class CommandBot extends ChainedBot {
    
    public CommandBot(String username) {
        super(username);
    }
    
    @Override
    protected void configureUpstream(OrderConfigurator<UpstreamHandler> chain) {
        chain.add(new GreetCommandHandler());
    }
}
```

```java
import ru.daniil4jk.strongram.core.command.EachCommandHandler;
import ru.daniil4jk.strongram.core.context.request.RequestContext;
import ru.daniil4jk.strongram.core.upstream.preinstalled.CommandUpstreamHandler;

import java.util.Map;

public class GreetCommandHandler extends CommandUpstreamHandler {

    @Override
    protected Map<String, EachCommandHandler> getCommands() {
        return Map.of(
                "hello", this::handleHello
        );
    }

    private void handleHello(RequestContext ctx, String[] args) {
        ctx.getResponder().send("Привет");
    }
}
```

### 3. Обработка ошибок и неизвестных команд

Пример с обработкой исключений и ситуаций, когда команда не распознана.

```java
import ru.daniil4jk.strongram.core.bot.ChainedBot;
import ru.daniil4jk.strongram.core.chain.configurator.OrderConfigurator;
import ru.daniil4jk.strongram.core.upstream.UpstreamHandler;
import ru.daniil4jk.strongram.core.upstream.preinstalled.ExceptionReportUpstreamHandler;
import ru.daniil4jk.strongram.core.report.exception.ExceptionFormatters;

public class RobustBot extends ChainedBot {

    public RobustBot(String username) {
        super(username);
    }

    @Override
    protected void configureUpstream(OrderConfigurator<UpstreamHandler> chain) {
        // Встроенный обработчик исключений (должен быть первым чтобы через try-catch ловить исключения)
        chain.add(new ExceptionReportUpstreamHandler(ExceptionFormatters.debug()));

        // Наш обработчик команд
        chain.add(new RobustCommandHandler());

        // Встроенный обработчик исключений
        chain.add(new CannotProcessUpstreamHandler("Неизвестная команда"));
    }
}
```

```java
import ru.daniil4jk.strongram.core.command.EachCommandHandler;
import ru.daniil4jk.strongram.core.context.request.RequestContext;
import ru.daniil4jk.strongram.core.upstream.preinstalled.CommandUpstreamHandler;

import java.util.Map;

public class RobustCommandHandler extends CommandUpstreamHandler {

    @Override
    protected Map<String, EachCommandHandler> getCommands() {
        return Map.of(
                "start", this::handleStart,
                "error", this::handleError,
                "divide", this::handleDivide
        );
    }

    private void handleStart(RequestContext ctx, String[] args) {
        ctx.getResponder().send("Бот запущен! Попробуйте /error");
    }

    private void handleError(RequestContext ctx, String[] args) {
        throw new RuntimeException("Это тестовая ошибка для демонстрации обработки исключений!");
    }
}
```

### 4. Отправка файлов

Пример отправки различных типов файлов.

```java
import ru.daniil4jk.strongram.core.bot.ChainedBot;
import ru.daniil4jk.strongram.core.chain.configurator.OrderConfigurator;
import ru.daniil4jk.strongram.core.upstream.UpstreamHandler;

public class FileBot extends ChainedBot {
    
    public FileBot(String username) {
        super(username);
    }
    
    @Override
    protected void configureUpstream(OrderConfigurator<UpstreamHandler> chain) {
        chain.add(new FileCommandHandler());
    }
}
```

```java
import ru.daniil4jk.strongram.core.command.EachCommandHandler;
import ru.daniil4jk.strongram.core.context.request.RequestContext;
import ru.daniil4jk.strongram.core.upstream.preinstalled.TextCommandUpstreamHandler;
import ru.daniil4jk.strongram.core.response.responder.smart.SmartResponder;

import java.io.File;
import java.util.Map;

public class FileCommandHandler extends TextCommandUpstreamHandler {

    @Override
    protected Map<String, EachCommandHandler> getCommands() {
        return Map.of(
                "photo", this::sendPhoto,
                "document", this::sendDocument,
                "video", this::sendVideo
        );
    }

    private void sendPhoto(RequestContext ctx, String[] args) {
        File file = new File("path/to/photo.jpg");

        ctx.getResponder().send(
                "📷 Вот ваша фотография!",
                file,
                SmartResponder.MediaType.Photo
        );
    }

    private void sendDocument(RequestContext ctx, String[] args) {
        File file = new File("path/to/document.pdf");

        ctx.getResponder().send(
                "📄 Вот ваш документ!",
                file,
                SmartResponder.MediaType.Document
        );
    }

    private void sendVideo(RequestContext ctx, String[] args) {
        File file = new File("path/to/video.mp4");

        ctx.getResponder().send(
                "🎥 Вот ваше видео!",
                file,
                SmartResponder.MediaType.Video
        );
    }
}
```

### 5. Базовый диалог

Комплексный пример многошагового диалога с использованием Builder паттерна.

```java
import ru.daniil4jk.strongram.core.bot.ChainedBot;
import ru.daniil4jk.strongram.core.chain.configurator.OrderConfigurator;
import ru.daniil4jk.strongram.core.upstream.UpstreamHandler;
import ru.daniil4jk.strongram.core.upstream.preinstalled.DialogUpstreamHandler;

public class PizzaBot extends ChainedBot {

    public PizzaBot(String username) {
        super(username);
    }

    @Override
    protected void configureUpstream(OrderConfigurator<UpstreamHandler> chain) {
        chain.add(new PizzaOrderCommandHandler());
        chain.add(new DialogUpstreamHandler());
    }
}
```

```java
import ru.daniil4jk.strongram.core.command.EachCommandHandler;
import ru.daniil4jk.strongram.core.context.request.RequestContext;
import ru.daniil4jk.strongram.core.dialog.DialogImpl;
import ru.daniil4jk.strongram.core.dialog.part.BuildableDialogPart;
import ru.daniil4jk.strongram.core.filter.Filters;
import ru.daniil4jk.strongram.core.upstream.preinstalled.DialogUpstreamHandler;
import ru.daniil4jk.strongram.core.upstream.preinstalled.TextCommandUpstreamHandler;
import ru.daniil4jk.strongram.core.unboxer.As;

import java.util.Map;

public class PizzaOrderCommandHandler extends TextCommandUpstreamHandler {

    @Override
    protected Map<String, EachCommandHandler> getCommands() {
        return Map.of("order", this::startOrder);
    }

    private void startOrder(RequestContext ctx, String[] args) {
        DialogImpl<PizzaState> dialog = DialogImpl.<PizzaState>builder()
                .initState(PizzaState.ASK_NAME)
                .part(PizzaState.ASK_NAME, BuildableDialogPart.<PizzaState>builder()
                        .filter(Filters.hasMessageText())
                        .firstNotification((context, storage) -> {
                            context.getResponder().send("Введите название пиццы");
                        })
                        .repeatNotification((context, storage) -> {
                            context.getResponder().send("Вы всё еще в диалоге, введите название пиццы");
                        })
                        .handler((context, dCtx) -> {
                            String pizzaName = context.getRequest(As.messageText());
                            dCtx.getDialogScopeStorage().set("pizzaName", pizzaName);
                            dCtx.setState(PizzaState.ASK_ADDRESS);
                        })
                        .build())
                .part(PizzaState.ASK_ADDRESS, BuildableDialogPart.<PizzaState>builder()
                        .filter(Filters.hasMessageText())
                        .firstNotification((context, storage) -> {
                            context.getResponder().send("Теперь укажите адрес доставки");
                        })
                        .repeatNotification((context, storage) -> {
                            context.getResponder().send("Вы всё еще в диалоге, укажите адрес доставки");
                        })
                        .handler((context, dCtx) -> {
                            String address = context.getRequest(As.messageText());
                            String pizzaName = dCtx.getDialogScopeStorage().get("pizzaName");

                            context.getResponder().send("""
                                    Заказ принят!
                                    
                                    Пицца: %s
                                    Адрес: %s
                                    """.formatted(pizzaName, address));

                            dCtx.stop();
                        })
                        .build())
                .build();

        ctx.getRequestScopeStorage().add(DialogUpstreamHandler.DIALOGS_CONTEXT_FIELD_NAME, dialog);
    }

    public enum PizzaState {
        ASK_NAME,
        ASK_ADDRESS
    }
}
```

### 6. Dialog со сложной логикой (ExtendableDialogPart)

Расширяем предыдущий пример системой оплаты.
Для начала скопируем класс `PizzaOrderCommandHandler`, добавив в него один шаг: PaymentDialogPart

```java
import ru.daniil4jk.strongram.core.command.EachCommandHandler;
import ru.daniil4jk.strongram.core.context.request.RequestContext;
import ru.daniil4jk.strongram.core.dialog.DialogImpl;
import ru.daniil4jk.strongram.core.dialog.part.BuildableDialogPart;
import ru.daniil4jk.strongram.core.filter.Filters;
import ru.daniil4jk.strongram.core.upstream.preinstalled.DialogUpstreamHandler;
import ru.daniil4jk.strongram.core.upstream.preinstalled.TextCommandUpstreamHandler;
import ru.daniil4jk.strongram.core.unboxer.As;

import java.util.Map;

public class PizzaOrderCommandHandler extends TextCommandUpstreamHandler {

    @Override
    protected Map<String, EachCommandHandler> getCommands() {
        return Map.of("order", this::startOrder);
    }

    private void startOrder(RequestContext ctx, String[] args) {
        DialogImpl<PizzaState> dialog = DialogImpl.<PizzaState>builder()
                .initState(PizzaState.ASK_NAME)
                .part(PizzaState.ASK_NAME, BuildableDialogPart.<PizzaState>builder()
                        .filter(Filters.hasMessageText())
                        .firstNotification((context, storage) -> {
                            context.getResponder().send("Введите название пиццы");
                        })
                        .repeatNotification((context, storage) -> {
                            context.getResponder().send("Вы всё еще в диалоге, введите название пиццы");
                        })
                        .handler((context, dCtx) -> {
                            String pizzaName = context.getRequest(As.messageText());
                            dCtx.getDialogScopeStorage().set("pizzaName", pizzaName);
                            dCtx.setState(PizzaState.ASK_ADDRESS);
                        })
                        .build())
                .part(PizzaState.ASK_ADDRESS, BuildableDialogPart.<PizzaState>builder()
                        .filter(Filters.hasMessageText())
                        .firstNotification((context, storage) -> {
                            context.getResponder().send("Теперь укажите адрес доставки");
                        })
                        .repeatNotification((context, storage) -> {
                            context.getResponder().send("Вы всё еще в диалоге, укажите адрес доставки");
                        })
                        .handler((context, dCtx) -> {
                            String address = context.getRequest(As.messageText());
                            dCtx.getDialogScopeStorage().set("address", address);
                            dCtx.setState(PizzaState.PAYMENT);
                        })
                        .build())
                .part(PizzaState.PAYMENT, new PaymentDialogPart())
                .build();

        ctx.getRequestScopeStorage().add(DialogUpstreamHandler.DIALOGS_CONTEXT_FIELD_NAME, dialog);
    }

    public enum PizzaState {
        ASK_NAME,
        ASK_ADDRESS,
        PAYMENT
    }
}
```

А теперь добавим объемную логику оплаты используя DialogPart реализуемый через наследование

```java
import ru.daniil4jk.strongram.core.context.dialog.DialogContext;
import ru.daniil4jk.strongram.core.context.request.RequestContext;
import ru.daniil4jk.strongram.core.context.storage.Storage;
import ru.daniil4jk.strongram.core.dialog.part.ExtendableDialogPart;
import ru.daniil4jk.strongram.core.filter.Filter;
import ru.daniil4jk.strongram.core.filter.Filters;
import ru.daniil4jk.strongram.core.unboxer.As;
import ru.daniil4jk.strongram.core.upstream.preinstalled.PizzaOrderWithPaymentCommandHandler.PizzaState;

public class PaymentDialogPart extends ExtendableDialogPart<PizzaState> {

    private final PaymentService paymentService;

    public PaymentDialogPart() {
        this.paymentService = new PaymentService();
    }

    @Override
    protected Filter getFilter() {
        return Filters.hasMessageText().and(
                Filters.iterateOr(
                        Filters.textEqualsIgnoreCase("оплатить"),
                        Filters.textEqualsIgnoreCase("отмена")
                )
        );
    }

    @Override
    protected void firstNotification(RequestContext ctx, Storage storage) {
        String pizzaName = storage.get("pizzaName");
        String address = storage.get("address");

        ctx.getResponder().send("""
                Подтверждение заказа
                Пицца: %s
                Адрес: %s
                Стоимость: 500 руб.
                Отправьте "оплатить" для подтверждения заказа:
                """.formatted(pizzaName, address));
    }

    @Override
    protected void repeatNotification(RequestContext ctx, Storage storage) {
        firstNotification(ctx, storage);
    }

    @Override
    protected void accept(RequestContext ctx, DialogContext<PizzaState> dCtx) {
        String message = ctx.getRequest(As.messageText()).toLowerCase();

        if (message.contains("отмена")) {
            ctx.getResponder().send("Заказ отменен. Используйте /order для нового заказа.");
            dCtx.stop();
            return;
        }


        String pizzaName = dCtx.getDialogScopeStorage().get("pizzaName");
        String address = dCtx.getDialogScopeStorage().get("address");
        Long userId = ctx.getUUID().getReplyChatId();

        try {
            // Обработка оплаты через сервис
            paymentService.processPayment(userId, 500);

            ctx.getResponder().send("""
                    Оплата прошла успешно!
                    Пицца: %s
                    Адрес: %s
                    Ожидайте доставку
                    """.formatted(pizzaName, address)
            );

            dCtx.stop();
        } catch (PaymentException e) {
            ctx.getResponder().send("Ошибка оплаты %s! Попробуйте еще раз или напишите \"отмена\" для отмены заказа.".formatted(e.getMessage()));
        }
    }
}
```

```java
public interface PaymentService {
    
    /**
     * Имитация обработки платежа
     * В реальном приложении здесь будет интеграция с платежной системой
     */
    void processPayment(Long userId, int amount) throws PaymentException;
}
```

### 7. Регистрация бота как Long Polling

Запуск бота с использованием Long Polling для получения обновлений.

```java
import ru.daniil4jk.strongram.longpolling.LongPollingBotApplication;
import ru.daniil4jk.strongram.longpolling.adapter.LongPollingBotAdapter;

public class Main {
    
    public static void main(String[] args) {
        try {
            LongPollingBotApplication application = new LongPollingBotApplication();
            
            application.registerBot(new LongPollingBotAdapter(
                "YOUR_BOT_TOKEN",
                new EchoBot("YOUR_BOT_USERNAME")
            ));
        } catch (Exception e) {
            System.err.println("❌ Failed to start bot: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

Пример с несколькими ботами:

```java
import ru.daniil4jk.strongram.longpolling.LongPollingBotApplication;
import ru.daniil4jk.strongram.longpolling.adapter.LongPollingBotAdapter;

public class Main {
    
    public static void main(String[] args) {
        try {
            LongPollingBotApplication application = new LongPollingBotApplication();
            
            application.registerBot(new LongPollingBotAdapter(
                "YOUR_FIRST_BOT_TOKEN",
                new EchoBot("YOUR_FIRST_BOT_USERNAME")
            ));
            
            application.registerBot(new LongPollingBotAdapter(
                "YOUR_SECOND_BOT_TOKEN",
                new CommandBot("YOUR_SECOND_BOT_USERNAME")
            ));
            
            application.registerBot(new LongPollingBotAdapter(
                "YOUR_THIRD_BOT_TOKEN",
                new FileBot("YOUR_THIRD_BOT_USERNAME")
            ));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### 8. Регистрация бота как Webhook

Запуск бота с использованием Webhook для получения обновлений.

```java
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.daniil4jk.strongram.webhook.WebhookBotApplication;
import ru.daniil4jk.strongram.webhook.adapter.WebhookBotAdapter;

public class Main {
    
    public static void main(String[] args) {
        try {
            WebhookBotApplication application = new WebhookBotApplication();
            
            application.registerBot(
                new WebhookBotAdapter(
                    new URI("https://yourdomain.com/webhook/bot").toURL(),
                    "YOUR_BOT_TOKEN",
                    new EchoBot("YOUR_BOT_USERNAME")
                )
            );
        } catch (Exception e) {
            System.err.println("Failed to start bot: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

## 📤 SmartResponderFactory — отправка сообщений любому пользователю

До сих пор мы использовали `getResponder()` из контекста сообщения. Это работает, когда бот отвечает юзеру, который написал ему первым. Но у такого `Responder`'а есть ограничение — **он живёт только 1 запрос**.

А что, если нужно отправить сообщение из совсем другого места?
- Пользователь оформил заказ на сайте → бот отправляет уведомление
- Прошёл час → бот напоминает о забытой корзине
- Админ нажал кнопку "разослать всем"

Для таких случаев используйте `getResponderFactory()`.

### 🤔 А в чём разница?

| | `getResponder()` | `getResponderFactory()` |
|---|---|---|
| **Время жизни** | 1 запрос | Пока жив бот |
| **Откуда chatId** | Из входящего сообщения | Вы указываете |
| **Когда использовать** | Ответ юзеру | Уведомления, рассылки |

### Где взять фабрику?

**В обработчике** — через контекст:
```java
SmartResponderFactory factory = ctx.getBotResponderFactory();
```

**В любом сервисе** — из вашего контекста:
```java
@Autowired
private SmartResponderFactory responderFactory;
```

Чтобы положить SmartResponderFactory в ваш контекст, вы можете получить её из объекта бота
```java
@Bean
SmartResponderFactory responderFactory(YourBot bot) {
    return new SmartResponderFactoryImpl(bot.getResponderFactory());
}
```

### Пример: сервис уведомлений

Допустим, у вас есть сервис заказов, который должен слать уведомления:

```java
import ru.daniil4jk.strongram.core.context.request.TelegramUUID;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;

@Service
public class OrderNotificationService {

    @Autowired
    private SmartResponderFactory responderFactory;

    public void notifyStatus(Chat chat, String status) {
        TelegramUUID uuid = new TelegramUUID(chat);
        responderFactory.createSmart(uuid, null).send("📦 Статус: " + status);
    }
}
```

### Рассылки

Фабрика отлично подходит для массовых рассылок:

```java
public void broadcast(String message) {
    for (Chat chat : userRepository.getAllChats()) {
        TelegramUUID uuid = new TelegramUUID(chat);
        responderFactory.createSmart(uuid, null).send(message);
    }
}
```

### Другие кейсы

- **Напоминания** — через_scheduler (Spring Scheduler, Quartz)_
- **Inline-кнопки** — создаёте `SmartResponder` и передаёте `InlineKeyboardMarkup`
- **Редактирование сообщений** — используйте `responder.send(EditMessageText...)` или напрямую через `Responder.send(Method)`

## 🤝 Вклад в проект

Мы рады вашему участию в развитии Strongram! Вот как вы можете помочь:

### Как внести изменения

1. **Fork проекта** - Создайте свою копию репозитория
2. **Создайте ветку** - `git checkout -b feature/AmazingFeature`
3. **Внесите изменения** - Реализуйте свою функциональность
4. **Закоммитьте** - `git commit -m 'Add some AmazingFeature'`
5. **Push в ветку** - `git push origin feature/AmazingFeature`
6. **Откройте Pull Request** - Опишите свои изменения

### Правила участия

- Следуйте существующему стилю кода
- Добавляйте тесты для новой функциональности
- Обновляйте документацию при необходимости
- Пишите понятные commit сообщения
- Один Pull Request = одна функция/исправление

### Что можно улучшить

- 📝 Документация и примеры
- 🐛 Исправление ошибок
- ✨ Новые функции
- 🎨 Улучшение API
- 🔧 Оптимизация производительности
- 🧪 Написание тестов

### Сообщения об ошибках

При создании issue укажите:
- Версию Strongram
- Версию Java
- Описание проблемы
- Шаги для воспроизведения
- Ожидаемое поведение
- Фактическое поведение

## Благодарности:

- **[TelegramBots Java Library](https://github.com/rubenlagus/TelegramBots)** - отличная базовая библиотека для работы с Telegram Bot API от [@rubenlagus](https://github.com/rubenlagus)
- Все [контрибьюторы](https://github.com/weavedwires/strongram/graphs/contributors) проекта

Спасибо всем, кто использует Strongram и делится обратной связью!

---

<div align="center">

⭐ **Если вам нравится Strongram, поставьте звезду на GitHub!** ⭐

Made with ❤️ by [Weavedwires](https://github.com/weavedwires)

</div>
