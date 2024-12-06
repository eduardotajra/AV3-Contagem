import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter {
    public static void writeResult(String fileName, List<Result> results) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Algorithm,Count,Time(ms)\n"); // Cabeçalho
            for (Result result : results) {
                writer.write(result.toCSV() + "\n"); // Adicionar quebra de linha
            }
        }
    }
}
