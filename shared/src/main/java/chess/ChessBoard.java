package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(squares);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } if (obj == null) {
            return false;
        } if (getClass() != obj.getClass()) {
            return false;
        }
        ChessBoard other = (ChessBoard) obj;
        if (!Arrays.deepEquals(squares, other.squares)) {
            return false;
        }
        return true;
    }

    // copilot generated
    public ChessBoard copy() {
        ChessBoard newBoard = new ChessBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = this.getPiece(new ChessPosition(i + 1, j + 1));
                if (piece != null) {
                    // Create a new ChessPiece object to avoid referencing the same object
                    ChessPiece copiedPiece = new ChessPiece(piece.getTeamColor(), piece.getPieceType());
                    newBoard.addPiece(new ChessPosition(i + 1, j + 1), copiedPiece);
                }
            }
        }
        return newBoard;
    }
    

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for(int i = 0; i < 8; i++) {
            for(int j = 1; j < 8; j++) {
                squares[i][j] = null;
            }
        }

        for(int i = 0; i < 8; i++) {
            squares[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            squares[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }

        for (int i = 0; i < 8; i += 1) {
            if (i == 0 || i == 7) {
                squares[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
                squares[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
            } else if (i == 1 || i == 6) {
                squares[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
                squares[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
            } else if (i == 2 || i == 5) {
                squares[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
                squares[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
            } else if (i == 3) {
                squares[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
                squares[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
            } else if (i == 4) {
                squares[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
                squares[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
            }
            
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        for (int row = 7; row >= 0; row--) { // Print from top row (8) to bottom row (1)
            builder.append(row + 1).append(" | "); // Add row number
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = squares[row][col];
                if (piece == null) {
                    builder.append(". "); // Empty square
                } else {
                    char pieceChar = piece.getPieceType().toString().charAt(0); // First letter of piece type
                    if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                        pieceChar = Character.toLowerCase(pieceChar); // Lowercase for black pieces
                    }
                    builder.append(pieceChar).append(" ");
                }
            }
            builder.append("\n");
        }
        builder.append("    a b c d e f g h\n"); // Add column labels
        return builder.toString();
    }
}