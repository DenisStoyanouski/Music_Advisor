package advisor;

interface Search {
    void printResult();
}

class SearchNew implements Search{
    @Override
    public void printResult() {
        System.out.println("---NEW RELEASES---");
        System.out.println("Mountains [Sia, Diplo, Labrinth]");
        System.out.println("Runaway [Lil Peep]");
        System.out.println("The Greatest Show [Panic! At The Disco]");
        System.out.println("All Out Life [Slipknot]");
    }
}

class SearchFeatured implements Search{
    @Override
    public void printResult() {
        System.out.println("---FEATURED---");
        System.out.println("Mellow Morning");
        System.out.println("Wake Up and Smell the Coffee");
        System.out.println("Monday Motivation");
        System.out.println("Songs to Sing in the Shower");
    }
}

class SearchCategories implements Search{
    @Override
    public void printResult() {
        System.out.println("---CATEGORIES---");
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
        System.out.println(String.format("---%s PLAYLISTS---", list.toUpperCase()));
        System.out.println("Walk Like A Badass");
        System.out.println("Rage Beats");
        System.out.println("Arab Mood Booster");
        System.out.println("Sunday Stroll");
    }
}

class Exit implements Search{
    @Override
    public void printResult() {
        System.out.print("---GOODBYE!---");
        System.exit(0);
    }
}


class SearchFactory {

    static Search produce(String[] request) {

            switch(request[0]) {
                case "new" :  return new SearchNew();
                case "featured" : return new SearchFeatured();
                case "categories" : return new SearchCategories();
                case "playlists" : return new SearchPlaylist(request[1]);
                case "exit" : return new Exit();
                default : break;
            }
            return null;
    }
}
