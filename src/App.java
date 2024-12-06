import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        String[] files = { "src/DonQuixote-388208.txt", "src/Dracula-165307.txt", "src/MobyDick-217452.txt" };
        String wordToSearch = "Gutenberg"; // Palavra a ser buscada
        int numThreads = 4; // Número de threads para ParallelCPU

        // Lista para armazenar os resultados
        List<Result> results = new ArrayList<>();

        for (String file : files) {
            System.out.println("\nTestando o txt: " + file + "\n");

            // Executar SerialCPU
            Result serialResult = SerialCPU.searchWord(file, wordToSearch);
            System.out.println(serialResult);
            results.add(serialResult);

            // Executar ParallelCPU
            Result parallelCpuResult = ParallelCPU.searchWord(file, wordToSearch, numThreads);
            System.out.println(parallelCpuResult);
            results.add(parallelCpuResult);

            // Executar ParallelGPU
            long startTimeGPU = System.nanoTime();
            int gpuCount = ParallelGPU.countWordOccurrences(file, wordToSearch);
            long endTimeGPU = System.nanoTime();

            Result gpuResult = new Result("ParallelGPU", gpuCount, (endTimeGPU - startTimeGPU) / 1_000_000);
            System.out.println(gpuResult);
            results.add(gpuResult);
        }

        // Salvar os resultados em um arquivo CSV
        CSVWriter.writeResult("src/performance_results.csv", results);
        System.out.println("Resultados salvos em src/performance_results.csv");

        // Exibir o gráfico no JFrame
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Performance dos Métodos");
            frame.setSize(1000, 700);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.add(new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);

                    // Configurações básicas
                    Graphics2D g2d = (Graphics2D) g;
                    int width = getWidth();
                    int height = getHeight();
                    int padding = 60;
                    int barWidth = (width - 2 * padding) / (results.size() * 2);
                    long maxTime = results.stream().mapToLong(Result::getTimeMs).max().orElse(1);

                    // Fundo branco
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(0, 0, width, height);

                    // Linhas de grade
                    g2d.setColor(Color.LIGHT_GRAY);
                    for (int i = 0; i <= 10; i++) {
                        int y = height - padding - i * (height - 2 * padding) / 10;
                        g2d.drawLine(padding, y, width - padding, y);
                        g2d.setColor(Color.DARK_GRAY);
                        g2d.drawString(String.valueOf(maxTime * i / 10), padding - 40, y + 5);
                        g2d.setColor(Color.LIGHT_GRAY);
                    }

                    // Desenhar eixos
                    g2d.setColor(Color.BLACK);
                    g2d.drawLine(padding, height - padding, width - padding, height - padding); // Eixo X
                    g2d.drawLine(padding, padding, padding, height - padding); // Eixo Y

                    // Desenhar as barras
                    int x = padding + 10;
                    for (Result result : results) {
                        int barHeight = (int) ((double) result.getTimeMs() / maxTime * (height - 2 * padding));
                        g2d.setColor(getColor(result.getAlgorithm()));
                        g2d.fillRect(x, height - padding - barHeight, barWidth, barHeight);
                        g2d.setColor(Color.BLACK);
                        g2d.drawString(result.getAlgorithm(), x, height - padding + 15);
                        g2d.drawString(result.getCount() + " ocorrências", x, height - padding + 30);
                        x += barWidth + 20;
                    }

                    // Título
                    g2d.setFont(new Font("Arial", Font.BOLD, 16));
                    g2d.drawString("Comparação de Tempo de Execução por Algoritmo", width / 3, padding / 2);

                    // Legenda
                    int legendX = width - 200;
                    int legendY = padding;
                    g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                    g2d.setColor(Color.BLUE);
                    g2d.fillRect(legendX, legendY, 20, 20);
                    g2d.setColor(Color.BLACK);
                    g2d.drawString("SerialCPU", legendX + 30, legendY + 15);

                    g2d.setColor(Color.GREEN);
                    g2d.fillRect(legendX, legendY + 30, 20, 20);
                    g2d.setColor(Color.BLACK);
                    g2d.drawString("ParallelCPU", legendX + 30, legendY + 45);

                    g2d.setColor(Color.RED);
                    g2d.fillRect(legendX, legendY + 60, 20, 20);
                    g2d.setColor(Color.BLACK);
                    g2d.drawString("ParallelGPU", legendX + 30, legendY + 75);
                }

                private Color getColor(String algorithm) {
                    return switch (algorithm) {
                        case "SerialCPU" -> Color.BLUE;
                        case "ParallelCPU" -> Color.GREEN;
                        case "ParallelGPU" -> Color.RED;
                        default -> Color.GRAY;
                    };
                }
            });

            frame.setVisible(true);
        });
    }
}
