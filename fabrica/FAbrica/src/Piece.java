import java.util.concurrent.atomic.AtomicInteger;

public class Piece {
    // Atributos da peça
    private String name;
    private static AtomicInteger pieceCounter = new AtomicInteger(1); // Contador de peças

    // Construtor
    public Piece(String name) {
        this.name = name;
    }

    // Getter
    public String getName() {
        return name;
    }

    // Método estático para obter o próximo ID de peça
    public static int getNextPieceId() {
        return pieceCounter.getAndIncrement();
    }
}
