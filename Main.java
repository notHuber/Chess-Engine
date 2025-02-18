public class Main {
    public static void main(String[] args){
        String startingPositionFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        ChessBoard board = new ChessBoard(startingPositionFEN);
        board.printBitboards();
        board.getBoardInfo();
    }
}
