package regextodfa;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<List<String>> bigList = new ArrayList<>();

        // Create and populate the sublists
        for (int i = 0; i < 10; i++) {
            List<String> sublist = new ArrayList<>();
            sublist.add("Element " + i + "A");
            sublist.add("Element " + i + "B");
            bigList.add(sublist);
        }

        // Access and print the values using a for loop
        for (int i = 0; i < bigList.size(); i++) {
            List<String> sublist = bigList.get(i);
            String firstElement = sublist.get(0);
            String secondElement = sublist.get(1);
            System.out.println("Sublist " + i + ":");
            System.out.println("First Element: " + firstElement);
            System.out.println("Second Element: " + secondElement);
            System.out.println();
        }
    }
}
