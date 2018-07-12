import control.JSONScraperDatabase;
import control.ScraperDatabase;

import java.util.Scanner;

public class Launcher {

    public static void main(String[] args) throws Exception {
        ScraperDatabase instance = JSONScraperDatabase.getInstance();
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("read or write? (r/w)");
            String next = in.next();
            if (next.equalsIgnoreCase("stop")) break;
            if (next.length() > 0) {
                switch (Character.toLowerCase(next.charAt(0))) {
                    case 'r':
                        System.out.println(instance.getAllLinks());
                        break;
                    case 'w':
                        System.out.print("src: ");
                        String src = in.next();
                        System.out.print("link: ");
                        String href = in.next();
                        try {
                            instance.addLink(href, href);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            }
        }
    }
}
