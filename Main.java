import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Map<String, Integer> wordCountMap = new HashMap<>();
        try (Scanner reader = new Scanner(new File("C:\\Users\\omkar\\Downloads\\College_pitures\\input.txt"))) {
            System.out.println("-----------------");
            System.out.println("input file: ");
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                System.out.println(line);
                String[] words = line.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "").split("\\s+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            return;
        }
        System.out.println("-----------------");
        System.out.println("Word Counts: ");
        wordCountMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
        System.out.println("-----------------");
        System.out.println("Some analytics: ");
        int total_words = 0;
        for (String word : wordCountMap.keySet()) {
            total_words += wordCountMap.get(word);
        }
        System.out.println("Unique words: " + wordCountMap.size());
        System.out.println("Total words: " + total_words);
    }
}