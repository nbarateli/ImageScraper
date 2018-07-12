package serve.api;

import model.PageScraper;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@WebServlet(name = "ProcessSource", urlPatterns = {"/api/process"})
public class ProcessSource extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PageScraper scraper = (PageScraper) getServletContext().getAttribute("scraper");
        String url = addProtocol(request.getParameter("url"));

        List<String> urls = scraper.scrapePage(url);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        urls.forEach(arrayBuilder::add);
        builder.add(url, arrayBuilder.build());
        JsonWriter writer = Json.createWriter(response.getWriter());
        writer.writeObject(builder.build());
        writer.close();
    }

    private String addProtocol(String src) {
        try {
            new URL(src).getProtocol();
            return src;
        } catch (Exception e) {
            return "https://" + src;
        }
    }

}
