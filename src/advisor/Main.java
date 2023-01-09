package advisor;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while (true) {
            String[] request = input().split("\\s");
            Search.getRequest(request);
        }

    }
    public static String input() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}

