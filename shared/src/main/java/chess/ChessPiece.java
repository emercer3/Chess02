package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessPiece.PieceType type;
    private final ChessGame.TeamColor pieceColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    public Collection<ChessMove> Bishopmoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition original = position;

        bishop(board, original, original, moves, 1, 1);
        bishop(board, original, original, moves, -1, 1);
        bishop(board, original, original, moves, 1, -1);
        bishop(board, original, original, moves, -1, -1);

        return moves;
    }

    public void bishop(ChessBoard board, ChessPosition original, ChessPosition position, Collection<ChessMove> moves, int x, int y) {
        ChessPosition newposition = new ChessPosition(position.getRow()+x, position.getColumn()+y);
        if (newposition.getColumn() > 8 || newposition.getColumn() < 1) {
            return;
        } else if (newposition.getRow() > 8 || newposition.getRow() < 1) {
            return;
        } else if (board.getPiece(newposition) != null && board.getPiece(original).getTeamColor() == board.getPiece(newposition).getTeamColor()) {
            return;
        } else if (board.getPiece(newposition) != null && board.getPiece(original).getTeamColor() != board.getPiece(newposition).getTeamColor()) {
            ChessMove move = new ChessMove(original, newposition, null);
            moves.add(move);
            return;
        }
 
        ChessMove move = new ChessMove(original, newposition, null);
        moves.add(move);
        bishop(board, original, newposition, moves, x, y);
    }

    public Collection<ChessMove> Kingmoves(ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        return moves;
    }

    public Collection<ChessMove> Knightmoves(ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        return moves;
    }

    public Collection<ChessMove> Pawnmoves(ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        return moves;
    }

    public Collection<ChessMove> Queenmoves(ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        return moves;
    }

    public Collection<ChessMove> Rookmoves(ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        return moves;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch (type) {
            case BISHOP:
                return Bishopmoves(board, myPosition);
            case KING:
                return Kingmoves(myPosition);
            case KNIGHT:
                return Knightmoves(myPosition);
            case PAWN:
                return Pawnmoves(myPosition);
            case QUEEN:
                return Queenmoves(myPosition);
            case ROOK:
                return Rookmoves(myPosition);
            default:
                throw new RuntimeException("Incorrect");
        }
    }
}
