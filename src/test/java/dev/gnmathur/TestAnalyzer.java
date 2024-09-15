package dev.gnmathur;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestAnalyzer {
    @Test
    public void testAnalyzer() {
        String[] r = Analyzer.analyze("A donut on a glass plate. Only the donuts.");
        assertThat(r).containsExactly("donut", "glass", "plate", "onli", "donut" );
    }
}
