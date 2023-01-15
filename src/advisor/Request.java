package advisor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

class Request {

    private  String resource;

    private  String path;

    private  String token;

    Request(String resource, String path, String token) {
        this.resource = resource;
        this.path = path;
        this.token = token;
    }

    public HttpResponse<String> getRequest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofMillis(1000L)).build();

        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .uri(URI.create(String.format("%s%s", resource, path)))
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

}
