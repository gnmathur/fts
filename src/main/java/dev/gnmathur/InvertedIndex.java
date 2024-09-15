package dev.gnmathur;

import org.roaringbitmap.longlong.Roaring64Bitmap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class InvertedIndex {
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
        Roaring64Bitmap result = null;

        for (String str : searchTokens) {
            Roaring64Bitmap bitmap = index.get(str);

            if (bitmap != null) {
                if (result == null) {
                    // Initialize the result with the first bitmap
                    result = new Roaring64Bitmap();
                    result.or(bitmap);  // Make a copy of the first bitmap
                } else {
                    // Perform intersection with the next bitmap
                    result.and(bitmap);
                }
            }
        }

        return result;  // May return null if no strings had bitmaps
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
