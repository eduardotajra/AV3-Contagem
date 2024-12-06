public class Result {
    private String algorithm; // Nome do algoritmo executado (SerialCPU, ParallelCPU, etc.)
    private int count;        // Contagem da palavra
    private long timeMs;      // Tempo de execução em milissegundos

    // Construtor
    public Result(String algorithm, int count, long timeMs) {
        this.algorithm = algorithm;
        this.count = count;
        this.timeMs = timeMs;
    }

    // Getters (se necessário)
    public String getAlgorithm() {
        return algorithm;
    }

    public int getCount() {
        return count;
    }

    public long getTimeMs() {
        return timeMs;
    }

    // Método para exportar os dados como CSV
    public String toCSV() {
        return String.format("%s,%d,%d", algorithm, count, timeMs);
    }

    // Substituir toString() para exibir uma saída amigável
    @Override
    public String toString() {
        return String.format("Algoritmo: %s | Ocorrência: %d | Tempo: %d ms", algorithm, count, timeMs);
    }
}
