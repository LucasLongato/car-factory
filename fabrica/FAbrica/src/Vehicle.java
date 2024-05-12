public class Vehicle {
    // Atributos do veículo
    private static int vehicleCounter = 1; // Contador de veículos
    private String id;
    private String color;
    private String type;

    // Construtor
    public Vehicle(String color, String type) {
        this.id = ""+vehicleCounter++; // Atribui o ID usando o contador e incrementa
        this.color = color;
        this.type = type;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public String getType() {
        return type;
    }
}
