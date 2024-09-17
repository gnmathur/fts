package dev.gnmathur.filters;

import org.tartarus.snowball.ext.EnglishStemmer;

import java.util.Arrays;

public class StemmerFilter implements Filter {
    @Override
    public String[] filter(String[] text) {
        EnglishStemmer stemmer = new EnglishStemmer();
        return Arrays.stream(text)
                .map(word -> {
                    stemmer.setCurrent(word);
                    stemmer.stem();
                    return stemmer.getCurrent();
                })
                .toArray(String[]::new);
    }
}
