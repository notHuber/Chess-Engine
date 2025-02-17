import java.util.Scanner;
// using the BitBoards approach (Piece centric representation)
interface IBoardBitboard {
    void setFen(String fen);
}

public class ChessBoard implements IBoardBitboard {
    private long wPawn   = 0L;
    private long wRook   = 0L;
    private long wKnight = 0L;
    private long wBishop = 0L;
    private long wQueen  = 0L;
    private long wKing   = 0L;
    private long bPawn   = 0L;
    private long bRook   = 0L;
    private long bKnight = 0L;
    private long bBishop = 0L;
    private long bQueen  = 0L;;
    private long bKing   = 0L;

    private boolean wCastlingRights = true;
    private boolean bCastlingRights = true;
    private boolean fiftyMoveRule = false;


    //Constructor
    public ChessBoard(String fen){
        setFen(fen);
    }

    /*
    @param bitboard The piece's 64-bit representation
    @param rankIndex The actual piece's rank minus one
    @param fileIndex The actual piece's column minus one
    @return the updated bitboard
    */
    private long setBitboard(long bitboard, int rankIndex, int fileIndex){ // 0 up to 7 (not from 1 to 8!!)
        int bitPosition = 8*rankIndex + fileIndex; // Least Significant File mapping (LSF)
        bitboard |= 1L << bitPosition;
        return bitboard;
    }


    //Forsyth-Edwards Notation (FEN)
    //TODO: update this method to handle castling rights, enpassant moves, Moving privileges
    public void setFen(String fen){

        Scanner sc = new Scanner(fen).useDelimiter("/");
        String fenPiece;
        int tempRankIndex = 7, tempFileIndex;

        // O(n^2) complexity
        while(sc.hasNext() && tempRankIndex >= 0){

            fenPiece = sc.next();
            tempFileIndex = 0;

            for (int i = 0; i < fenPiece.length(); i++){

                char fenChar = fenPiece.charAt(i);

                if (Character.isDigit(fenChar)) {// empty squares
                    int emptySquares = Character.getNumericValue(fenChar);
                    tempFileIndex += emptySquares;
                }

                else{
                    switch (fenChar){
                        case 'P' :
                            wPawn = setBitboard(wPawn, tempRankIndex, tempFileIndex);
                            break;
                        case 'R':
                            wRook = setBitboard(wRook, tempRankIndex, tempFileIndex);
                            break;
                        case 'N' :
                            wKnight = setBitboard(wKnight, tempRankIndex, tempFileIndex);
                            break;
                        case 'B' :
                            wBishop = setBitboard(wBishop, tempRankIndex, tempFileIndex);
                            break;
                        case 'Q' :
                            wQueen = setBitboard(wQueen, tempRankIndex, tempFileIndex);
                            break;
                        case 'K' :
                            wKing = setBitboard(wKing, tempRankIndex, tempFileIndex);
                            break;
                        case 'p' :
                            bPawn = setBitboard(bPawn, tempRankIndex, tempFileIndex);
                            break;
                        case 'r':
                            bRook = setBitboard(bRook, tempRankIndex, tempFileIndex);
                            break;
                        case 'n' :
                            bKnight = setBitboard(bKnight, tempRankIndex, tempFileIndex);
                            break;
                        case 'b' :
                            bBishop = setBitboard(bBishop, tempRankIndex, tempFileIndex);
                            break;
                        case 'q' :
                            bQueen = setBitboard(bQueen, tempRankIndex, tempFileIndex);
                            break;
                        case 'k' :
                            bKing = setBitboard(bKing, tempRankIndex, tempFileIndex);
                            break;
                    }
                    tempFileIndex++;
                }
            }
            if(tempFileIndex != 8){ //WARNING: do not remove this line of code for now
                System.err.println("Error while handling the fen position\nRank index: " + tempRankIndex + "\nColumn Index:" + tempFileIndex + " 8 was expected");
            }
            tempRankIndex--;
        }
        sc.close();
    }




    //Debugging stuff

    /*
    Returns the bitboard formatted like a 64-bit binary number
    Useful for debugging, not that indispensable for the chess program itself.
    @return the formatted string
    */
    private String toBinaryString64(long bitboard){
        return String.format("%64s", Long.toBinaryString(bitboard)).replace(' ', '0');
    }

    /*
   Just some very helpful debug code (junk code)
*/
    public void printBitboards() {
        System.out.println("wPawn:   " + toBinaryString64(wPawn));
        System.out.println("wRook:   " + toBinaryString64(wRook));
        System.out.println("wKnight: " + toBinaryString64(wKnight));
        System.out.println("wBishop: " + toBinaryString64(wBishop));
        System.out.println("wQueen:  " + toBinaryString64(wQueen));
        System.out.println("wKing:   " + toBinaryString64(wKing));
        System.out.println("bPawn:   " + toBinaryString64(bPawn));
        System.out.println("bRook:   " + toBinaryString64(bRook));
        System.out.println("bKnight: " + toBinaryString64(bKnight));
        System.out.println("bBishop: " + toBinaryString64(bBishop));
        System.out.println("bQueen:  " + toBinaryString64(bQueen));
        System.out.println("bKing:   " + toBinaryString64(bKing));
        long bitboardOfAllPieces = wPawn | wRook | wKnight | wBishop | wQueen | wKing | bPawn | bRook | bKnight | bBishop | bQueen | bKing;
        System.out.println("final board \n" + Long.toBinaryString(bitboardOfAllPieces));
    }
}
