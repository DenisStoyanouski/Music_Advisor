package advisor;

import java.util.ArrayList;
import java.util.Arrays;

class Viewer {

    private int page = 5;
    private int currentPage = 1;
    private final ArrayList<ArrayList<String>> items = new ArrayList<>();

    public void readData(String... values) {
        ArrayList<String> item = new ArrayList<>();
        item.addAll(Arrays.asList(values));
        items.add(item);
    }

    public void startView() {
        printPage(page);
        makeView();
    }

    private void printPage(int page) {
        for (int i = currentPage; i < currentPage + page - 1; i++) {
            for (String str : items.get(i)) {
                System.out.println(str);
            }
        }
        currentPage++;
        System.out.printf("---PAGE %d OF %d---", currentPage, items.size() / page);
    }

    private void makeView() {
        String command = Engine.input();
        while (!"exit".equals(command)) {
            switch (command) {
                case "next" : currentPage += 1;
                printPage(page);
                break;
                case "prev" : currentPage -= page;
                printPage(page);
                break;
                case "exit" : break;
                default :
                    System.out.println("next, prev, exit");
                    break;
            }
        }
    }
}
