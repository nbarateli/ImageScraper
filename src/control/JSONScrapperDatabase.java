package control;

import java.util.List;

public class JSONScrapperDatabase implements ScraperDatabase {
    @Override
    public boolean addImage(String url) {
        return false;
    }

    @Override
    public List<String> getAllImages() {
        return null;
    }
}
