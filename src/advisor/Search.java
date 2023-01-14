package advisor;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

interface Search {

    void getRequest() throws InterruptedException, IOException;

    void parseResponse(String body);

    void printResult();


}

class SearchNew implements Search{

    private String resource;

    private String token;

    final private String PATH = "/v1/browse/categories";

    private String print;

    public SearchNew(String resource, String token) {
        this.resource = resource;
        this.token = token;
    }

    @Override
    public void getRequest() throws InterruptedException, IOException {
        System.out.println(token);

        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofMillis(1000L)).build();

        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + token.replaceAll("access_token:", ""))
                .header("Content-Type", "application/json")
                .uri(URI.create(String.format("%s%s", resource, PATH)))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body());
        System.out.println(response.uri().isAbsolute());
        if (response.statusCode() == 200) {
            parseResponse(response.body());
        }

    }

    @Override
    public void parseResponse(String body) {
        System.out.println(body);
        JsonElement jsonElement = JsonParser.parseString(body);




    }

    @Override
    public void printResult() {
        System.out.println(print);
    }
}

class SearchFeatured implements Search{

    private String resource;

    private String token;

    final private String PATH = "/v1/browse/featured-playlists";

    public SearchFeatured(String resource, String token) {
        this.resource = resource;
        this.token = token;
    }

    @Override
    public void getRequest() throws InterruptedException, IOException {

    }

    @Override
    public void parseResponse(String body) {

    }

    @Override
    public void printResult() {
        System.out.printf("---%s---%n", Mode.FEATURED.getName());
        System.out.println("Mellow Morning");
        System.out.println("Wake Up and Smell the Coffee");
        System.out.println("Monday Motivation");
        System.out.println("Songs to Sing in the Shower");
    }
}

class SearchCategories implements Search{

    private String resource;

    private String token;

    final private String PATH = "/v1/browse/categories";

    public SearchCategories(String resource, String token) {
        this.resource = resource;
        this.token = token;
    }

    @Override
    public void getRequest() throws InterruptedException, IOException {

    }

    @Override
    public void parseResponse(String body) {

    }

    @Override
    public void printResult() {
        System.out.printf("---%s---%n", Mode.CATEGORIES.getName());
        System.out.println("Top Lists");
        System.out.println("Pop");
        System.out.println("Mood");
        System.out.println("playlists Mood");
    }
}

class SearchPlaylist implements Search{
    private String list;
    private String resource;
    private String token;

    public SearchPlaylist(String list, String resource, String token) {
        this.list = list;
        this.resource = resource;
        this.token = token;
    }

    @Override
    public void getRequest() throws InterruptedException, IOException {

    }

    @Override
    public void parseResponse(String body) {

    }

    @Override
    public void printResult() {
        System.out.printf("---%s %s---%n", list.toUpperCase(), Mode.PLAYLISTS.getName());
        System.out.println("Walk Like A Badass");
        System.out.println("Rage Beats");
        System.out.println("Arab Mood Booster");
        System.out.println("Sunday Stroll");
    }
}

class Exit implements Search{
    @Override
    public void getRequest() {

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
