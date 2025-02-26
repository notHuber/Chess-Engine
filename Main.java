import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        //Scanner sc = new Scanner(new FileReader("positions.txt"));
        //while (sc.hasNextLine()){
        //    new ChessBoard(sc.nextLine()).createBoard();
        //}
        ChessBoard board = new ChessBoard("8/8/8/8/8/8/8/8 w - - 0 1");
        board.bPawn |= board.pawnAttack("e7",false);
        board.createBoard();
        //board.printBitboards();
        //board.getBoardInfo();
        //board.printBitboards();

    }
}


