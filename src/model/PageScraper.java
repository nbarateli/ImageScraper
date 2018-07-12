package model;

import control.ScraperDatabase;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PageScraper {

    /**
     * Scrapes the given page, stores it in the database and returns the list
     * of found hyperlinks.
     *
     * @param src address of the page to scrape.
     * @return list of found hyperlinks
     */
    public static Collection<String> scrapePage(String src, ScraperDatabase database) {
        Collection<String> hrefs = new HashSet<>();
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
        hrefs = processURLs(src, (Set<String>) hrefs, protocol);

        database.addLinks(src, hrefs);

        return hrefs;
    }

    private static Set<String> processURLs(String src, Set<String> hrefs, String protocol) {
        Set<String> result = new HashSet<>();
        hrefs.forEach(el -> result.add(processURL(src, el, protocol)));
        return result;
    }

    private static String processURL(String src, String href, String protocol) {
        if (src.substring(0, 2).equalsIgnoreCase("//"))
            return protocol + href;
        try {
            new URL(href).getProtocol();
            return href;
        } catch (MalformedURLException e) {
            return src + "/." + href;
        }
    }

    private static void addIfHas(Element element, String attr, Collection<String> hrefs) {
        if (element.hasAttr(attr)) hrefs.add(element.attr(attr));
    }

    private PageScraper() {

    }

}
