package dev.gnmathur.fe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gnmathur.FTS;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class Frontend {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void frontend(String[] args) {
        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.useVirtualThreads = true;
            javalinConfig.showJavalinBanner = false;
        }).start(7000);

        app.get("/search", Frontend::handleSearch);
    }

    private static void handleSearch(Context ctx) {
        String query = ctx.queryParam("text");
        ctx.contentType("application/json");

        var result = FTS.search(query);

        try {
            ctx.result(objectMapper.writeValueAsString(result));
        } catch (JsonProcessingException e) {
            ctx.result("Error processing JSON");
        }
    }
}
