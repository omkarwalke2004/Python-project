import java.io.*;
import java.util.Arrays;

public class Temp {
    public static void main(String[] args) {
        String filePath = "temp.txt";
        int tempSum = 0, dewPointSum = 0, windSpeedSum = 0;
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip the header line
            if (line == null) {
                System.out.println("File is empty.");
                return;
            }
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue; // Skip empty lines
                String[] parts = line.trim().split("\\s+");
                System.out.println(Arrays.toString(parts));
                if (parts.length < 4) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }
                int temp = Integer.parseInt(parts[1]);
                int dewPoint = Integer.parseInt(parts[2]);
                int windSpeed = Integer.parseInt(parts[3]);
                tempSum += temp;
                dewPointSum += dewPoint;
                windSpeedSum += windSpeed;
                count++;
            }
            if (count > 0) {
                System.out.println("Average Temperature: " + (tempSum / (double) count) + " °C");
                System.out.println("Average Dew Point: " + (dewPointSum / (double) count) + " °C");
                System.out.println("Average Wind Speed: " + (windSpeedSum / (double) count) + " km/h");
            } else {
                System.out.println("No valid data found.");
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number in file: " + e.getMessage());
        }
    }
}