package dev.gnmathur.fe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import dev.gnmathur.FTS;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Frontend {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    // Create logger
    private static final Logger logger = LoggerFactory.getLogger(Frontend.class);

    private static final CacheLoader<String, List<FTS.Tuple>> loader = new CacheLoader<>() {
        @Override
        public List<FTS.Tuple> load(String key) {
            logger.info("Loading cache for query: {}", key);
            return FTS.search(key);
        }
    };

    private static final LoadingCache<String, List<FTS.Tuple>> cache =
            CacheBuilder.newBuilder()
                    .expireAfterAccess(5, java.util.concurrent.TimeUnit.MINUTES)
                    .maximumSize(1000)
                    .build(loader);

    public static void frontend(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.useVirtualThreads = true;
        }).start(7000);

        app.get("/search", Frontend::handleSearch);
    }

    private static void handleSearch(Context ctx) {
        String query = ctx.queryParam("text");
        ctx.contentType("application/json");

        List<FTS.Tuple> result = null;
        try {
            assert query != null;
            result = cache.get(query);
        } catch (ExecutionException e) {
            // Try direct search
            result = FTS.search(query);
            logger.info("Cache miss for query: {}", query);
        }

        try {
            ctx.result(objectMapper.writeValueAsString(result));
        } catch (JsonProcessingException e) {
            ctx.result("Error processing JSON");
        }
    }
}
