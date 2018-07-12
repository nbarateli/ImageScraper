package control;

import javax.json.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;

/**
 * Implements the ScrapperDatabase interface using JSON files.
 */
public class JSONScraperDatabase implements ScraperDatabase {
    private static final String DB_FILE = "Database.json";
    private static JSONScraperDatabase instance;

    public static ScraperDatabase getInstance() {
        return instance == null ? instance = new JSONScraperDatabase() : instance;
    }

    private JSONScraperDatabase() {

    }

    @Override
    public void addLink(String url, String src) {
        addLinks(src, Collections.singletonList(src));

    }

    @Override
    public void addLinks(String src, List<String> urls) {
        JsonObject database;
        JsonObjectBuilder builder = addAll(Json.createObjectBuilder(), database = readDatabase(DB_FILE));

        if (!database.containsKey(src)) {
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            urls.forEach(arrayBuilder::add);
            builder.add(src, arrayBuilder.build());
        } else {
            JsonArrayBuilder arrayBuilder = addAll(Json.createArrayBuilder(), database.getJsonArray(src));
            urls.forEach(arrayBuilder::add);
            builder.add(src, arrayBuilder.build());
        }
        try (JsonWriter writer = Json.createWriter(new FileOutputStream(DB_FILE))) {
            writer.write(builder.build());
        } catch (FileNotFoundException ignored) {

        }
    }

    @Override
    public Map<String, List<String>> getAllLinks() {
        JsonObject o = readDatabase(DB_FILE);
        Map<String, List<String>> result = new HashMap<>();
        o.keySet().forEach(k -> {
            List<String> links = new ArrayList<>();
            o.getJsonArray(k).forEach(el -> links.add(el.toString()));
            result.put(k, links);
        });
        return result;
    }

    private JsonArrayBuilder addAll(JsonArrayBuilder arrayBuilder, JsonArray array) {
        array.forEach(arrayBuilder::add);
        return arrayBuilder;
    }

    private JsonObjectBuilder addAll(JsonObjectBuilder objectBuilder, JsonObject o) {
        o.keySet().forEach(k -> objectBuilder.add(k, o.get(k)));
        return objectBuilder;
    }

    private JsonObject readDatabase(String filename) {
        try (JsonReader reader = Json.createReader(new FileInputStream(filename))) {

            return reader.readObject();
        } catch (FileNotFoundException | JsonException e) {
            return Json.createObjectBuilder().build();
        }
    }
}
