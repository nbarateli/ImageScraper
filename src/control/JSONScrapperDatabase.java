package control;

import java.util.List;
import java.util.Map;

/**
 * Implements the ScrapperDatabase interface using JSON files.
 */
public class JSONScrapperDatabase implements ScraperDatabase {
    private JSONScrapperDatabase() {
    }

    @Override
    public boolean addImage(String url) {
        return false;
    }

    @Override
    public Map<String, List<String>> getAllImages() {
        return null;
    }
}
