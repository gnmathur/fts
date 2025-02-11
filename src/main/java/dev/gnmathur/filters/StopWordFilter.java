package dev.gnmathur.filters;

import java.util.HashSet;
import java.util.Set;

/** A filter that removes stop words from text. For example, "a", "an", "and", etc. */
public class StopWordFilter implements Filter {
    private final HashSet<String> stopWords =
        new HashSet<>(Set.of("a", "an", "and", "are", "as", "at", "be", "by", "for", "from", "has", "he", "in", "is",
                "it", "its", "of", "on", "that", "the", "to", "was", "were", "will", "with"));

    @Override
    public String[] filter(String[] text) {
        return java.util.Arrays.stream(text)
                .filter(word -> !stopWords.contains(word))
                .toArray(String[]::new);
    }
}
