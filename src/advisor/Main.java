package advisor;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while (true) {
            String[] request = input().split("\\s");
            final SearchFactory searchFactory = new SearchFactory();
            final Search search = searchFactory.produce(request);
            assert search != null;
            if (search != null) {
                search.printResult();
            } else {
                System.out.println("Unknown command");
            }

        }

    }
    public static String input() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}

