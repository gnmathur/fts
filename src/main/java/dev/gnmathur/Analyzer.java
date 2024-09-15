package dev.gnmathur;

import dev.gnmathur.filters.LowerCaseFilter;
import dev.gnmathur.filters.StemmerFilter;
import dev.gnmathur.filters.StopWordFilter;

public class Analyzer {
    public static String[] analyze(final String entry) {
        String[] tokens = Tokenizer.tokenize(entry);

        var lowerCaseFiltered = new LowerCaseFilter().filter(tokens);
        var stopWordFiltered= new StopWordFilter().filter(lowerCaseFiltered);
        var stemmed = new StemmerFilter().filter(stopWordFiltered);

        return stemmed;
    }
}
