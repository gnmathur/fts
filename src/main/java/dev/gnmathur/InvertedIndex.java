package dev.gnmathur;

import org.roaringbitmap.longlong.Roaring64Bitmap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class InvertedIndex {
    // Using RoaringBitmap for more efficient bitmaps instead of HashSet<Long>
    private final ConcurrentHashMap<String, Roaring64Bitmap> index = new ConcurrentHashMap<>();
    private final HashMap<Long, String> idToTitle = new HashMap<>();
    private final HashMap<Long, String> idToAbstract = new HashMap<>();

    // Add tokens, title, and abstract to the index
    public void addToIndex(String[] tokens, long docId, String title, String abstractText) {
        int docIdInt = (int) docId;  // RoaringBitmap uses int values
        Arrays.stream(tokens).forEach(token ->
                index.computeIfAbsent(token, k -> new Roaring64Bitmap()).add(docIdInt)
        );
        idToTitle.put(docId, title);
        idToAbstract.put(docId, abstractText);
    }

    public Roaring64Bitmap searchIndex(String searchString) {
        String[] searchTokens = Analyzer.analyze(searchString);

        if (searchTokens.length == 0) {
            return new Roaring64Bitmap();  // Return early if no tokens
        }

        return Arrays.stream(searchTokens)
                .map(index::get)
                .filter(Objects::nonNull)  // Avoid null bitmaps
                .reduce((bitmap1, bitmap2) -> {
                    bitmap1.and(bitmap2);  // Modify bitmap1 in place
                    return bitmap1;         // Return bitmap1 for further reduction
                })
                .orElse(new Roaring64Bitmap());  // Return empty bitmap if no results
    }

    // Get title by document ID
    public String getTitle(long docId) {
        return idToTitle.get(docId);
    }

    // Get abstract by document ID
    public String getAbstract(long docId) {
        return idToAbstract.get(docId);
    }

    // Get the size of the index
    public int size() {
        return index.size();
    }
}
