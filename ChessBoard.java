import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class InvalidFenException extends RuntimeException {}

// using the BitBoards approach (Piece centric representation)
interface IBoardBitboard {
    int convertCordToSquarePosition(String coordinate);
    String convertSquarePositionToCord(int squareIndex);
}

public class ChessBoard implements IBoardBitboard {
    public long wPawn   = 0L;
    public long wRook   = 0L;
    public long wKnight = 0L;
    public long wBishop = 0L;
    public long wQueen  = 0L;
    public long wKing   = 0L;
    public long bPawn   = 0L;
    public long bRook   = 0L;
    public long bKnight = 0L;
    public long bBishop = 0L;
    public long bQueen  = 0L;
    public long bKing   = 0L;

    // Side to move
    private boolean whiteToMove;

    // castling rights
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


    private final int maxRank = 8;
    private final int maxFile = 8;
    private final int minRank = 1;
    private final int minFile = 1;
    /*
    Those function work with real life ranks and files (ranks & files starts at 1, end at 8)
    */
    private int getUpSpaceFromRank(int rank) {return maxRank - rank;}
    private int getDownSpaceFromRank(int rank) {return rank - minRank ;}
    private int getRightSpaceFromFile(int file) {return maxFile - file;}
    private int getLeftSpaceFromFile(int file) {return file - minFile;}



    public long rookAttack(int squarePosition){
        String coordinate = convertSquarePositionToCord(squarePosition);
        int file = Files.valueOf(coordinate.substring(0,1)).file;
        int rank = Integer.parseInt(coordinate.substring(1));

        int upSpace = getUpSpaceFromRank(rank);
        int downSpace = getDownSpaceFromRank(rank);
        int rightSpace = getRightSpaceFromFile(file);
        int leftSpace = getLeftSpaceFromFile(file);

        // store the pseudo legal moves of the rook moving piece
        long tempBitboard  = setBitboard(0L, rank - 1, file - 1);

        long upBitboard    = tempBitboard;
        long downBitboard  = tempBitboard;
        long rightBitboard = tempBitboard;
        long leftBitboard  = tempBitboard;


        for (int i = 0; i < upSpace; i++){upBitboard |= upBitboard << 8;}
        for (int i = 0; i < downSpace; i++){downBitboard = downBitboard | (downBitboard >>> 8);}
        for (int i = 0; i < rightSpace; i++){rightBitboard |= rightBitboard << 1;}
        for (int i = 0; i < leftSpace; i++){leftBitboard |= leftBitboard >> 1;}

        return tempBitboard ^ (downBitboard | upBitboard | leftBitboard | rightBitboard);
    }
    public long rookAttack(String coordinate){
        int squarePosition = convertCordToSquarePosition(coordinate);
        return rookAttack(squarePosition);
    }
    public long bishopAttack(int squarePosition){
        String coordinate = convertSquarePositionToCord(squarePosition);
        int file = Files.valueOf(coordinate.substring(0,1)).file;
        int rank = Integer.parseInt(coordinate.substring(1));

        int tempUpSpace,tempDownSpace, tempRightSpace ,tempLeftSpace;

        int tempSquarePosition;

        /*
        Attacking Bitboards for all directions
        */
        long downRigthBitboard = 0L;
        long downLeftBitboard  = 0L;
        long upRightBitboard   = 0L;
        long upLeftBitboard    = 0L;

        tempSquarePosition = squarePosition;
        tempDownSpace = getDownSpaceFromRank(rank);
        tempRightSpace = getRightSpaceFromFile(file);
        while (tempDownSpace-- > 0 && tempRightSpace-- > 0) {
            tempSquarePosition -= 7;
            downRigthBitboard |= 1L << tempSquarePosition;
        }

        tempSquarePosition = squarePosition;
        tempDownSpace = getDownSpaceFromRank(rank);
        tempLeftSpace = getLeftSpaceFromFile(file);
        while(tempDownSpace-- > 0 && tempLeftSpace-- > 0) {
            tempSquarePosition -= 9;
            downLeftBitboard |= 1L << tempSquarePosition;

        }

        tempSquarePosition = squarePosition;
        tempUpSpace = getUpSpaceFromRank(rank);
        tempRightSpace = getRightSpaceFromFile(file);
        while (tempUpSpace-- >0 && tempRightSpace-- > 0){
            tempSquarePosition += 9;
            upRightBitboard |= 1L << tempSquarePosition;
        }

        tempSquarePosition = squarePosition;
        tempUpSpace = getUpSpaceFromRank(rank);
        tempLeftSpace = getLeftSpaceFromFile(file);
        while (tempUpSpace-- >0 && tempLeftSpace-- > 0){
            tempSquarePosition += 7;
            upLeftBitboard |= 1L << tempSquarePosition;
        }

        //pseudo legal moves for the bishop moving piece
        return downLeftBitboard | downRigthBitboard | upLeftBitboard | upRightBitboard;
    }
    public long bishopAttack(String coordinate){
        int squarePosition = convertCordToSquarePosition(coordinate);
        return bishopAttack(squarePosition);
    }
    public long queenAttack(int squarePosition){
        return bishopAttack(squarePosition) | rookAttack(squarePosition);
    }
    public long queenAttack(String coordinate){
        return bishopAttack(coordinate) | rookAttack(coordinate);
    }
    public long kingAttack(int squarePosition){
        String coordinate = convertSquarePositionToCord(squarePosition);
        int file = Files.valueOf(coordinate.substring(0,1)).file;
        int rank = Integer.parseInt(coordinate.substring(1));
        int upSpace = getUpSpaceFromRank(rank);
        int downSpace = getDownSpaceFromRank(rank);
        int leftSpace = getLeftSpaceFromFile(file);
        int rightSpace = getRightSpaceFromFile(file);

        long upBitboard = 0b0L;
        long downBitboard = 0b0L;
        long leftBitboard = 0b0L;
        long rightBitboard = 0b0L;
        long upRigthB = 0b0L;
        long upLeftB = 0b0L;
        long downRightB= 0b0L;
        long downLeftB = 0b0L;

        if (upSpace > 0)
            upBitboard |= 1L << (squarePosition + 8);
        if (downSpace > 0)
            downBitboard |= 1L << (squarePosition - 8);
        if (leftSpace > 0){
            leftBitboard |= 1L << (squarePosition - 1);
            if (upSpace > 0) upLeftB |= 1L << (squarePosition + 7);
            if (downSpace >0) downLeftB |= 1L << (squarePosition - 9);
        }
        if (rightSpace > 0){
            rightBitboard |= 1L << (squarePosition + 1);
            if (upSpace > 0) upRigthB |= 1L << (squarePosition + 9);
            if (downSpace >0) downRightB |= 1L << (squarePosition -7);

        }

        return upBitboard | downBitboard | rightBitboard | leftBitboard | upLeftB | upRigthB | downLeftB | downRightB;
    }
    public long kingAttack(String coordinate){
        return kingAttack(convertCordToSquarePosition(coordinate));
    }
    public long knightAttack(int squarePosition){
        String coordinate = convertSquarePositionToCord(squarePosition);
        int file = Files.valueOf(coordinate.substring(0,1)).file;
        int rank = Integer.parseInt(coordinate.substring(1));
        int upSpace = getUpSpaceFromRank(rank);
        int downSpace = getDownSpaceFromRank(rank);
        int leftSpace = getLeftSpaceFromFile(file);
        int rightSpace = getRightSpaceFromFile(file);


        long upRightB1 = 0L;
        long upRightB2 = 0L;
        long upLeftB1 = 0L;
        long upLeftB2 = 0L;
        long downRightB1 = 0L;
        long downRightB2 = 0L;
        long downLeftB1 = 0L;
        long downLeftB2 = 0L;

        if (rightSpace > 1){
            if (upSpace > 0) upRightB1 |= 1L << (squarePosition + 10);
            if (downSpace > 0) downRightB1 |= 1L << (squarePosition -6);

        }
        if (rightSpace > 0){
            if (upSpace > 1) upRightB2 |= 1L << (squarePosition + 17);
            if (downSpace > 1) downRightB2 |= 1L << (squarePosition -15);
        }
        if (leftSpace > 1){
            if (upSpace > 0) upLeftB1 |= 1L << (squarePosition + 6);
            if (downSpace > 0) downLeftB1 |= 1L << (squarePosition -10);

        }
        if (leftSpace > 0){
            if (upSpace > 1) upLeftB2 |= 1L << (squarePosition + 15);
            if (downSpace > 1) downLeftB2 |= 1L << (squarePosition -17);
        }

        return upLeftB1 | upLeftB2 |upRightB1 | upRightB2 | downLeftB1 | downLeftB2 | downRightB1 | downRightB2;
    }
    public long knightAttack(String coordinate){
        return knightAttack(convertCordToSquarePosition(coordinate));
    }
    public long pawnAttack(int squarePosition, boolean movingForward){
        String coordinate = convertSquarePositionToCord(squarePosition);
        int file = Files.valueOf(coordinate.substring(0,1)).file;
        int rank = Integer.parseInt(coordinate.substring(1));
        long attackingPawnBitboard = 0L;
        if (movingForward){
            if (rank == 2) {
                attackingPawnBitboard = 1L << (squarePosition + 8);
                attackingPawnBitboard |= 1L << (squarePosition + 16);
            }
            else attackingPawnBitboard = 1L << (squarePosition + 8);
        }
        if (!movingForward){
            if (rank == 7) {
                attackingPawnBitboard = 1L << (squarePosition - 8);
                attackingPawnBitboard |= 1L << (squarePosition - 16);
            }
            else attackingPawnBitboard = 1L << (squarePosition - 8);
        }
        return attackingPawnBitboard;
    }
    public long pawnAttack(String coordinate, boolean movingForward){
        return pawnAttack(convertCordToSquarePosition(coordinate),movingForward);
    }
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

    // Constructor
    public ChessBoard(String fen) {
        try {
            setFen(fen);
        } catch (Exception e) {
            throw new InvalidFenException();
        }

    }

    /*
     * @param bitboard The piece's 64-bit representation
     * 
     * @param rankIndex The actual piece's rank minus one
     * 
     * @param fileIndex The actual piece's column minus one
     * 
     * @return the updated bitboard
     */
    private long setBitboard(long bitboard, int rankIndex, int fileIndex) { // 0 up to 7 (not from 1 to 8!!)
        int bitPosition = lsf(rankIndex, fileIndex); // Least Significant File mapping (LSF)
        bitboard |= 1L << bitPosition;
        return bitboard;
    }

    private int lsf(int rank, int file) {
        return (8 * rank) + file;
    }


    // Forsyth-Edwards Notation (FEN)
    // https://www.chessprogramming.org/Forsyth-Edwards_Notation
    private void setFen(String fen) {

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
         * PIECE PLACEMENT (saving pieces position to the corresponding bitboard)
         * O(n^2) complexity
         */
        int tempRankIndex = 7, tempFileIndex;
        while (piecePlacement.hasNext() && tempRankIndex >= 0) {

            singleRankPiecePlacement = piecePlacement.next();
            tempFileIndex = 0;

            for (int i = 0; i < singleRankPiecePlacement.length(); i++) {

                char fenChar = singleRankPiecePlacement.charAt(i);

                if (Character.isDigit(fenChar)) {// empty squares
                    int emptySquares = Character.getNumericValue(fenChar);
                    tempFileIndex += emptySquares;
                }

                else {
                    switch (fenChar) {
                        case 'P':
                            wPawn = setBitboard(wPawn, tempRankIndex, tempFileIndex);
                            break;
                        case 'R':
                            wRook = setBitboard(wRook, tempRankIndex, tempFileIndex);
                            break;
                        case 'N':
                            wKnight = setBitboard(wKnight, tempRankIndex, tempFileIndex);
                            break;
                        case 'B':
                            wBishop = setBitboard(wBishop, tempRankIndex, tempFileIndex);
                            break;
                        case 'Q':
                            wQueen = setBitboard(wQueen, tempRankIndex, tempFileIndex);
                            break;
                        case 'K':
                            wKing = setBitboard(wKing, tempRankIndex, tempFileIndex);
                            break;
                        case 'p':
                            bPawn = setBitboard(bPawn, tempRankIndex, tempFileIndex);
                            break;
                        case 'r':
                            bRook = setBitboard(bRook, tempRankIndex, tempFileIndex);
                            break;
                        case 'n':
                            bKnight = setBitboard(bKnight, tempRankIndex, tempFileIndex);
                            break;
                        case 'b':
                            bBishop = setBitboard(bBishop, tempRankIndex, tempFileIndex);
                            break;
                        case 'q':
                            bQueen = setBitboard(bQueen, tempRankIndex, tempFileIndex);
                            break;
                        case 'k':
                            bKing = setBitboard(bKing, tempRankIndex, tempFileIndex);
                            break;
                    }
                    tempFileIndex++;
                }
            }
            if (tempFileIndex != 8) { // WARNING: do not remove this line of code for now
                System.err.println("Error while handling the fen position\nRank index: " + tempRankIndex
                        + "\nColumn Index:" + tempFileIndex + " 8 was expected");
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
        if (enPassantTargetSquare.equals("-"))
            enPassantCaptureSquare = -1;
        else
            enPassantCaptureSquare = convertCordToSquarePosition(enPassantTargetSquare);

        // HALFMOVE CLOCK & FULLMOVE CLOCK
        halfMoveClock = Integer.parseInt(halfMoveClockSTR);
        fullMoveClock = Integer.parseInt(fullMoveClockSTR);
    }

    // ex. "h8" --> 63
    public int convertCordToSquarePosition(String coordinate) {
        String columnStr = coordinate.substring(0, 1);
        int file = Files.valueOf(columnStr).file - 1; // because file goes from 0 to 7, not from 1 to 8

        int rank = Integer.parseInt(coordinate.substring(1)) - 1;
        return lsf(rank, file);
    }

    // ex. 63 --> "h8"
    public String convertSquarePositionToCord(int squareIndex) {
        String coordinate = "";
        int file = squareIndex % 8 + 1;
        int rank = squareIndex / 8 + 1;

        for (Files c : Files.values()) {
            if (c.file != file)
                continue;
            coordinate = c.name();
            break;
        }
        return coordinate + rank;
    }

    // ex. 0000000000000000000000000000000000000000000000001111111100000000 --> "[48, 49, 50, 51, 52, 53, 54, 55]"
    public List<Integer> convertBitboardToSquareIndexArray(String bitboard) {
        //TODO : update this code with something more useful and more reliable
        bitboard = getReadableBitboard(bitboard);
        List<Integer> squareIndices = new ArrayList<>();
        for (int i = 0; i < bitboard.length(); i++) {
            if (bitboard.charAt(i) == '1') {
                squareIndices.add(i); 
            }
        }
        return squareIndices;
    }
    // TODO: create a new class with static methods (getReverse even though it's for a temporary fix, it shouldn't be here)
    public String getReverse(String str){
        String res = "";
        for (int i = str.length() -1; i >= 0; i--) {
            res += str.charAt(i);
        }
        return res;
    }

    public String getReadableBitboard(String bitboard){ return
            getReverse(bitboard.substring(0,8)) + getReverse(bitboard.substring(8,16))  +
            getReverse(bitboard.substring(16,24)) + getReverse(bitboard.substring(24,32)) +
            getReverse(bitboard.substring(32,40)) + getReverse(bitboard.substring(40,48)) +
            getReverse(bitboard.substring(48,56)) + getReverse(bitboard.substring(56,64));}

    // TODO: reduce dependencies
    public void createBoard() {
        GUI gui = new GUI();
        List<Integer> wPawnSquares = convertBitboardToSquareIndexArray(toBinaryString64(wPawn));
        List<Integer> wKnightSquares = convertBitboardToSquareIndexArray(toBinaryString64(wKnight));
        List<Integer> wBishopSquares = convertBitboardToSquareIndexArray(toBinaryString64(wBishop));
        List<Integer> wQueenSquares = convertBitboardToSquareIndexArray(toBinaryString64(wQueen));
        List<Integer> wRookSquares = convertBitboardToSquareIndexArray(toBinaryString64(wRook));
        List<Integer> wKingSquares = convertBitboardToSquareIndexArray(toBinaryString64(wKing));
        List<Integer> bPawnSquares = convertBitboardToSquareIndexArray(toBinaryString64(bPawn));
        List<Integer> bKnightSquares = convertBitboardToSquareIndexArray(toBinaryString64(bKnight));
        List<Integer> bBishopSquares = convertBitboardToSquareIndexArray(toBinaryString64(bBishop));
        List<Integer> bQueenSquares = convertBitboardToSquareIndexArray(toBinaryString64(bQueen));
        List<Integer> bRookSquares = convertBitboardToSquareIndexArray(toBinaryString64(bRook));
        List<Integer> bKingSquares = convertBitboardToSquareIndexArray(toBinaryString64(bKing));

        for (int squareIndex : wRookSquares) gui.setPiece(squareIndex, "wr");
        for (int squareIndex : wPawnSquares) gui.setPiece(squareIndex, "wp");
        for (int squareIndex : wKingSquares) gui.setPiece(squareIndex, "wk");
        for (int squareIndex : wQueenSquares) gui.setPiece(squareIndex, "wq");
        for (int squareIndex : wBishopSquares) gui.setPiece(squareIndex, "wb");
        for (int squareIndex : wKnightSquares) gui.setPiece(squareIndex, "wn");

        for (int squareIndex : bRookSquares) gui.setPiece(squareIndex, "br");
        for (int squareIndex : bPawnSquares) gui.setPiece(squareIndex, "bp");
        for (int squareIndex : bKingSquares) gui.setPiece(squareIndex, "bk");
        for (int squareIndex : bQueenSquares) gui.setPiece(squareIndex, "bq");
        for (int squareIndex : bBishopSquares) gui.setPiece(squareIndex, "bb");
        for (int squareIndex : bKnightSquares) gui.setPiece(squareIndex, "bn");
    }




    // Debugging stuff (under this line of code ONLY debugging stuff)
    public void getBoardInfo() {
        System.out.println("Is white to move : " + whiteToMove);
        System.out.println("EnPassantSquareIndex (number, not coordinate) : " + enPassantCaptureSquare);
        System.out.println("Castling Rights" +
                "\n Queen side castling (white): " + wCastlingQueenRights +
                "\n King  side castling (white): " + wCastlingKingRights +
                "\n Queen side castling (black): " + bCastlingQueenRights +
                "\n King  side castling (black): " + bCastlingKingRights);
        System.out.println("Halfmove clock: " + halfMoveClock);
        System.out.println("Fullfmove clock: " + fullMoveClock);
    }

    /*
     * Returns the bitboard formatted like a 64-bit binary number
     * Useful for debugging, not that indispensable for the chess program itself.
     * 
     * @return the formatted string
     */
    private String toBinaryString64(long bitboard) {
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
