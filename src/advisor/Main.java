package advisor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static String ip;
    private static String resource;
    static int page;

    private static final Map<String, String> arguments = new HashMap<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        getArguments(args);
        Engine.start(ip, resource);
    }

    private static void getArguments(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            arguments.put(args[i].replaceAll("-",""), args[i + 1]);
        }
        ip = arguments.getOrDefault("access", "https://accounts.spotify.com");
        resource = arguments.getOrDefault("resource", "https://api.spotify.com");
        page = Integer.parseInt(arguments.getOrDefault("page", "5"));
    }


}
class Engine {
    private static String accessToken;

    static void start(String ip, String resource) throws IOException, InterruptedException {
        String line = null;
        do {
            line = input();
            if ("auth".equals(line)) {
                accessToken = new Authorization(ip).isAuthorization();

                break;
            } else if ("exit".equals(line)) {
                System.exit(0);
            } else {
                System.out.println("Please, provide access for application.");
            }
        } while (!"auth".equals(line));

        if (!accessToken.isEmpty()) {
            while (true) {
                String[] request = input().split("\\s");
                final SearchFactory searchFactory = new SearchFactory();
                final Search search = searchFactory.produce(request, resource, accessToken);
                assert search != null;
                if (search != null) {
                    search.makeRequest();
                } else {
                    System.out.println("Unknown command");
                }
            }
        }
    }
    public static String input() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}

