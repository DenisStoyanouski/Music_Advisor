package advisor;

import java.util.Scanner;

public class Main {

    final private String CLIENT_ID = "59968b64e44b43bcaa61049eaae7b6e7";

    final private String REDIRECT_URI = "http://mysite.com/callback/";

    static final private String LINK = "https://accounts.spotify.com/authorize?client_id=a19ee7dbfda443b2a8150c9101bfd645&redirect_uri=http://localhost:8080&response_type=code";

    public static void main(String[] args) {
        String line;
        do {
            line = input();
            System.out.println("Please, provide access for application.");
        } while (!"auth".equals(line));

        if (authorization()) {
            while (true) {
                String[] request = input().split("\\s");
                final SearchFactory searchFactory = new SearchFactory();
                final Search search = searchFactory.produce(request);
                assert search != null;
                if (search != null) {
                    search.printResult();
                } else {
                    System.out.println("Unknown command");
                }
            }
        }

    }

    private static boolean authorization() {
        boolean success = false;
        String link = input();
        do {
            if (link.equals(LINK)) {
                System.out.println("---SUCCESS---");
                success = true;
            } else {
                System.out.println("Link does not exist");
            }
        } while(!success);

        return success;
    }

    public static String input() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}

