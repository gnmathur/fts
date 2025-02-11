package dev.gnmathur.filters;

import java.util.Arrays;

/** A filter that converts all text to lower case. */
public class LowerCaseFilter implements Filter {
    @Override
    public String[] filter(String[] text) {
        return Arrays.stream(text)
                .map(String::toLowerCase)
                .toArray(String[]::new);
    }
}
