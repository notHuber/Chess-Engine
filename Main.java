public class Main {
    public static void main(String[] args){
        String startingPositionFEN = "r1bqkbnr/ppp1pppp/2n5/3P4/8/8/PPPP1PPP/RNBQKBNR w KQkq - 1 3";
        ChessBoard board = new ChessBoard(startingPositionFEN);
        board.printBitboards();
        board.getBoardInfo();
        board.createBoard();
    }
}
