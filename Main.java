import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        // Scanner sc = new Scanner(new FileReader("positions.txt"));
        // while (sc.hasNextLine()){
        // new ChessBoard(sc.nextLine()).createBoard();
        // }
        ChessBoard board = new ChessBoard("8/4P1q1/8/b1n1R3/8/4p3/4p3/8 w - - 0 1");
        board.wRook |= board.rookAttack("e5");
        board.createBoard();
        // board.printBitboards();
        // board.getBoardInfo();
    }
}
