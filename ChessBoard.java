import java.util.Scanner;
class InvalidFenException extends RuntimeException {}
// using the BitBoards approach (Piece centric representation)
interface IBoardBitboard {
    int convertCordToSquarePosition(String coordinate);
    String convertSquarePositionToCord(int squareIndex);
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
    private long bQueen  = 0L;
    private long bKing   = 0L;

    //Side to move
    private boolean whiteToMove;

    //castling rights
    private boolean wCastlingKingRights;
    private boolean wCastlingQueenRights;
    private boolean bCastlingKingRights;
    private boolean bCastlingQueenRights;

    // 50 move rule, starts at 0
    private int halfMoveClock;

    // Move counter (for each black move), starts at 1
    private int fullMoveClock;

    // En passant capture square
    int enPassantCaptureSquare;

    public enum Files {
        a(1),
        b(2),
        c(3),
        d(4),
        e(5),
        f(6),
        g(7),
        h(8);

        public final int file;
        Files(int column) {
            this.file = column;
        }
    }

    //Constructor
    public ChessBoard(String fen){
        try{
            setFen(fen);
        }catch (Exception e){
            throw new InvalidFenException();
        }

    }

    /*
    @param bitboard The piece's 64-bit representation
    @param rankIndex The actual piece's rank minus one
    @param fileIndex The actual piece's column minus one
    @return the updated bitboard
    */
    private long setBitboard(long bitboard, int rankIndex, int fileIndex){ // 0 up to 7 (not from 1 to 8!!)
        int bitPosition = lsf(rankIndex, fileIndex); // Least Significant File mapping (LSF)
        bitboard |= 1L << bitPosition;
        return bitboard;
    }

    private int lsf(int rank, int file) {return 8*rank + file;}

    //Forsyth-Edwards Notation (FEN)
    //https://www.chessprogramming.org/Forsyth-Edwards_Notation
    private void setFen(String fen){

        Scanner tokens = new Scanner(fen);
        Scanner piecePlacement = new Scanner(tokens.next()).useDelimiter("/");
        String singleRankPiecePlacement;

        String sideToMove = tokens.next();
        String castlingAbility = tokens.next();
        String enPassantTargetSquare = tokens.next();
        String halfMoveClockSTR = tokens.next();
        String fullMoveClockSTR = tokens.next();
        tokens.close();


        /*
            PIECE PLACEMENT (saving pieces position to the corresponding bitboard)
            O(n^2) complexity
        */
        int tempRankIndex = 7, tempFileIndex;
        while(piecePlacement.hasNext() && tempRankIndex >= 0){

            singleRankPiecePlacement = piecePlacement.next();
            tempFileIndex = 0;

            for (int i = 0; i < singleRankPiecePlacement.length(); i++){

                char fenChar = singleRankPiecePlacement.charAt(i);

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
        piecePlacement.close();


        // SIDE TO MOVE
        whiteToMove = sideToMove.equalsIgnoreCase("w");


        // CASTLING ABILITY
        wCastlingKingRights = castlingAbility.contains("K");
        wCastlingQueenRights = castlingAbility.contains("Q");
        bCastlingKingRights = castlingAbility.contains("k");
        bCastlingQueenRights = castlingAbility.contains("q");


        // EN PASSANT TARGET SQUARE
        if (enPassantTargetSquare.equals("-")) enPassantCaptureSquare = -1;
        else enPassantCaptureSquare = convertCordToSquarePosition(enPassantTargetSquare);

        // HALFMOVE CLOCK & FULLMOVE CLOCK
        halfMoveClock = Integer.parseInt(halfMoveClockSTR);
        fullMoveClock = Integer.parseInt(fullMoveClockSTR);
    }

    // ex. "h8" --> 63
    public int convertCordToSquarePosition(String coordinate){
        String columnStr = coordinate.substring(0,1);
        int file = Files.valueOf(columnStr).file - 1; //because file goes from 0 to 7, not from 1 to 8

        int rank = Integer.parseInt(coordinate.substring(1)) - 1;
        return lsf(rank,file);
    }

    // ex. 63 --> "h8"
    public String convertSquarePositionToCord(int squareIndex){
        String coordinate = "";
        int file = squareIndex % 8 + 1;
        int rank = squareIndex /8 + 1;

        for (Files c : Files.values()){
            if (c.file != file) continue;
            coordinate = c.name();
            break;
        }
        return coordinate + rank;
    }


    //Debugging stuff
    public void getBoardInfo(){
        System.out.println("Is white to move : " + whiteToMove);
        System.out.println("EnPassantSquareIndex (number, not coordinate) : " + enPassantCaptureSquare);
        System.out.println("Castling Rights" +
                "\n Queen side castling (white): " + wCastlingQueenRights +
                "\n King  side castling (white): " + wCastlingKingRights +
                "\n Queen side castling (black): " + bCastlingQueenRights+
                "\n King  side castling (black): " + bCastlingKingRights);
        System.out.println("Halfmove clock: " + halfMoveClock);
        System.out.println("Fullfmove clock: " + fullMoveClock);
    }
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
        System.out.println("Bitboard with every piece: " + Long.toBinaryString(bitboardOfAllPieces));
    }
}
