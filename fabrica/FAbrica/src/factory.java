import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class factory {
    // Atributos da fábrica
    private List<ProductionStation> stations;
    private BlockingQueue<Piece> pieceQueue;
    private BlockingQueue<Boolean> agentQueue;
    private int totalCarsProduced; // Contador de carros produzidos
    @SuppressWarnings("unused")
    private int pieceIdCounter; // Contador para ID da peça

    // Construtor
    public factory() {
        this.stations = new ArrayList<>();
        this.pieceQueue = new ArrayBlockingQueue<>(500);
        this.agentQueue = new ArrayBlockingQueue<>(1); // O agente (saleiro) controla o acesso aos recursos
        this.totalCarsProduced = 0; // Inicializa o contador de carros produzidos
        this.pieceIdCounter = 1; // Inicializa o contador do ID da peça
        try {
            agentQueue.put(true); // Inicialmente, o agente está disponível
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 4; i++) {
            stations.add(new ProductionStation(i, pieceQueue, agentQueue));
        }
    }

    // Método para iniciar a produção
    public void startProduction() {
        // Simula a produção contínua de peças
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(200); // Simula o tempo de produção de peça
                    Piece piece = new Piece("Peça " + Piece.getNextPieceId()); // Incrementa e usa o contador do ID da peça
                    pieceQueue.put(piece); // Coloca a peça na fila da esteira
                    System.out.println("Peça produzida - " + piece.getName());
                    System.out.println("Quantidade de peças disponíveis no estoque: " + pieceQueue.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // Inicia a produção de veículos em cada estação produtora
        for (ProductionStation station : stations) {
            new Thread(() -> {
                while (true) {
                    station.produceVehicle(); // Produz um veículo na estação
                    synchronized (this) { // Sincroniza o acesso ao contador para evitar condições de corrida
                        totalCarsProduced++; // Incrementa o contador de carros produzidos
                        System.out.println("Total de carros produzidos: " + totalCarsProduced);
                    }
                }
            }).start();
        }

    }

    // Método para executar o agente (saleiro)
    public void runAgent() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000); // Verifica a cada segundo
                    if (pieceQueue.size() >= 4) { // Se houver peças suficientes
                        agentQueue.take(); // Remove o agente da fila para permitir a produção
                        agentQueue.put(true); // Coloca o agente de volta após a produção

                        // Adicione estas linhas para registrar a produção e venda dos veículos
                        Logger.logProduction("Produção autorizada pelo agente", "", "", 0, 0, pieceQueue.size());
                        Logger.logSale("Reinício da produção após autorização do agente", "", "", 0, 0, pieceQueue.size(), "", 0);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
