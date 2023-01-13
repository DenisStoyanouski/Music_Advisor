package advisor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static String ip = "https://accounts.spotify.com";

    private static String endpoint;

    final private static String CLIENT_ID = "59968b64e44b43bcaa61049eaae7b6e7";

    final private static String CLIENT_SECRET = "97b69291dd9342b8a61250a8bd6a4778";

    final private static String REDIRECT_URI = "http://localhost:8888";

    private static String code;

    private static boolean isSuccess;




    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length > 1) {
            ip = args[1];
        }
        for (String str : args) {}



        String line = null;
        while (!"auth".equals(line)) {
            line = input();
            if ("auth".equals(line)) {
                authorization();
                break;
            } else if ("exit".equals(line)) {
                System.exit(0);
            } else {
                System.out.println("Please, provide access for application.");
            }
        }

        if (isSuccess) {
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

    private static void authorization() throws IOException, InterruptedException {
        System.out.println("use this link to request the access code:");
        System.out.printf("%s?client_id=%s&redirect_uri=%s&response_type=code%n", ip, CLIENT_ID, REDIRECT_URI);
        isSuccess = false;
        getCode();
        Thread.sleep(5000);
        getAccessToken();
        Thread.sleep(5000);
        isSuccess = true;
    }

    public static String input() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static void getCode() throws IOException {
        System.out.println("waiting for code...");
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8888), 50);
        server.createContext("/",
                new HttpHandler() {
                    public void handle(HttpExchange exchange) throws IOException {
                        String query = exchange.getRequestURI().getQuery();
                        String hello = null;
                        if (query.contains("error=access_denied")) {
                            hello = "Authorization code not found. Try again.";
                            exchange.sendResponseHeaders(200, hello.length());
                            exchange.getResponseBody().write(hello.getBytes());
                            exchange.getResponseBody().close();
                        } else if (query.contains("code=")) {
                            code = query.replaceAll("code=", "");
                            hello = "Got the code. Return back to your program.";
                            exchange.sendResponseHeaders(200, hello.length());
                            exchange.getResponseBody().write(hello.getBytes());
                            exchange.getResponseBody().close();
                            server.stop(1);
                            System.out.println("code received");
                        }
                    }
                }
        );
        server.start();
    }

    private static void getAccessToken() throws IOException, InterruptedException {
        if (code == null) {
            return;
        }
        System.out.println("making http request for access_token...");
        String encoded = Base64.getEncoder().encodeToString(String.format("%s:%s",CLIENT_ID, CLIENT_SECRET).getBytes());

        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic " + encoded)
                .uri(URI.create("https://accounts.spotify.com/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(String.format("grant_type=authorization_code&code=%s&redirect_uri=%s", code, REDIRECT_URI)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.body().contains("access_token")) {
            System.out.println(response.body());
            System.out.println("---SUCCESS---");

        }
    }
}

