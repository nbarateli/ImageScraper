package control;

import java.util.List;
import java.util.Map;

public interface ScraperDatabase {
    /**
     * Adds the image to the database.
     *
     * @param url the hyperlink to be added
     * @param src the webpage from which the given hyperlink  was scraped
     */
    void addLink(String url, String src);

    /**
     * Adds and associates all the given likes to the given source.
     */
    void addLinks(String src, List<String> urls);

    /**
     * Returns the map of all sources with the list of associated hyperlinks.
     */
    Map<String, List<String>> getAllLinks();
}
