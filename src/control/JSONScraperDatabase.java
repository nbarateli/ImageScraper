package control;

import javax.json.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public boolean addLink(String url, String src) {
        JsonObject database;
        JsonObjectBuilder builder = addAll(Json.createObjectBuilder(), database = readDatabase(DB_FILE));

        if (!database.containsKey(src))
            builder.add(src, Json.createArrayBuilder().add(url).build());
        else {
            JsonArrayBuilder arrayBuilder = addAll(Json.createArrayBuilder(), database.getJsonArray(src));
            arrayBuilder.add(url);
            builder.add(src, arrayBuilder.build());
        }
        try (JsonWriter writer = Json.createWriter(new FileOutputStream(DB_FILE))) {
            writer.write(builder.build());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return true;
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
