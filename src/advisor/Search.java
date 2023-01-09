package advisor;

interface Search {
    void printResult();
}

class SearchNew implements Search{
    @Override
    public void printResult() {
        System.out.printf("---%s---%n", Mode.NEW.getName());
        System.out.println("Mountains [Sia, Diplo, Labrinth]");
        System.out.println("Runaway [Lil Peep]");
        System.out.println("The Greatest Show [Panic! At The Disco]");
        System.out.println("All Out Life [Slipknot]");
    }
}

class SearchFeatured implements Search{
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
    final private String list;
    public SearchPlaylist(String list) {
        this.list = list;
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
    public void printResult() {
        System.out.printf("---%s---%n", Mode.EXIT.getName());
        System.exit(0);
    }
}


class SearchFactory {

    static Search produce(String[] request) {
        switch(request[0]) {
            case "new" :   return new SearchNew();
            case "featured" : return new SearchFeatured();
            case "categories" : return new SearchCategories();
            case "playlists" :
                try {
                    return new SearchPlaylist(request[1]);
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
