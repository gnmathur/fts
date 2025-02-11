package dev.gnmathur;

import org.roaringbitmap.longlong.Roaring64Bitmap;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class InvertedIndexPersistence {
    static {
        RocksDB.loadLibrary();
    }

    private static byte[] serialize(final Object obj) throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(obj);
            return os.toByteArray();
        }
    }

    public static void commitToDisk(final InvertedIndex index, final String dbPath) throws RocksDBException, IOException {
        final Options options = new Options().setCreateIfMissing(true);
        final RocksDB db = RocksDB.open(options, dbPath);

        for (String token : index.index.keySet()) {
            db.put(("index_" + token).getBytes(), serialize(index.index.get(token)));
        }

        for (Long docId : index.documents.keySet()) {
            db.put(("doc_" + docId).getBytes(), serialize(index.documents.get(docId)));
        }

        db.close();
    }

    private static Roaring64Bitmap deserializeRoaringBitmap(byte[] data) throws ClassNotFoundException, IOException {
        try(ByteArrayInputStream is = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(is)) {
            return (Roaring64Bitmap) ois.readObject();
        }
    }

    private static Document deserializeDocument(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream is = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(is)) {
            return (Document) ois.readObject();
        }
    }

    public static InvertedIndex loadFromDisk(String dbPath) throws RocksDBException, IOException, ClassNotFoundException {
        try(final Options options = new Options();
            final RocksDB db = RocksDB.open(options, dbPath)) {

            final ConcurrentHashMap<String, Roaring64Bitmap> index = new ConcurrentHashMap<>();
            final HashMap<Long, Document> documents = new HashMap<>();

            try (RocksIterator iterator = db.newIterator()) {
                iterator.seekToFirst();
                while (iterator.isValid()) {
                    String key = new String(iterator.key());
                    byte[] value = iterator.value();
                    if (key.startsWith("index_")) {
                        index.put(key.substring(6), deserializeRoaringBitmap(value));
                    } else if (key.startsWith("doc_")) {
                        long docId = Long.parseLong(key.substring(4));
                        documents.put(docId, deserializeDocument(value));
                    }
                    iterator.next();
                }
            }

            return new InvertedIndex(index, documents);
        }
    }
}
