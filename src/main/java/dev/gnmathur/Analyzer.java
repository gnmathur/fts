package dev.gnmathur;

import dev.gnmathur.filters.LowerCaseFilter;
import dev.gnmathur.filters.StemmerFilter;
import dev.gnmathur.filters.StopWordFilter;

public class Analyzer {
    private static final LowerCaseFilter lowerCaseFilter = new LowerCaseFilter();
    private static final StopWordFilter stopWordFilter = new StopWordFilter();
    private static final StemmerFilter stemmerFilter = new StemmerFilter();

    public static String[] analyze(final String entry) {
        String[] tokens = Tokenizer.tokenize(entry);

        var lowerCaseFiltered = lowerCaseFilter.filter(tokens);
        var stopWordFiltered= stopWordFilter.filter(lowerCaseFiltered);
        var stemmed = stemmerFilter.filter(stopWordFiltered);

        return stemmed;
    }
}
