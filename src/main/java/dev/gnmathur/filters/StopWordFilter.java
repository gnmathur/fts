package dev.gnmathur.filters;

import java.util.HashSet;
import java.util.Set;

public class StopWordFilter implements Filter {
    // Initialize hash set with stop words
    private final HashSet<String> stopWords =
        new HashSet<>(Set.of("a", "an", "and", "are", "as", "at", "be", "by", "for", "from", "has", "he", "in", "is",
                "it", "its", "of", "on", "that", "the", "to", "was", "were", "will", "with"));

    @Override
    public String[] filter(String[] text) {
        // Remove stop words from the text
        return java.util.Arrays.stream(text).filter(word -> !stopWords.contains(word)).toArray(String[]::new);
    }
}
