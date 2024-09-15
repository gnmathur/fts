package dev.gnmathur;

public record WikiEntry(String title, String url, String abstractText, Long id) {
    @Override
    public String toString() {
        return "WikiEntry{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", abstractText='" + abstractText + '\'' +
                ", id=" + id +
                '}';
    }
}

