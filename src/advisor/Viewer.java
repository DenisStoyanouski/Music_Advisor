package advisor;

import java.util.ArrayList;
import java.util.Arrays;

class Viewer {

    private int page = 5;
    private int currentPage = 1;

    private int currentItem = 1;
    private final ArrayList<ArrayList<String>> items = new ArrayList<>();

    public void readData(String... values) {
        ArrayList<String> item = new ArrayList<>();
        item.addAll(Arrays.asList(values));
        items.add(item);
    }

    public void startView() {
        printPage();
        makeView();
    }

    private void printPage() {

        if (currentItem < items.size()) {
            int limit = Math.min(currentPage * page, items.size() - 1);
            for (int i = currentItem; i <= limit; i++) {
                for (String str : items.get(i)) {
                    System.out.println(str);
                }
            }
            System.out.printf("---PAGE %d OF %d---%n", currentPage++, items.size() / page);
        } else {
            System.out.println("next");
        }

    }

    private void makeView() {
        String command = null;
        while (!"exit".equals(command)) {
            command = Engine.input();
            switch (command) {
                case "next" :
                    currentItem += page;
                    printPage();
                    break;
                case "prev" :
                    currentItem -= page;
                    currentPage -= 1;
                    printPage();
                break;
                case "exit" : break;
                default :
                    System.out.println("next, prev, exit");
                    break;
            }
        }
    }
}
