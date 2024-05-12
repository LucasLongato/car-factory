import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Logger {
    private static final String LOG_FILE_PATH = "production_log.txt";
    private static final BlockingQueue<String> logQueue = new ArrayBlockingQueue<>(1000);

    // Inicializa o thread para gravar os logs
    static {
        Thread logWriterThread = new Thread(Logger::writeLogs);
        logWriterThread.setDaemon(true);
        logWriterThread.start();
    }

    // Método para registrar a produção de um veículo
    public static void logProduction(String vehicleId, String vehicleColor, String vehicleType, int stationId, int employeeId, int position) {
        String logEntry = String.format("Vehicle ID: %s, Color: %s, Type: %s, Station ID: %d, Employee ID: %d, Position: %d%n",
                vehicleId, vehicleColor, vehicleType, stationId, employeeId, position);
        addToLogQueue(logEntry);
    }

    // Método para registrar a venda de um veículo
    public static void logSale(String vehicleId, String vehicleColor, String vehicleType, int stationId, int employeeId, int position, String store, int storePosition) {
        String logEntry = String.format("Vehicle ID: %s, Color: %s, Type: %s, Station ID: %d, Employee ID: %d, Position: %d, Sold to: %s, Store Position: %d%n",
                vehicleId, vehicleColor, vehicleType, stationId, employeeId, position, store, storePosition);
        addToLogQueue(logEntry);
    }

    // Adiciona a entrada de log à fila de logs
    private static void addToLogQueue(String logEntry) {
        try {
            logQueue.put(logEntry);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Thread que escreve os logs no arquivo
    private static void writeLogs() {
        try {
            // Verifica se o arquivo de log existe, caso contrário, cria um novo
            Path filePath = Paths.get(LOG_FILE_PATH);
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            // Escreve continuamente os logs no arquivo
            try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE_PATH, true))) {
                while (true) {
                    String logEntry = logQueue.take(); // Aguarda uma entrada de log na fila
                    writer.println(logEntry);
                    writer.flush(); // Certifica-se de que os logs são gravados imediatamente
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
