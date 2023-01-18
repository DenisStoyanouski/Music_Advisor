package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

interface Search {

    void makeRequest() throws InterruptedException, IOException;

    void parseResponse(String body);
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
        Viewer viewer = new Viewer(Main.page);
        JsonObject jo = JsonParser.parseString(body).getAsJsonObject();
        JsonObject albumsObj = jo.getAsJsonObject("albums");
        JsonArray itemsObj = albumsObj.getAsJsonArray("items");

        for (JsonElement item : itemsObj) {
            JsonObject itemObj = item.getAsJsonObject();
            String name = itemObj.get("name").getAsString();
            JsonArray artists = itemObj.get("artists").getAsJsonArray();
            List<String> artistList = new ArrayList<>();
            for (JsonElement artist : artists) {
                artistList.add(artist.getAsJsonObject().get("name").getAsString());
            }
            String art = String.valueOf(artistList);
            String url = itemObj.get("external_urls").getAsJsonObject().get("spotify").getAsString();
            viewer.readData(name, art, url);
        }
        viewer.startView();
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

        String PATH = "/v1/browse/featured-playlists";
        HttpResponse<String> response = new Request(resource, PATH, token).getRequest();

        if (response.statusCode() == 200) {
            parseResponse(response.body());
        } else {
            System.out.println(response.statusCode());
        }
    }

    @Override
    public void parseResponse(String body) {
        Viewer viewer = new Viewer(Main.page);
        JsonObject jo = JsonParser.parseString(body).getAsJsonObject();
        JsonObject playlists = jo.getAsJsonObject("playlists");
        JsonArray items = playlists.getAsJsonArray("items");

        for(JsonElement item : items) {
            String name = item.getAsJsonObject().get("name").getAsString();
            String url = item.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString();
            viewer.readData(name, url);
        }
        viewer.startView();
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

        Viewer viewer = new Viewer(Main.page);
        JsonObject jo = JsonParser.parseString(body).getAsJsonObject();
        JsonObject playlists = jo.getAsJsonObject("categories");
        JsonArray items = playlists.getAsJsonArray("items");

        for(JsonElement item : items) {
            viewer.readData(item.getAsJsonObject().get("name").getAsString());
        }
        viewer.startView();
    }
}

class SearchPlaylist implements Search{
    final private String list;
    final private String resource;
    final private String token;

    private String categoryId;

    public SearchPlaylist(String list, String resource, String token) {
        this.list = list;
        this.resource = resource;
        this.token = token;
    }

    @Override
    public void makeRequest() throws InterruptedException, IOException {
        String path = "/v1/browse/categories";
        //get {category_id}
        HttpResponse<String> response = new Request(resource, path, token).getRequest();

        if (response.statusCode() == 200 && !response.body().contains("error")) {
            getCategoryId(response.body());
        } else if (response.statusCode() >= 400 || response.body().contains("error")) {
            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            System.out.println(jo.get("error").getAsJsonObject().get("message").getAsString());
        }
        if (categoryId == null) {
            System.out.println("Unknown category name.");
        } else {
            path = String.format("/v1/browse/categories/%s/playlists", categoryId);
            HttpResponse<String> response1 = new Request(resource, path, token).getRequest();
            if (response.statusCode() == 200 && !response.body().contains("error")) {
                parseResponse(response1.body());
            } else if (response.statusCode() >= 400 || response.body().contains("error")){
                JsonObject jo = JsonParser.parseString(response1.body()).getAsJsonObject();
                System.out.println(jo.get("error").getAsJsonObject().get("message").getAsString());
            }
        }
    }

    @Override
    public void parseResponse(String body) {
        Viewer viewer = new Viewer(Main.page);
        JsonObject jo = JsonParser.parseString(body).getAsJsonObject();
        try {
            JsonObject tracks = jo.getAsJsonObject("playlists");
            JsonArray items = tracks.getAsJsonArray("items");

            for(JsonElement item : items) {
                String name = item.getAsJsonObject().get("name").getAsString();
                String url = item.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString();
                viewer.readData(name, url);
            }
            viewer.startView();
        } catch (NullPointerException e) {
            System.out.println(jo.get("error").getAsJsonObject().get("message").getAsString());
        }
    }

    public void getCategoryId(String body) {
        JsonObject jo = JsonParser.parseString(body).getAsJsonObject();
        try {
            JsonObject playlists = jo.getAsJsonObject("categories");
            JsonArray items = playlists.getAsJsonArray("items");

            for (JsonElement item : items) {
                if (item.getAsJsonObject().get("name").getAsString().equals(list)) {
                    categoryId = item.getAsJsonObject().get("id").getAsString();
                }
            }
        } catch (NullPointerException e) {
            System.out.println(jo.get("error").getAsJsonObject().get("message").getAsString());
        }
    }
}

class Exit implements Search{
    @Override
    public void makeRequest() {
        System.out.println("---GOODBYE!---");
        System.exit(0);
    }

    @Override
    public void parseResponse(String body) {
    }
}


class SearchFactory {

    static Search produce(String[] request, String resource, String token) {
        StringBuilder listName = new StringBuilder();
        if (request.length > 1) {
            Arrays.stream(request).forEach(x -> listName.append(x).append(" "));
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
            default :
                System.out.println("new, featured, categories, playlists <name>, exit");
                break;
            }
        return null;
    }
}
