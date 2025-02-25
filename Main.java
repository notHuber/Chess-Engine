public class Main {
    public static void main(String[] args){
        String startingPositionFEN = "8/3k4/8/8/4K3/1R6/8/8 w - - 0 1";
        ChessBoard board = new ChessBoard(startingPositionFEN);
        board.printBitboards();
        board.getBoardInfo();
        board.printBitboards();
        board.createBoard();

    }
}


