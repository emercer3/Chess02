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

        bishop(board, original, original, moves, 1, 1, false);
        bishop(board, original, original, moves, -1, 1, false);
        bishop(board, original, original, moves, 1, -1, false);
        bishop(board, original, original, moves, -1, -1, false);

        return moves;
    }

    public void bishop(ChessBoard board, ChessPosition original, ChessPosition position, Collection<ChessMove> moves, int x, int y, boolean once) {
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
        if (once) {
            return;
        }
        bishop(board, original, newposition, moves, x, y, false);
    }

    public Collection<ChessMove> Kingmoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition original = position;

        bishop(board, original, original, moves, 1, 1, true);
        bishop(board, original, original, moves, -1, 1, true);
        bishop(board, original, original, moves, 1, -1, true);
        bishop(board, original, original, moves, -1, -1, true);

        rook(board, original, original, moves, 1, 0, true);
        rook(board, original, original, moves, -1, 0, true);
        rook(board, original, original, moves, 0, 1, true);
        rook(board, original, original, moves, 0, -1, true);

        return moves;
    }

    public Collection<ChessMove> Knightmoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition original = position;

        knight(board, original, original, moves, 1, 2, 0);
        knight(board, original, original, moves, 2, 1, 0);
        knight(board, original, original, moves, -1, 2, 0);
        knight(board, original, original, moves, 2, -1, 0);

        return moves;
    }

    public void knight(ChessBoard board, ChessPosition original, ChessPosition position, Collection<ChessMove> moves, int x, int y, int count) {
        ChessPosition newposition = new ChessPosition(position.getRow()+x, position.getColumn()+y);
        if (count == 2) {
            return;
        } 
        knight(board, original, original, moves, -1*x, -1*y, count+1);

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
    }

    public Collection<ChessMove> Pawnmoves(ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        return moves;
    }

    public Collection<ChessMove> Queenmoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition original = position;

        bishop(board, original, original, moves, 1, 1, false);
        bishop(board, original, original, moves, -1, 1, false);
        bishop(board, original, original, moves, 1, -1, false);
        bishop(board, original, original, moves, -1, -1, false);

        rook(board, original, original, moves, 1, 0, false);
        rook(board, original, original, moves, -1, 0, false);
        rook(board, original, original, moves, 0, 1, false);
        rook(board, original, original, moves, 0, -1, false);

        return moves;
    }

    public Collection<ChessMove> Rookmoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition original = position;

        rook(board, original, original, moves, 1, 0, false);
        rook(board, original, original, moves, -1, 0, false);
        rook(board, original, original, moves, 0, 1, false);
        rook(board, original, original, moves, 0, -1, false);

        return moves;
    }

    public void rook(ChessBoard board, ChessPosition original, ChessPosition position, Collection<ChessMove> moves, int x, int y, boolean once) {
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
        if (once) {
            return;
        }
        rook(board, original, newposition, moves, x, y, false);
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
                return Kingmoves(board, myPosition);
            case KNIGHT:
                return Knightmoves(board, myPosition);
            case PAWN:
                return Pawnmoves(myPosition);
            case QUEEN:
                return Queenmoves(board, myPosition);
            case ROOK:
                return Rookmoves(board, myPosition);
            default:
                throw new RuntimeException("Incorrect");
        }
    }
}
