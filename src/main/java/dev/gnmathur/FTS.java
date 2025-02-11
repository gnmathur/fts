package dev.gnmathur;

import dev.gnmathur.docparsers.WikipediaDocumentParser;
import dev.gnmathur.fe.Frontend;
import dev.gnmathur.utils.Timer;
import org.rocksdb.RocksDBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class FTS {
    private static InvertedIndex index;
    private static final String ROCKS_DIR = "rocksdb_fts";
    private static final String RESOURCE_PATH = "src/main/resources/enwiki-latest-abstract.xml.gz";
    private static final Logger logger = LoggerFactory.getLogger(FTS.class);

    public static List<Tuple> search(String text) {
        return Timer.time("Search operation", () -> {
            var searchResult = index.searchIndex(text);
            List<Tuple> results = new ArrayList<>();

            var longIterator = searchResult.getLongIterator();
            while (longIterator.hasNext()) {
                long docId = longIterator.next();
                results.add(new Tuple(index.getTitle(docId), index.getAbstract(docId)));
            }

            return results;
        }, logger);
    }

    public static void main(String[] args) throws Exception {
        final boolean rebuild = args.length > 0 && args[0].equals("--rebuild");

        if (rebuild) {
            logger.info("Rebuilding index...");
            index = new InvertedIndex();

            InputStream is = Timer.time("Resource reading",
                    () -> new GZIPInputStream(new FileInputStream(RESOURCE_PATH)),
                    logger);

            List<Document> entries = Timer.time("Wikipedia parsing",
                    () -> WikipediaDocumentParser.parse(is),
                    logger);

            Timer.time("Indexing entries", () -> {
                entries.forEach(entry -> index.addToIndex(entry));
                return null;
            }, logger);

            logger.info("Index size: {}", index.size());

            Timer.time("Saving index",
                    () -> {
                        try { InvertedIndexPersistence.commitToDisk(index, ROCKS_DIR); }
                        catch (RocksDBException | IOException e) { throw new RuntimeException(e); }
                        return null;
                    },
                    logger);
        }

        logger.info("Loading index from disk...");
        try {
            index = Timer.time("Loading index",
                    () -> InvertedIndexPersistence.loadFromDisk(ROCKS_DIR),
                    logger);
        } catch (RuntimeException e) {
            logger.error("Error loading index from disk. Try rebuilding the index and see usage below.");
            logger.error("Usage: mvn compile exec:java  -Dexec.mainClass=\"dev.gnmathur.FTS\" -Dexec.args=\"--rebuild\"");
            System.exit(1);
        }

        Frontend.frontend(args);
    }
}
