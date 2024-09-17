package dev.gnmathur.filters;

import java.util.Arrays;

public class LowerCaseFilter implements Filter {
    @Override
    public String[] filter(String[] text) {
        return Arrays.stream(text)
                .map(String::toLowerCase)
                .toArray(String[]::new);
    }
}
