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
    }

    private static void getCategories() {
        System.out.println("---CATEGORIES---");
    }

    private static void getPlaylist(String s) {
        System.out.println(String.format("---playlist %s---", s));
    }

    private static void exit() {
        System.out.println("---GOODBYE!---");
        System.exit(0);
    }
}
