package advisor;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static String ip = "https://accounts.spotify.com/authorize";

    private static boolean isAuthorized = false;

    public static void main(String[] args) throws IOException, InterruptedException {

        getIp(args);

        String line = null;
        while (!"auth".equals(line)) {
            line = input();
            if ("auth".equals(line)) {
                Authorization authorization = authorization = new Authorization(ip);
                isAuthorized = authorization.isAuthorization();
                break;
            } else if ("exit".equals(line)) {
                System.exit(0);
            } else {
                System.out.println("Please, provide access for application.");
            }
        }

        if (isAuthorized) {
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

    private static void getIp(String[] args) {
        if (args.length > 1) {
            ip = args[1];
        }
    }

    public static String input() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }


}

