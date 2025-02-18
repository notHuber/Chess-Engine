public class Main {
    public static void main(String[] args){
        String startingPositionFEN = "r1bqkbn1/1p1np1p1/p1p4r/4Pp1p/7P/2pP1N2/PPPBQPP1/3RKB1R w Kq f6 0 10";
        ChessBoard board = new ChessBoard(startingPositionFEN);
        board.printBitboards();
        board.getBoardInfo();
    }
}
