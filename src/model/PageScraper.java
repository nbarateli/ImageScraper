package model;

import control.ScraperDatabase;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PageScraper {
    private final ScraperDatabase database;

    public PageScraper(ScraperDatabase database) {
        this.database = database;
    }

    /**
     * Scrapes the given page, stores it in the database and returns the list
     * of found hyperlinks.
     *
     * @param src address of the page to scrape.
     * @return list of found hyperlinks
     */
    public List<String> scrapePage(String src) {
        List<String> hrefs = new ArrayList<>();
        String protocol = "https";
        try {
            Connection connection = Jsoup.connect(src).followRedirects(true);
            Response response = connection.execute();
            src = response.url().toString();
            protocol = response.url().getProtocol();
            Elements elements = connection.get().getAllElements();
            for (Element element : elements) {
                addIfHas(element, "href", hrefs);
                addIfHas(element, "src", hrefs);
            }

        } catch (Exception ignored) {

        }
        processURLs(src, hrefs, protocol);
        database.addLinks(src, hrefs);

        return hrefs;
    }


    private void processURLs(String src, List<String> hrefs, String protocol) {
        for (int i = 0; i < hrefs.size(); i++) {
            hrefs.set(i, processURL(src, hrefs.get(i), protocol));
        }
    }

    private String processURL(String src, String href, String protocol) {
        if (src.substring(0, 2).equalsIgnoreCase("//"))
            return protocol + href;
        try {
            new URL(href).getProtocol();
            return href;
        } catch (MalformedURLException e) {
            return src + "/." + href;
        }
    }

    private void addIfHas(Element element, String attr, List<String> hrefs) {
        if (element.hasAttr(attr)) hrefs.add(element.attr(attr));
    }

}
