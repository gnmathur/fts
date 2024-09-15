package dev.gnmathur;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestTokenizer {
    @Test
    public void testTokenizer() {
        String text1 = "This is a test. 1, 2, 3. Testing, testing. 1, 2, 3.";
        // Tokenize and compare
        String[] tokens = Tokenizer.tokenize(text1);
        // Test all tokens in one go
        assertThat(tokens).containsExactly("This", "is", "a", "test", "1", "2", "3", "Testing", "testing", "1", "2", "3");

        String text2 = "The quick brown fox jumps over the lazy dog. 1234567890";
        // Tokenize and compare
        String[] tokens2 = Tokenizer.tokenize(text2);
        // Test all tokens in one go
        assertThat(tokens2).containsExactly("The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog", "1234567890");

        String text3 = "测试中文 characters 日本語 mixed with English";
        // Tokenize and compare
        String[] tokens3 = Tokenizer.tokenize(text3);
        // Test all tokens in one go
        assertThat(tokens3).containsExactly("测试中文", "characters", "日本語", "mixed", "with", "English");

        String text4 = "Special characters: !@#$%^&*()_+=<>?\"':;{}[]|\\~`";
        String[] tokens4 = Tokenizer.tokenize(text4);
        assertThat(tokens4).containsExactly("Special", "characters");

        String text5 = "Well-known phrases like state-of-the-art.";
        String[] tokens5 = Tokenizer.tokenize(text5);
        assertThat(tokens5).containsExactly("Well", "known", "phrases", "like", "state", "of", "the", "art");

        String text6 = "https://www.google.com/search?q=tokenizer";
        String[] tokens6 = Tokenizer.tokenize(text6);
        assertThat(tokens6).containsExactly("https", "www", "google", "com", "search", "q", "tokenizer");

        String text7 = "Contact us at email@example.com or visit http://example.com\"";
        String[] tokens7 = Tokenizer.tokenize(text7);
        assertThat(tokens7).containsExactly("Contact", "us", "at", "email", "example", "com", "or", "visit", "http", "example", "com");
    }
}
