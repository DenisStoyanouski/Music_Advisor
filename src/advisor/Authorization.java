package advisor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;

class Authorization {

    static String ip;

    final private static String CLIENT_ID = "59968b64e44b43bcaa61049eaae7b6e7";

    final private static String CLIENT_SECRET = "97b69291dd9342b8a61250a8bd6a4778";

    final private static String REDIRECT_URI = "http://localhost:8888";

    private static String code;

    private static String accessToken;

    public Authorization(String ip) {
        this.ip = ip;
    }



    String isAuthorization() throws IOException, InterruptedException {
        System.out.println("use this link to request the access code:");
        System.out.printf("%s/authorize?client_id=%s&redirect_uri=%s&response_type=code%n", ip, CLIENT_ID, REDIRECT_URI);
        getCode();
        Thread.sleep(10000);
        getAccessToken();
        return accessToken;
    }

    private void getCode() throws IOException {
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

    private void getAccessToken() throws IOException, InterruptedException {
        System.out.println("Making http request for access_token...");
        String encoded = Base64.getEncoder().encodeToString(String.format("%s:%s",CLIENT_ID, CLIENT_SECRET).getBytes());

        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofMillis(1000L)).build();

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic " + encoded)
                .uri(URI.create(String.format("%s/api/token", ip)))
                .POST(HttpRequest.BodyPublishers.ofString(String.format("grant_type=authorization_code&code=%s&redirect_uri=%s", code, REDIRECT_URI)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            accessToken = parseToken(response.body());
        }
        if (accessToken.contains("access_token")) {
            System.out.println("Success!");
        }
    }

    private String parseToken(String body) {
        JsonElement jsonElement = JsonParser.parseString(body);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return jsonObject.get("access_token").getAsString();
    }
}
