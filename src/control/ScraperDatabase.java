package control;

import java.util.List;
import java.util.Map;

public interface ScraperDatabase {
    /**
     * Adds the image to the database.
     *
     * @param url the hyperlink to be added
     * @param src the webpage from which the given hyperlink  was scraped
     * @return whether the operation was successful
     */
    boolean addLink(String url, String src);

    /**
     * Returns the map of all sources with the list of associated hyperlinks.
     */
    Map<String, List<String>> getAllLinks();
}
