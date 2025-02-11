package dev.gnmathur;

import java.util.regex.Pattern;

/** Tokenizes a string into an array of tokens. For example, the string "Hello, world!" would be tokenized into
 * ["Hello", "world"].
 */
public class Tokenizer {
    public static String[] tokenize (String text) {
        Pattern pattern = Pattern.compile("[^\\p{L}\\p{Nd}]+");
        return pattern.splitAsStream(text)
                .filter(token -> !token.isEmpty()) // Filter out empty strings
                .toArray(String[]::new);
    }

}
