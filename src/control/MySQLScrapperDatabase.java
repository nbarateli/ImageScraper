package control;

import java.util.List;

public class MySQLScrapperDatabase implements ScraperDatabase {
    @Override
    public boolean addImage(String url) {
        return false;
    }

    @Override
    public List<String> getAllImages() {
        return null;
    }
}
