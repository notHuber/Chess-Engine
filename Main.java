public class Main {
    public static void main(String[] args){
        ChessBoard board = new ChessBoard("r1bk3r/p2pBpNp/n4n2/1p1NP2P/6P1/3P4/P1P1K3/q5b1");
        board.printBitboards();
        /*
        * excepted  output for the bitboard with all the chess pieces
        * (with pieces I include : pawns,bishops,knights,queens,kings,rooks)
        * -> 1000110111111001001000011001101001000000000010000001010101000001
        */
    }
}
