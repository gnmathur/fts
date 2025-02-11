package dev.gnmathur;

import dev.gnmathur.filters.LowerCaseFilter;
import dev.gnmathur.filters.StemmerFilter;
import dev.gnmathur.filters.StopWordFilter;

/** An analyzer that tokenizes, lowercases, removes stop words, and stems text. */
public class Analyzer {
    private static final LowerCaseFilter lowerCaseFilter = new LowerCaseFilter();
    private static final StopWordFilter stopWordFilter = new StopWordFilter();
    private static final StemmerFilter stemmerFilter = new StemmerFilter();

    public static String[] analyze(final String entry) {
        String[] tokens = Tokenizer.tokenize(entry);

        final var lowerCaseFiltered = lowerCaseFilter.filter(tokens);
        final var stopWordFiltered= stopWordFilter.filter(lowerCaseFiltered);
        final var stemmed = stemmerFilter.filter(stopWordFiltered);

        return stemmed;
    }
}
