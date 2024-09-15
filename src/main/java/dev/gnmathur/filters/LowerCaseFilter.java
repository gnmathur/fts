package dev.gnmathur.filters;

public class LowerCaseFilter implements Filter {
    @Override
    public String[] filter(String[] text) {
        // Convert the text to lower case
        for (int i = 0; i < text.length; i++) {
            text[i] = text[i].toLowerCase();
        }
        return text;
    }
}
