package control;

import java.util.List;

public interface ScraperDatabase {
    /**
     * Adds the image to the database.
     *
     * @return whether the operation was successful
     */
    boolean addImage(String url);

    /**
     * Returns the list of all previously added images
     */
    List<String> getAllImages();
}
