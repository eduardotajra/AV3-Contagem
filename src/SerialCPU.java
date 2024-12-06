import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class SerialCPU {
    public static Result searchWord(String filePath, String word) throws IOException {
        long startTime = System.nanoTime();
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                count += Arrays.stream(line.split("\\W+"))
                               .filter(w -> w.equalsIgnoreCase(word))
                               .count();
            }
        }
        long endTime = System.nanoTime();
        return new Result("SerialCPU", count, (endTime - startTime) / 1_000_000);
    }
}
