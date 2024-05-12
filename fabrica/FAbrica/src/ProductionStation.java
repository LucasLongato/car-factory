import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProductionStation {
    // Atributos da estação produtora
    private int id;
    private List<Employee> employees;
    private BlockingQueue<Vehicle> vehicleQueue;
    private BlockingQueue<Piece> pieceQueue;
    private BlockingQueue<Boolean> agentQueue;

    // Construtor
    public ProductionStation(int id, BlockingQueue<Piece> pieceQueue, BlockingQueue<Boolean> agentQueue) {
        this.id = id;
        this.employees = new ArrayList<>();
        this.vehicleQueue = new ArrayBlockingQueue<>(40);
        this.pieceQueue = pieceQueue;
        this.agentQueue = agentQueue;
        for (int i = 0; i < 5; i++) {
            employees.add(new Employee(i));
        }
    }

    // Método para produzir um veículo
    public void produceVehicle() {
        // Simulação de produção de veículo
        try {
            agentQueue.take(); // Espera pela permissão do agente (saleiro)
            @SuppressWarnings("unused")
            Piece piece = pieceQueue.take(); // Pega uma peça
            Thread.sleep(new Random().nextInt(1000)); // Simula o tempo de produção

            // Gerar informações do carro
            String color = getRandomColor();
            String type = getRandomType();

            Vehicle vehicle = new Vehicle(color, type); // Utiliza o novo construtor com contador de veículos
            vehicleQueue.put(vehicle); // Coloca o veículo na fila da esteira
            System.out.println("Estação " + id + ": Carro produzido - ID: " + vehicle.getId() + ", Cor: " + vehicle.getColor() + ", Tipo: " + vehicle.getType());
            agentQueue.put(true); // Libera o agente (saleiro) após a produção

            // Adicione esta linha para registrar a produção do veículo
            Logger.logProduction(vehicle.getId(), vehicle.getColor(), vehicle.getType(), id, employees.get(0).getId(), vehicleQueue.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Método para gerar uma cor aleatória
    private String getRandomColor() {
        String[] colors = {"RED", "GREEN", "BLUE"};
        return colors[new Random().nextInt(colors.length)];
    }

    // Método para gerar um tipo aleatório (SUV ou SEDAN)
    private String getRandomType() {
        return new Random().nextBoolean() ? "SUV" : "SEDAN";
    }
}
