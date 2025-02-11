package dev.gnmathur;

import org.roaringbitmap.longlong.Roaring64Bitmap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/** An in-memory inverted index. */
public class InvertedIndex {
    /** The key data structure for the inverted index - a map from token to a bitmap of document IDs. */
    public final ConcurrentHashMap<String, Roaring64Bitmap> index;
    /** A map from document ID to document. */
    public final HashMap<Long, Document> documents;

    public InvertedIndex(ConcurrentHashMap<String, Roaring64Bitmap> index, HashMap<Long, Document> documents) {
        this.index = index;
        this.documents = documents;
    }

    public InvertedIndex() {
        this.index = new ConcurrentHashMap<>();
        this.documents = new HashMap<>();
    }

    /** Add a document to the index. The document is tokenized and each token is added to the index. */
    public void addToIndex(final Document document) {
        final int docIdInt = (int)document.id();
        final String[] docTokens = Analyzer.analyze(document.abstractText());

        Arrays.stream(docTokens).forEach(token ->
                index.computeIfAbsent(token, k -> new Roaring64Bitmap()).add(docIdInt)
        );
        documents.put(document.id(), document);
    }

    /** Search the index for the given search string. Returns a bitmap of document IDs that contain the search string. */
    public Roaring64Bitmap searchIndex(final String searchString) {
        final String[] searchTokens = Analyzer.analyze(searchString);
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

    public String getTitle(final long docId) { return documents.get(docId).title(); }
    public String getAbstract(final long docId) { return documents.get(docId).abstractText(); }
    public int size() { return index.size(); }
}
