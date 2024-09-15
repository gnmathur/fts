package dev.gnmathur;

import dev.gnmathur.filters.Filter;
import dev.gnmathur.filters.StemmerFilter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TestStemmer {
    @Test
    public void testStemmer() {
        String[] text = {"running", "ran", "runs"};
        Filter stemmerFilter = new StemmerFilter();
        text = stemmerFilter.filter(text);
        // Check all
        assertThat(text).containsExactly("run", "ran", "run");

        String[] text2 = {"fishing", "fished", "fishes"};
        text2 = stemmerFilter.filter(text2);
        // Check all
        assertThat(text2).containsExactly("fish", "fish", "fish");

    }
}
