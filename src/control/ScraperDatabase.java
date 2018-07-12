package control;

import java.util.List;
import java.util.Map;

public interface ScraperDatabase {
    /**
     * Adds the image to the database.
     *
     * @return whether the operation was successful
     */
    boolean addImage(String url, String src);

    /**
     * Returns the list of all previously added images
     */
    Map<String, List<String>> getAllImages();
}
