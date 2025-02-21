public class Main {
    public static void main(String[] args){
        String startingPositionFEN = "r1bqkbnr/ppnp1p1p/8/4p3/3P4/5N2/PPP1PPPP/RNBQKB1R w KQkq - 0 1";
        ChessBoard board = new ChessBoard(startingPositionFEN);
        board.printBitboards();
        board.getBoardInfo();
        board.createBoard();
    }
}
