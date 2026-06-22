package ru.daniil4jk.strongram.core.downstream.preinstalled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.daniil4jk.strongram.core.context.request.RequestContext;
import ru.daniil4jk.strongram.core.downstream.BaseDownstreamHandler;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Downstream handler that sets a default {@link ReplyKeyboard} on outgoing messages
 * if none is already set. Uses {@link java.lang.invoke.MethodHandle} to dynamically
 * discover and invoke get/set methods on any {@link PartialBotApiMethod} subclass.
 */
@Slf4j
@RequiredArgsConstructor
public class AddDefaultKeyboardDownstreamHandler extends BaseDownstreamHandler {
    public static final Class<ReplyKeyboard> REPLY_KEYBOARD_CLASS = ReplyKeyboard.class;
    private static final String GET_METHOD_NAME_PART = "get";
    private static final String SET_METHOD_NAME_PART = "set";

    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();
    private static final Map<Class<?>, Methods> methodsByClass = new HashMap<>();

    private final Function<Optional<RequestContext>, ReplyKeyboard> getDefaultKeyboardFunction;

    public AddDefaultKeyboardDownstreamHandler(ReplyKeyboard defaultKeyboard) {
        this(notUsedContext -> defaultKeyboard);
    }

    @Override
    protected void process(Optional<RequestContext> ctx, PartialBotApiMethod<?> msg) {
        Class<?> key = msg.getClass();

        if (!methodsByClass.containsKey(key)) {
            tryFill(key);
        }

        Methods methods = methodsByClass.get(key);

        if (!methods.invokable) {
            return;
        }

        try {
            if (methods.get.invoke(msg) == null) {
                methods.set.invoke(msg, getDefaultKeyboardFunction.apply(ctx));
            }
        } catch (Throwable e) {
            log.warn("Cannot set default keyboard", e);
        }
    }

    private void tryFill(Class<?> key) {
        Methods methods;
        try {
            methods = new Methods(
                    true,
                    findGetMethod(key),
                    findSetMethod(key)
            );
        } catch (Exception e) {
            methods = new Methods(
                    false,
                    null,
                    null
            );
        }
        methodsByClass.put(key, methods);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private @NotNull MethodHandle findGetMethod(@NotNull Class<?> key) {
        return Arrays.stream(key.getMethods())
                .filter(method -> REPLY_KEYBOARD_CLASS.isAssignableFrom(method.getReturnType()))
                .filter(method -> method.getName().contains(GET_METHOD_NAME_PART))
                .map(Optional::of)
                .map(optional -> {
                    try {
                        return Optional.of(lookup.unreflect(optional.get()));
                    } catch (Exception e) {
                        return Optional.<MethodHandle>empty();
                    }
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny()
                .orElseThrow();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private @NotNull MethodHandle findSetMethod(@NotNull Class<?> key) {
        return Arrays.stream(key.getMethods())
                .filter(method -> method.getParameterCount() == 1)
                .filter(method ->
                        Arrays.stream(method.getParameterTypes())
                                .anyMatch(REPLY_KEYBOARD_CLASS::isAssignableFrom)
                )
                .filter(method -> method.getName().contains(SET_METHOD_NAME_PART))
                .map(Optional::of)
                .map(optional -> {
                    try {
                        return Optional.of(lookup.unreflect(optional.get()));
                    } catch (Exception e) {
                        return Optional.<MethodHandle>empty();
                    }
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny()
                .orElseThrow();
    }

    private record Methods(
            boolean invokable,
            MethodHandle get,
            MethodHandle set
    ) {
    }
}
