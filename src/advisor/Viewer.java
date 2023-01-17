package advisor;

import java.util.ArrayList;
import java.util.Arrays;

class Viewer {

    private int page;
    private int currentPage = 1;

    private int currentItem = 1;
    private final ArrayList<ArrayList<String>> items = new ArrayList<>();

    Viewer(int page) {
        this.page = page;
    }

    public void readData(String... values) {
        ArrayList<String> item = new ArrayList<>(Arrays.asList(values));
        items.add(item);
    }

    public void startView() {
        printPage();
        makeView();
    }

    private void printPage() {

        if (currentItem <= items.size() && currentItem > 0) {
            int limit = Math.min(currentPage * page, items.size());
            for (int i = currentItem - 1; i <= limit - 1; i++) {
                for (String str : items.get(i)) {
                    System.out.println(str);
                }
                if (items.get(i).size() > 1) {
                    System.out.println();
                }
            }
            System.out.printf("---PAGE %d OF %d---%n", currentPage, page == 1 ? items.size() : items.size() / page + 1);
        } else if (currentItem > 1) {
            currentItem -= page;
            currentPage--;
            printPage();
        } else {
            System.out.println("No more pages");
        }

    }

    private void makeView() {
        String command = null;
        while (!"exit".equals(command)) {
            command = Engine.input();
            switch (command) {
                case "next" :
                    currentItem += page;
                    currentPage++;
                    if ((page == 1 ? items.size() : items.size() / page + 1) < currentPage) {
                        System.out.println("No more pages");
                        currentItem -= page;
                        currentPage--;
                    } else {
                        printPage();
                    }
                    break;
                case "prev" :
                    currentItem -= page;
                    currentPage--;
                    if(currentPage > 0) {
                        printPage();
                    } else {
                        System.out.println("No more pages");
                        currentItem += page;
                        currentPage++;
                    }
                break;
                case "exit" : break;
                default :
                    System.out.println("next, prev, exit");
                    break;
            }
        }
    }
}
