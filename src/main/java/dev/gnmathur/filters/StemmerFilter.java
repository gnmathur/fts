package dev.gnmathur.filters;

import org.tartarus.snowball.ext.EnglishStemmer;

import java.util.Arrays;

/** A filter that stems words in the text. For example, "running" becomes "run", "easily" becomes "easili", etc. */
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
