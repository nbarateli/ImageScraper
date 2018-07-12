package serve.api;

import model.ScrapingManager;

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
import java.util.Collection;

@WebServlet(name = "ProcessSource", urlPatterns = {"/api/process"})
public class ProcessSource extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ScrapingManager manager = (ScrapingManager) getServletContext().getAttribute("manager");
        String url = addProtocol(request.getParameter("url"));

        Collection<String> urls = manager.scrapePage(url);
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

    /**
     * Adds a protocol (HTTPS) to the given url string (if none is present)
     *
     * @param src a url string to be checked
     * @return processed url string
     */
    private String addProtocol(String src) {
        try {
            new URL(src).getProtocol();
            return src;
        } catch (Exception e) {
            return "https://" + src;
        }
    }

}
