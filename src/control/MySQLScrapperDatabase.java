package control;

import java.util.List;

/**
 * Implements the ScrapperDatabase interface using the MySQL database.
 */
public class MySQLScrapperDatabase implements ScraperDatabase {
    private MySQLScrapperDatabase() {

    }

    @Override
    public boolean addImage(String url) {
        return false;
    }

    @Override
    public List<String> getAllImages() {
        return null;
    }
}
