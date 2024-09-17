package dev.gnmathur;

import org.roaringbitmap.longlong.Roaring64Bitmap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class InvertedIndex {
    private final ConcurrentHashMap<String, Roaring64Bitmap> index = new ConcurrentHashMap<>();
    private final HashMap<Long, String> idToTitle = new HashMap<>();
    private final HashMap<Long, String> idToAbstract = new HashMap<>();

    public void addToIndex(String[] tokens, long docId, String title, String abstractText) {
        int docIdInt = (int) docId;
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
                    result = new Roaring64Bitmap();
                    result.or(bitmap);
                } else {
                    result.and(bitmap);
                }
            }
        }

        return result;
    }

    public String getTitle(long docId) {
        return idToTitle.get(docId);
    }
    public String getAbstract(long docId) {
        return idToAbstract.get(docId);
    }

    public int size() {
        return index.size();
    }
}
