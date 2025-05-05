import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class MapReduceLogProcessor {
    // Mapper function to extract log levels from log lines
    public static List<String> map(String line) {
        List<String> result = new ArrayList<>();
        String[] parts = line.split(" ");

        if (parts.length > 2) {
            String logLevel = parts[2]; // Assuming log level is the 3rd word
            result.add(logLevel);
        }

        return result;
    }

    // Reducer function to count occurrences of log levels
    public static Map<String, Integer> reduce(List<String> logLevels) {
        Map<String, Integer> result = new HashMap<>();
        for (String logLevel : logLevels) {
            result.put(logLevel, result.getOrDefault(logLevel, 0) + 1);
        }
        return result;
    }

    // Process log file with Map and Reduce
    public static void processLogFile(String filePath, int numThreads) throws IOException,
            InterruptedException, ExecutionException {
        // Start measuring time
        long startTime = System.currentTimeMillis();
        // Step 1: Read the log file
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        List<String> logLines = reader.lines().collect(Collectors.toList());
        reader.close();
        // Step 2: Create thread pool and parallelize Map phase
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Callable<List<String>>> tasks = new ArrayList<>();
        for (String line : logLines) {
            tasks.add(() -> map(line)); // Submit mapping tasks
        }
        List<Future<List<String>>> futures = executor.invokeAll(tasks);
        // Step 3: Collect the results from the Map phase
        List<String> mappedResults = new ArrayList<>();
        for (Future<List<String>> future : futures) {
            mappedResults.addAll(future.get());
        }
        // Step 4: Reduce phase
        Map<String, Integer> logCounts = reduce(mappedResults);
        // Step 5: Print the result
        System.out.println("Log Level Counts:");
        for (Map.Entry<String, Integer> entry : logCounts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        // Step 6: Shutdown the executor service
        executor.shutdown();
        // End measuring time
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        // Output the current execution time
        System.out.println("Execution Time for Current Run: " + elapsedTime + " milliseconds");
        // Step 7: Read previous execution time (if any)
        long previousExecutionTime = readPreviousExecutionTime();
        if (previousExecutionTime != -1) {
            System.out.println("Previous Execution Time: " + previousExecutionTime + " milliseconds");
            // Compare the times (e.g., is the current run faster or slower?)
            if (elapsedTime < previousExecutionTime) {
                System.out.println("The current run was faster than the previous one.");
            } else if (elapsedTime > previousExecutionTime) {
                System.out.println("The current run was slower than the previous one.");
            } else {
                System.out.println("The current run took the same time as the previous one.");
            }
        } else {
            System.out.println("This is the first run, no previous execution time to compare.");
        }
        // Step 8: Save the current execution time for next time
        saveExecutionTime(elapsedTime);
    }

    // Method to read the previous execution time from a file
    public static long readPreviousExecutionTime() {
        try {
            File file = new File("execution_time.txt");
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String previousTime = reader.readLine();
                reader.close();
                return Long.parseLong(previousTime);
            } else {
                return -1; // No previous time, first run
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Method to save the current execution time to a file
    public static void saveExecutionTime(long time) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("execution_time.txt"));
            writer.write(String.valueOf(time));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            String logFilePath = "system.txt"; // Path to your log file
            processLogFile(logFilePath, 4); // Use 4 threads for parallel processing
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}