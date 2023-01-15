package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

interface Search {

    void makeRequest() throws InterruptedException, IOException;

    void parseResponse(String body);

    void printResult();


}

class SearchNew implements Search{

    final private String resource;

    final private String token;

    private String print;

    public SearchNew(String resource, String token) {
        this.resource = resource;
        this.token = token;
    }

    @Override
    public void makeRequest() throws InterruptedException, IOException {

        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofMillis(1000L)).build();

        String path = "/v1/browse/new-releases";
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .uri(URI.create(String.format("%s%s", resource, path)))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            parseResponse(response.body());
        } else {
            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject error = jo.getAsJsonObject("error");
            System.out.println(error.getAsJsonObject().get("message").getAsString());
        }

    }

    @Override
    public void parseResponse(String body) {
        JsonObject jo = JsonParser.parseString(body).getAsJsonObject();
        JsonObject albumsObj = jo.getAsJsonObject("albums");
        JsonArray itemsObj = albumsObj.getAsJsonArray("items");

        for (JsonElement item : itemsObj) {
            JsonObject itemObj = item.getAsJsonObject();
            System.out.println(itemObj.get("name").getAsString());
            JsonArray artists = itemObj.get("artists").getAsJsonArray();
            List<String> artistList = new ArrayList<>();
            for (JsonElement artist : artists) {
                artistList.add(artist.getAsJsonObject().get("name").getAsString());
            }
            System.out.println(artistList);
            System.out.println(itemObj.get("external_urls").getAsJsonObject().get("spotify").getAsString());
            System.out.println();
        }
    }

    @Override
    public void printResult() {
    }
}

class SearchFeatured implements Search{

    private final String resource;

    private final String token;

    public SearchFeatured(String resource, String token) {
        this.resource = resource;
        this.token = token;
    }

    @Override
    public void makeRequest() throws InterruptedException, IOException {
        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofMillis(1000L)).build();

        String path = "/v1/browse/featured-playlists";
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .uri(URI.create(String.format("%s%s", resource, path)))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            parseResponse(response.body());
        } else {
            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject error = jo.getAsJsonObject("error");
            System.out.println(error.getAsJsonObject().get("message").getAsString());
        }
    }

    @Override
    public void parseResponse(String body) {
        JsonObject jo = JsonParser.parseString(body).getAsJsonObject();
        JsonObject playlists = jo.getAsJsonObject("playlists");
        JsonArray items = playlists.getAsJsonArray("items");

        for(JsonElement item : items) {
            System.out.println(item.getAsJsonObject().get("name").getAsString());
            System.out.println(item.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString());
            System.out.println();
        }
    }

    @Override
    public void printResult() {
        System.out.println("end");
    }
}

class SearchCategories implements Search{

    final String resource;

    final String token;

    final String path = "/v1/browse/categories";

    public SearchCategories(String resource, String token) {
        this.resource = resource;
        this.token = token;
    }

    @Override
    public void makeRequest() throws IOException, InterruptedException {
        HttpResponse<String> response = new Request(resource, path, token).getRequest();

        if (response.statusCode() == 200) {
            parseResponse(response.body());
        } else {
            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject error = jo.getAsJsonObject("error");
            System.out.println(error.getAsJsonObject().get("message").getAsString());
        }
    }

    @Override
    public void parseResponse(String body) {
        JsonObject jo = JsonParser.parseString(body).getAsJsonObject();
        JsonObject playlists = jo.getAsJsonObject("categories");
        JsonArray items = playlists.getAsJsonArray("items");

        for(JsonElement item : items) {
            System.out.println(item.getAsJsonObject().get("name").getAsString());
            System.out.println(item.getAsJsonObject().get("id").getAsString());
        }
    }

    @Override
    public void printResult() {
    }
}

class SearchPlaylist implements Search{
    private String list;
    private String resource;
    private String token;

    final private String path = "v1/browse/categories/{category_id}/playlists";

    public SearchPlaylist(String list, String resource, String token) {
        this.list = list;
        this.resource = resource;
        this.token = token;
    }

    @Override
    public void makeRequest() throws InterruptedException, IOException {
        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofMillis(1000L)).build();

        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .uri(URI.create(String.format("%s%s", resource, path)))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            parseResponse(response.body());
        } else {
            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject error = jo.getAsJsonObject("error");
            System.out.println(error.getAsJsonObject().get("message").getAsString());
        }
    }

    @Override
    public void parseResponse(String body) {

    }

    @Override
    public void printResult() {

    }
}

class Exit implements Search{
    @Override
    public void makeRequest() {
        printResult();
    }

    @Override
    public void parseResponse(String body) {

    }

    @Override
    public void printResult() {
        System.out.printf("---%s---%n", Mode.EXIT.getName());
        System.exit(0);
    }
}


class SearchFactory {

    static Search produce(String[] request, String resource, String token) {
        switch(request[0]) {
            case "new" :   return new SearchNew(resource, token);
            case "featured" : return new SearchFeatured(resource, token);
            case "categories" : return new SearchCategories(resource, token);
            case "playlists" :
                try {
                    return new SearchPlaylist(request[1], resource, token);
                } catch (ArrayIndexOutOfBoundsException e) {
                break;
                }
            case "exit" : return new Exit();
            default : break;
            }
            return null;
            
    }
}

enum Mode {
    NEW ("NEW RELEASES"),
    FEATURED ("FEATURED"),
    CATEGORIES("CATEGORIES"),
    PLAYLISTS("PLAYLISTS"),
    EXIT("GOODBYE!");
    
    String name;
    
    Mode(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
