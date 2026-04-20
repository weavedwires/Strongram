# Strongram AGENTS.md

## Build Commands

- `./mvnw compile` - compile all modules
- `./mvnw test` - run tests (none currently)
- `./mvnw package` - build JARs
- `./mvnw install` - install to local maven repo

## Project Structure

```
strongram-core/          - Main framework (handlers, dialogs, chains, filters)
  src/main/java/ru/daniil4jk/strongram/core/
    bot/                 - Bot base classes (BaseBot, ChainedBot, UpdateProcessor)
    chain/               - Chain pattern (handler linking via NextConsumer)
    command/             - Command handler interface (EachCommandHandler)
    context/             - Request/Dialog context, storage, TelegramUUID
    dialog/              - Dialog system (DialogImpl, DialogPart, DialogRepository)
    downstream/          - Downstream handlers (response processing)
    filter/              - Filter system (Filter interface, Filters.java)
    keyboard/            - Inline/Reply keyboards
    response/            - Response sending (Sender, SmartResponder, SmartResponderFactory)
    unboxer/             - Data extraction from Update (As.java with 80+ extractors)
    upstream/            - Upstream handlers (request processing)
    report/exception/    - Exception handling formatters
strongram-longpolling/   - Long polling adapter (LongPollingBotAdapter)
strongram-webhook/       - Webhook adapter (WebhookBotAdapter)
```

## Tech Stack

- Java 17+, Maven 3.6+
- TelegramBots v9.5.0 (Telegram API)
- Lombok (annotation processing)
- Log4j2 (logging)
- JUnit 5 + Mockito + AssertJ (testing deps available in core)

## Architecture Overview

### Bot Types
- `BaseBot` - abstract base with `SmartResponderFactory`, username handling
- `ChainedBot` - extends BaseBot, implements Chain of Responsibility pattern

### Handler Hierarchy
```
UpstreamHandler (interface)
  └── BaseUpstreamHandler (implements chain via setNext())
        └── FilteredUpstreamHandler (adds Filter support)
              ├── TextCommandUpstreamHandler (command routing)
              └── DialogUpstreamHandler (dialog processing)

DownstreamHandler (interface)
  └── BaseDownstreamHandler
```

### Chain Pattern
- Handlers linked via `setNext()`
- Built by `Chain.build()` from factory
- Request flows through chain: each handler calls `processNext(ctx)` to continue

### Dialog System
- `DialogImpl<T>` - state machine with enum states
- `DialogPart` - single step in dialog (builder: `BuildableDialogPart` or custom: `ExtendableDialogPart`)
- `DialogContext` - manages state transitions, storage
- Dialogs stored in `DialogRepository` (default: `InMemoryDialogRepository`)

### Response System
- `SmartResponder` - high-level API for sending messages
- `SmartResponderFactory` - long-lived factory for notifications/broadcasts
- `RequestContext.getResponder()` - per-request responder
- `RequestContext.getBotResponderFactory()` - long-lived, for any user

### Data Extraction (Unboxer)
- `As.*` - static factory for extracting Telegram objects from Update
- Examples: `As.message()`, `As.text()`, `As.callbackQueryData()`, `As.photo()`, etc.

### Filters
- `Filter` extends `Predicate<RequestContext>` with and/or/negate
- `Filters` - utility with 80+ built-in filters
- Can check: message content, chat type, user ID, text content, etc.

## Entry Points

### Long Polling
```java
LongPollingBotApplication app = new LongPollingBotApplication();
app.registerBot(new LongPollingBotAdapter("TOKEN", new YourBot()));
```

### Webhook
```java
WebhookBotApplication app = new WebhookBotApplication();
app.registerBot(new WebhookBotAdapter(new URL("https://..."), "TOKEN", new YourBot()));
```

## Creating Handlers

### Upstream Handler (processes incoming updates)
```java
// Simple handler
public class MyHandler extends BaseUpstreamHandler {
    protected void process(RequestContext ctx) {
        ctx.getResponder().send("Hello!");
        processNext(ctx); // continue chain
    }
}

// Filtered handler
public class MyFilteredHandler extends FilteredUpstreamHandler {
    protected Filter getFilter() {
        return Filters.hasMessageText();
    }
    protected void processFiltered(RequestContext ctx) {
        String text = ctx.getRequest(As.text());
        ctx.getResponder().send("You said: " + text);
    }
}
```

### Command Handler
```java
public class MyCommands extends TextCommandUpstreamHandler {
    protected Map<String, EachCommandHandler> getCommands() {
        return Map.of("start", this::onStart, "help", this::onHelp);
    }
    private void onStart(RequestContext ctx, String[] args) {
        ctx.getResponder().send("Started!");
    }
}
```