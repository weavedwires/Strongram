# Strongram AGENTS.md

## Build Commands

- `./mvnw compile` - compile all modules
- `./mvnw test` - run tests (none currently)
- `./mvnw package` - build JARs
- `./mvnw install` - install to local maven repo

## Tech Stack

- Java 17+, Maven wrapper (3.9.x embedded)
- TelegramBots v9.2.0
- Lombok 1.18.38 (annotation processing via maven-compiler-plugin in core pom.xml)
- Log4j2 + log4j-slf4j-impl
- JUnit 5, Mockito, AssertJ, Awaitility (declared in core, no tests written yet)

## Module Layout

| Module | Artifact | Depends on | Entrypoint class |
|---|---|---|---|
| `strongram-core` | strongram-core | — | `ChainedBot`, `BaseBot` |
| `strongram-longpolling` | strongram-longpolling | core | `LongPollingBotApplication` |
| `strongram-webhook` | strongram-webhook | core | `WebhookBotApplication` |

## Architecture

### Bot Types
- `Bot` interface → `UpdateProcessor` (`BiConsumer<Update, ResponseSink>`)
- `BaseBot` — abstract, auto-@-prefixes username, owns `SinkResponderFactory`
- `ChainedBot` extends `BaseBot` — Chain of Responsibility with upstream + downstream chains
- Constructor: `ChainedBot(@Nullable String username)` — token goes to adapter, not bot

### Handler Hierarchy
```
UpstreamHandler (interface + NextConsumer)
  └── BaseUpstreamHandler (abstract: process + processNext)
        ├── FilteredUpstreamHandler (filter guard → processFiltered or skip)
        │     ├── TextCommandUpstreamHandler (non-slash text commands)
        │     ├── CommandUpstreamHandler (slash commands, /cmd@bot disambiguation)
        │     └── InteractiveKeyboardUpstreamHandler (inline/reply button routes)
        ├── DialogUpstreamHandler (dialog state machine, extends BaseUpstreamHandler directly)
        ├── ExceptionReportUpstreamHandler (try/catch wrapper, must be first in chain)
        └── CannotProcessUpstreamHandler (terminal fallback, default last in chain)

DownstreamHandler (BiConsumer<Optional<RequestContext>, PartialBotApiMethod>)
  └── BaseDownstreamHandler (abstract)
        └── AddDefaultKeyboardDownstreamHandler (sets ReplyKeyboard on outbound via MethodHandle)
```

### Chain Configuration (NOT what README shows)

Override in your `ChainedBot` subclass:
```java
protected void configureUpstream(OrderConfigurator<UpstreamHandler> chain) {
    chain.add(new ExceptionReportUpstreamHandler());  // first = try/catch wrapper
    chain.add(new MyHandler());
    // CannotProcessUpstreamHandler is auto-added last by default
}

protected void configureDownstream(OrderConfigurator<DownstreamHandler> chain) {
    chain.add(new AddDefaultKeyboardDownstreamHandler(defaultKeyboard));
}
```

- `OrderConfigurator.add()` / `addBefore(Class)` / `addAfter(Class)` — positional insertion
- Default upstream: `CannotProcessUpstreamHandler` (chain never drops silently)
- Default downstream: empty

### Preinstalled Upstream Handlers (`upstream/preinstalled/`)

| Handler | Extends | Purpose |
|---|---|---|
| `ExceptionReportUpstreamHandler` | BaseUpstreamHandler | try/catch around chain, send error to user. Constructor: `new ExceptionReportUpstreamHandler(ExceptionFormatters.debug())` or `new ExceptionReportUpstreamHandler()` (uses `info`) |
| `CannotProcessUpstreamHandler` | BaseUpstreamHandler | Terminal "I can't explain" fallback |
| `CommandUpstreamHandler` | FilteredUpstreamHandler | Slash-command router (`/start`, `/help@bot`) via `Map<String, EachCommandHandler>` |
| `TextCommandUpstreamHandler` | FilteredUpstreamHandler | Non-slash command router (takes first word, lowercased) |
| `DialogUpstreamHandler` | BaseUpstreamHandler | Manages active dialogs from `DialogRepository` |
| `InteractiveKeyboardUpstreamHandler` | FilteredUpstreamHandler | Routes button presses from `InteractiveKeyboardHolder`. Constructor takes `ReplyKeyboardMarkup` or `InlineKeyboardMarkup` |

### Dialog System

- `DialogImpl<T>` — state machine with enum states, Builder pattern
- `BuildableDialogPart` — builder style (filter, firstNotification, repeatNotification, handler)
- `ExtendableDialogPart` — subclass style (override getFilter, firstNotification, repeatNotification, accept)
- Register dialog: `ctx.getRequestScopeStorage().add(DialogUpstreamHandler.DIALOGS_CONTEXT_FIELD_NAME, dialog)`
- Active dialogs stored in `DialogRepository` (default `InMemoryDialogRepository`)

### Response System
- `ctx.getResponder()` — per-request `SmartResponder` (scoped to one update)
- `ctx.getBotResponderFactory()` — long-lived `SmartResponderFactory` for notifications/broadcasts
- `SmartResponderFactory.createSmart(chatId).send(text)` — send to any user
- `SmartResponder` methods: `send(text)`, `send(text, file, MediaType)`, `send(text, keyboard)`, `editMessage(id, text)`, `answerCallbackQuery(text)`
- Long messages auto-split into Telegram-legal chunks

### Data Extraction (Unboxer)
- `As.*` — 83+ static `Unboxer<O>` methods: `As.messageText()`, `As.callbackQueryData()`, `As.photo()`, `As.text()`, etc.
- Usage: `ctx.getRequest(As.messageText())`

### Filters
- `Filter` extends `Predicate<RequestContext>` with `and()`, `or()`, `negate()`, `not()`
- `Filters` — 84+ static factories: `hasMessageText()`, `isPrivateChat()`, `textEqualsIgnoreCase()`, `hasCallbackQuery()`, etc.
- Combinators: `Filters.iterateOr(filter1, filter2, ...)`, `Filters.iterateAnd(...)`

## Creating Handlers

### Simple handler
```java
public class MyHandler extends BaseUpstreamHandler {
    protected void process(RequestContext ctx) {
        ctx.getResponder().send("Hello!");
        processNext(ctx);  // continue chain
    }
}
```

### Filtered handler
```java
public class MyFilteredHandler extends FilteredUpstreamHandler {
    protected Filter getFilter() { return Filters.hasMessageText(); }
    protected void processFiltered(RequestContext ctx) {
        String text = ctx.getRequest(As.messageText());
        ctx.getResponder().send("You said: " + text);
    }
}
```

### Text command handler (no slash)
```java
public class MyCommands extends TextCommandUpstreamHandler {
    protected Map<String, EachCommandHandler> getCommands() {
        return Map.of("start", this::onStart);
    }
    private void onStart(RequestContext ctx, String[] args) {
        ctx.getResponder().send("Started!");
    }
}
```

### Slash command handler
```java
public class MySlashCommands extends CommandUpstreamHandler {
    protected Map<String, EachCommandHandler> getCommands() {
        return Map.of("start", this::onStart);
    }
    private void onStart(RequestContext ctx, String[] args) {
        ctx.getResponder().send("/start received!");
    }
}
```

### Full bot example
```java
public class MyBot extends ChainedBot {
    public MyBot() { super("MyBot"); }
    protected void configureUpstream(OrderConfigurator<UpstreamHandler> chain) {
        chain.add(new ExceptionReportUpstreamHandler(ExceptionFormatters.debug()));
        chain.add(new MyFilteredHandler());
        chain.add(new MyCommands());
    }
}
```

## Entry Points

### Long Polling
```java
LongPollingBotApplication app = new LongPollingBotApplication();
app.registerBot(new LongPollingBotAdapter("TOKEN", new MyBot()));
```
- `LongPollingBotApplication` extends `TelegramBotsLongPollingApplication`
- `LongPollingBotAdapter` implements `HasLongPollingBot` (token + client + consume), processes updates on single-thread executor by default

### Webhook
```java
WebhookBotApplication app = new WebhookBotApplication();
app.registerBot(new WebhookBotAdapter(new URL("https://..."), "TOKEN", new MyBot()));
```
- `WebhookBotApplication` extends `TelegramBotsWebhookApplication`
- `WebhookBotAdapter` implements `TelegramWebhookBot` + `TelegramClientProvider`, sends synchronously
