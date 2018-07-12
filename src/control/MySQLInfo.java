package control;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public final class MySQLInfo {
    private static final String DB_INFO_FILE = "DatabaseInfo.json";

    public static Map<String, String> readDBInfo() throws FileNotFoundException {
        JsonReader reader = Json.createReader(new FileInputStream(DB_INFO_FILE));
        Map<String, String> result = new HashMap<>();
        JsonObject info = reader.readObject();
        result.put("server", info.getString("server"));
        result.put("username", info.getString("username"));
        if (info.isNull("password"))
            result.put("password", "");
        else result.put("password", info.getString("password"));
        result.put("database", info.getString("database"));
        return result;
    }


    private MySQLInfo() {
    }
}
