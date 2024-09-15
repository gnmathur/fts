package dev.gnmathur.filters;

import org.tartarus.snowball.ext.EnglishStemmer;

public class StemmerFilter implements Filter {
    @Override
    public String[] filter(String[] text) {
        // Stem using Snowball stemmer
        EnglishStemmer stemmer = new EnglishStemmer();
        for (int i = 0; i < text.length; i++) {
            stemmer.setCurrent(text[i]);
            stemmer.stem();
            text[i] = stemmer.getCurrent();
        }
        return text;
    }
}
