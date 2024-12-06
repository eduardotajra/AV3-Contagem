import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelCPU {
    public static Result searchWord(String filePath, String word, int numThreads) throws IOException {
        long startTime = System.nanoTime();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<Integer>> futures = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        
        for (String line : lines) {
            futures.add(executor.submit(() -> 
                (int) Arrays.stream(line.split("\\W+"))
                            .filter(w -> w.equalsIgnoreCase(word))
                            .count()));
        }
        
        int count = 0;
        for (Future<Integer> future : futures) {
            try {
                count += future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        long endTime = System.nanoTime();
        return new Result("ParallelCPU", count, (endTime - startTime) / 1_000_000);
    }
}
