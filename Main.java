import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        // Scanner sc = new Scanner(new FileReader("positions.txt"));
        // while (sc.hasNextLine()){
        // new ChessBoard(sc.nextLine()).createBoard();
        // }
        ChessBoard board = new ChessBoard("n7/8/8/p5k1/8/8/8/8 w KQkq - 0 1");
        board.wRook |= board.rookAttack("d5");
        board.createBoard();
        // board.printBitboards();
        // board.getBoardInfo();
        board.printBitboards();
    }
}
