package ru.daniil4jk.strongram.core.util.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
class Splitter {
    public static final String WORD_SEPARATOR = " ";
    public static final int MAX_MSG_SIZE = 4096;

    private final String originalMessage;

    @Getter
    private List<String> splat;
    private StringBuilder currentMessage = new StringBuilder();

    public void split() {
        if (originalMessage.length() <= MAX_MSG_SIZE) {
            splat = List.of(originalMessage);
            return;
        }

        String[] words = originalMessage.split(WORD_SEPARATOR);

        splat = new ArrayList<>();
        for (String word : words) {
            if (word.length() > MAX_MSG_SIZE) {
                for (int i = 0; i < word.length(); i += MAX_MSG_SIZE) {
                    if (!currentMessage.isEmpty()) {
                        exchangeBuilders();
                    }
                    currentMessage.append(
                            word,
                            i,
                            Math.min(word.length(), i + MAX_MSG_SIZE)
                    );
                }
            } else {
                if (currentMessage.length() + word.length() + WORD_SEPARATOR.length() >= MAX_MSG_SIZE) {
                    exchangeBuilders();
                }
                currentMessage.append(WORD_SEPARATOR).append(word);
            }
        }
        exchangeBuilders();
    }

    private void exchangeBuilders() {
        splat.add(currentMessage.toString().trim());
        currentMessage = new StringBuilder();
    }
}
