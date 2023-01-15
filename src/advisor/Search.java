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

class SearchNew implements Search {

    final private String resource;

    final private String token;

    public SearchNew(String resource, String token) {
        this.resource = resource;
        this.token = token;
    }

    @Override
    public void makeRequest() throws InterruptedException, IOException {

        String path = "/v1/browse/new-releases";

        HttpResponse<String> response = new Request(resource, path, token).getRequest();

        if (response.statusCode() == 200) {
            parseResponse(response.body());
        } else {
            System.out.println(response.statusCode());
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

    private String path = "/v1/browse/featured-playlists";

    public SearchFeatured(String resource, String token) {
        this.resource = resource;
        this.token = token;
    }

    @Override
    public void makeRequest() throws InterruptedException, IOException {

        HttpResponse<String> response = new Request(resource, path, token).getRequest();

        if (response.statusCode() == 200) {
            parseResponse(response.body());
        } else {
            System.out.println(response.statusCode());
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
            System.out.println(response.statusCode());
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

    private String categoryId;

    private String path; // v1/browse/categories/{category_id}/playlists

    public SearchPlaylist(String list, String resource, String token) {
        this.list = list;
        this.resource = resource;
        this.token = token;
    }

    @Override
    public void makeRequest() throws InterruptedException, IOException {
        path = "/v1/browse/categories";
        //get {category_id}
        HttpResponse<String> response = new Request(resource, path, token).getRequest();

        if (response.statusCode() == 200) {
            getCategoryId(response.body());
        } else {
            System.out.println(response.statusCode());
        }

        if (categoryId == null || categoryId.matches("\\b[-._~:/?#\\]\\[@!$&'()*+,;=A-Za-z\\d]*\\b")) {
            System.out.println("Unknown category name.");
        } else {
            path = String.format("/v1/playlists/%s", categoryId);
            HttpResponse<String> response1 = new Request(resource, path, token).getRequest();
            if (response.statusCode() == 200) {
                parseResponse(response1.body());
            } else {
                System.out.println(response1.statusCode());
            }
        }
    }

    @Override
    public void parseResponse(String body) {
        JsonObject jo = JsonParser.parseString(body).getAsJsonObject();
        JsonObject tracks = jo.getAsJsonObject("tracks");
        JsonArray items = tracks.getAsJsonArray("items");

        for(JsonElement item : items) {
            item.getAsJsonObject().get("name").getAsString();
        }
    }

    @Override
    public void printResult() {

    }

    public void getCategoryId(String body) {
        JsonObject jo = JsonParser.parseString(body).getAsJsonObject();
        JsonObject playlists = jo.getAsJsonObject("categories");
        JsonArray items = playlists.getAsJsonArray("items");

        for (JsonElement item : items) {
            if (item.getAsJsonObject().get("name").getAsString().equals(list)) {
                categoryId = item.getAsJsonObject().get("id").getAsString();
                System.out.println(categoryId);
            }
        }
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
        StringBuilder listName = new StringBuilder();
        if (request.length > 1) {
            for (int i = 1; i < request.length; i++) {
                listName.append(request[i]).append(" ");
            }
        }

        switch(request[0]) {
            case "new" :   return new SearchNew(resource, token);
            case "featured" : return new SearchFeatured(resource, token);
            case "categories" : return new SearchCategories(resource, token);
            case "playlists" :
                try {
                    return new SearchPlaylist(listName.toString().trim(), resource, token);
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
