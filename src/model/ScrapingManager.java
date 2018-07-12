package model;

import control.JSONScraperDatabase;
import control.MySQLScraperDatabase;
import control.ScraperDatabase;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Collection;

public class ScrapingManager {
    private static ScrapingManager instance;
    private static ScraperDatabase primaryDatabase;

    public static ScrapingManager getInstance() {
        return instance == null ? instance = new ScrapingManager() : instance;
    }

    private ScrapingManager() {
        ScraperDatabase database;
        try {
            database = MySQLScraperDatabase.getInstance();
        } catch (Exception e) {
            database = JSONScraperDatabase.getInstance();
        }
        primaryDatabase = database;
    }

    public ScraperDatabase getDatabase() {
        if (primaryDatabase instanceof JSONScraperDatabase) {
            try {
                primaryDatabase = MySQLScraperDatabase.getInstance();
            } catch (FileNotFoundException | SQLException e) {
                primaryDatabase = null;
            }
        }
        if (primaryDatabase == null || !((MySQLScraperDatabase) primaryDatabase).checkConnection()) {
            primaryDatabase = JSONScraperDatabase.getInstance();
        }
        return primaryDatabase;
    }

    /**
     * Scrapes the given page, stores it in the database and returns the list
     * of found hyperlinks.
     *
     * @param src address of the page to scrape.
     * @return list of found hyperlinks
     */
    public Collection<String> scrapePage(String src) {

        return PageScraper.scrapePage(src, getDatabase());
    }
}
