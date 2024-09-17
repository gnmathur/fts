package dev.gnmathur;

import dev.gnmathur.docparsers.WikipediaDocumentParser;
import dev.gnmathur.fe.Frontend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

public class FTS {
    private static final InvertedIndex index = new InvertedIndex();
    private static final Logger logger = Logger.getLogger(FTS.class.getName());

    public static record Tuple(String title, String abstractText) {
    }

    public static List<Tuple> search(String text) {
        var start = System.nanoTime();

        var searchResult = index.searchIndex(text);

        List<Tuple> results = new ArrayList<>();

        var longIterator = searchResult.getLongIterator();
        while (longIterator.hasNext()) {
            long docId = longIterator.next();
            results.add(new Tuple(index.getTitle(docId), index.getAbstract(docId)));
        }

        return results;
    }

    public static void main(String[] args) throws Exception {
        final String fileURI = "src/main/resources/enwiki-latest-abstract1.xml.gz";

        Long start = System.currentTimeMillis();
        InputStream is = new GZIPInputStream(new FileInputStream(fileURI));
        logger.info("Loading time: " + (System.currentTimeMillis() - start) + "ms");

        // Step 2: Parse the XML content and get a list of WikiEntry objects
        start = System.currentTimeMillis();
        List<WikiEntry> entries = WikipediaDocumentParser.parse(is);
        logger.info("Parsing time: " + (System.currentTimeMillis() - start) + "ms");

        start = System.currentTimeMillis();
        for (WikiEntry entry : entries) {
            var result = Analyzer.analyze(entry.abstractText());
            index.addToIndex(result, entry.id(), entry.title(), entry.abstractText());
        }
        logger.info("Indexing time: " + (System.currentTimeMillis() - start) + "ms");
        logger.info("Index size: " + index.size());

        Frontend.frontend(args);
    }
}
