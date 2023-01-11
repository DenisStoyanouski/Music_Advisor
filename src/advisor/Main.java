package advisor;

import java.util.Scanner;

public class Main {
    final private static String IP = "https://accounts.spotify.com/authorize";

    final private static String CLIENT_ID = "59968b64e44b43bcaa61049eaae7b6e7";

    final private static String REDIRECT_URI = "http://mysite.com/callback/";


    public static void main(String[] args) {
        String line = null;
        while (!"auth".equals(line)) {
            line = input();
            if ("auth".equals(line)) {
                break;
            } else if ("exit".equals(line)) {
                System.exit(0);
            } else {
                System.out.println("Please, provide access for application.");
            }
        }

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
        /*String link;
        link = input();*/
        System.out.printf("%s?client_id=%s&redirect_uri=%s&response_type=code%n", IP, CLIENT_ID, REDIRECT_URI);
        System.out.println("---SUCCESS---");
        success = true;
        /*do {
            link = input();
            System.out.println("---SUCCESS---");
            if (link.equals(LINK)) {
                System.out.println("---SUCCESS---");
                success = true;
            } else {
                System.exit(0);
                System.out.println("Link does not exist. Try again:");
            }
        } while(!success);*/

        return success;
    }

    public static String input() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}

