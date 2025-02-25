public class Main {
    public static void main(String[] args){
        String startingPositionFEN = "r1b2rk1/ppp1nppp/3p2q1/2b1n3/3NP3/2P1BP2/PP1NB1PP/R2Q1RK1 b - - 2 11";
        ChessBoard board = new ChessBoard(startingPositionFEN);
        //board.printBitboards();
        //board.getBoardInfo();
        //board.printBitboards();
        board.createBoard();
    }
}


