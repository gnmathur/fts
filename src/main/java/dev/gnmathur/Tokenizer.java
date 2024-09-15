package dev.gnmathur;

import java.util.regex.Pattern;

public class Tokenizer {
    public static String[] tokenize (String text) {
        Pattern pattern = Pattern.compile("[^\\p{L}\\p{Nd}]+");
        return pattern.splitAsStream(text)
                .filter(token -> !token.isEmpty()) // Filter out empty strings
                .toArray(String[]::new);
    }

}
