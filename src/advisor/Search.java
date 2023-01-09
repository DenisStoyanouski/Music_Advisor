package advisor;


class Search {

    static void getRequest(String[] request) {

            switch(request[0]) {
                case "new" :  getNew();
                    break;
                case "featured" : getFeatured();
                    break;
                case "categories" : getCategories();
                    break;
                case "playlists" : getPlaylist(request[1]);
                    break;
                case "exit" : exit();
                    System.exit(0);
                    break;
                default :
                    System.out.println("Unknown command");
            }
    }

    private static void getNew() {
        System.out.println("---NEW RELEASES---");
        System.out.println("Mountains [Sia, Diplo, Labrinth]");
        System.out.println("Runaway [Lil Peep]");
        System.out.println("The Greatest Show [Panic! At The Disco]");
        System.out.println("All Out Life [Slipknot]");
    }

    private static void getFeatured() {
        System.out.println("---FEATURED---");
        System.out.println("Mellow Morning");
        System.out.println("Wake Up and Smell the Coffee");
        System.out.println("Monday Motivation");
        System.out.println("Songs to Sing in the Shower");
    }

    private static void getCategories() {
        System.out.println("---CATEGORIES---");
        System.out.println("Top Lists");
        System.out.println("Pop");
        System.out.println("Mood");
        System.out.println("playlists Mood");
    }

    private static void getPlaylist(String s) {
        System.out.println(String.format("---%s PLAYLISTS---", s.toUpperCase()));
        System.out.println("Walk Like A Badass");
        System.out.println("Rage Beats");
        System.out.println("Arab Mood Booster");
        System.out.println("Sunday Stroll");
    }

    private static void exit() {
        System.out.print("---GOODBYE!---");
    }
}
