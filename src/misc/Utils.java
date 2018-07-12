package misc;

public class Utils {
    public static String escapeHTML(String txt) {

        return txt.replace("&", "&amp;")
                .replace("\"", "&#034;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    private Utils() {
    }
}
