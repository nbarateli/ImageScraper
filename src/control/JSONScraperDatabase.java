package control;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
        JsonObject database = readDatabase(DB_FILE);
        if (!database.containsKey(src))
            database.put(src, Json.createArrayBuilder().add(url).build());
        else {
            JsonArrayBuilder builder = Json.createArrayBuilder();
            builder.add(database.get(src));
        }
        return false;
    }

    @Override
    public Map<String, List<String>> getAllLinks() {
        return null;
    }

    private JsonObject readDatabase(String filename) {
        try (JsonReader reader = Json.createReader(new FileInputStream(filename))) {

            return reader.readObject();
        } catch (FileNotFoundException e) {
            return Json.createObjectBuilder().build();
        }
    }
}
