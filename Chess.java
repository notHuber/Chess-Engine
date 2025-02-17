class Chess {
    public long whitePawns;
    public long whiteKnights;
    public long whiteBishops;
    public long whiteRooks;
    public long whiteQueens;
    public long whiteKing;

    public long blackPawns;
    public long blackKnights;
    public long blackBishops;
    public long blackRooks;
    public long blackQueens;
    public long blackKing;

    public String fen;

    public Chess(String fen) {
        this.fen = fen;
    }

    // rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR
    public void chessBoard() {
        String[] board = new String[64];
        int index = 0;

        for (int i = 0; i < this.fen.length(); i++) {
            char c = fen.charAt(i);

            if (Character.isLowerCase(c)) { // black pieces
                if (c == 'r') {
                    System.out.println("Pos torre nero: " + index);
                    board[index++] = "r";

                } else if (c == 'n') {
                    System.out.println("Pos cavallo nero: " + index);
                    board[index++] = "n";

                } else if (c == 'b') {
                    System.out.println("Pos alfiere nero: " + index);
                    board[index++] = "b";

                } else if (c == 'q') {
                    System.out.println("Pos regina nero: " + index);
                    board[index++] = "q";

                } else if (c == 'k') {
                    System.out.println("Pos re nero: " + index);
                    board[index++] = "k";

                } else if (c == 'p') {
                    System.out.println("Pos pedoni nero: " + index);
                    board[index++] = "p";
                }

            } else if (Character.isUpperCase(c)) { // white pieces
                if (c == 'R') {
                    System.out.println("Pos torre bianco: " + index);
                    board[index++] = "R";

                } else if (c == 'N') {
                    System.out.println("Pos cavallo bianco: " + index);
                    board[index++] = "N";

                } else if (c == 'B') {
                    System.out.println("Pos alfiere bianco: " + index);
                    board[index++] = "B";

                } else if (c == 'Q') {
                    System.out.println("Pos regina bianco: " + index);
                    board[index++] = "Q";

                } else if (c == 'K') {
                    System.out.println("Pos re bianco: " + index);
                    board[index++] = "K";

                } else if (c == 'P') {
                    System.out.println("Pos pedoni bianco: " + index);
                    board[index++] = "P";
                }

            } else if (Character.isDigit(c)) { // empty squares
                int emptySquares = Character.getNumericValue(c);

                for (int j = 0; j < emptySquares; j++) {
                    board[index++] = ".";
                }
            }
        }

        for (int i = 0; i < board.length; i++) {
            if (i % 8 == 0) {
                System.out.println();
            }

            System.out.print(board[i] + " ");
        }
    }
}
