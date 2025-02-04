package chess;

import java.util.ArrayList;
import java.util.Collection;

import chess.ChessGame.TeamColor;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessPiece.PieceType type;
    private final ChessGame.TeamColor pieceColor;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((pieceColor == null) ? 0 : pieceColor.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChessPiece other = (ChessPiece) obj;
        if (type != other.type)
            return false;
        if (pieceColor != other.pieceColor)
            return false;
        return true;
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    // copilot generated
    public ChessPiece copy() {
        // Create a new instance of the piece with the same properties
        return new ChessPiece(this.pieceColor, this.type);
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

    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition original = position;

        bishop(board, original, original, moves, 1, 1, false, false);
        bishop(board, original, original, moves, -1, 1, false, false);
        bishop(board, original, original, moves, 1, -1, false, false);
        bishop(board, original, original, moves, -1, -1, false, false);

        return moves;
    }

    public void bishop(ChessBoard board, ChessPosition original, ChessPosition position, Collection<ChessMove> moves,
            int x, int y, boolean once, boolean promote) {
        ChessPosition newposition = new ChessPosition(position.getRow() + y, position.getColumn() + x);
        if (newposition.getColumn() > 8 || newposition.getColumn() < 1) {
            return;
        } else if (newposition.getRow() > 8 || newposition.getRow() < 1) {
            return;
        } else if (board.getPiece(newposition) != null
                && board.getPiece(original).getTeamColor() == board.getPiece(newposition).getTeamColor()) {
            return;
        } else if (board.getPiece(newposition) != null
                && board.getPiece(original).getTeamColor() != board.getPiece(newposition).getTeamColor()) {
            moveAdder(moves, original, newposition, promote);
            return;
        }
        if (board.getPiece(original).getPieceType() != PieceType.PAWN) {
            moveAdder(moves, original, newposition, promote);
        }
        if (once) {
            return;
        }
        bishop(board, original, newposition, moves, x, y, false, promote);
    }

    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition original = position;

        bishop(board, original, original, moves, 1, 1, true, false);
        bishop(board, original, original, moves, -1, 1, true, false);
        bishop(board, original, original, moves, 1, -1, true, false);
        bishop(board, original, original, moves, -1, -1, true, false);

        rook(board, original, original, moves, 1, 0, true, false);
        rook(board, original, original, moves, -1, 0, true, false);
        rook(board, original, original, moves, 0, 1, true, false);
        rook(board, original, original, moves, 0, -1, true, false);

        return moves;
    }

    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition original = position;

        knight(board, original, original, moves, 1, 2, 0);
        knight(board, original, original, moves, 2, 1, 0);
        knight(board, original, original, moves, -1, 2, 0);
        knight(board, original, original, moves, 2, -1, 0);

        return moves;
    }

    public void knight(ChessBoard board, ChessPosition original, ChessPosition position, Collection<ChessMove> moves,
            int x, int y, int count) {
        ChessPosition newposition = new ChessPosition(position.getRow() + y, position.getColumn() + x);
        if (count == 2) {
            return;
        }
        knight(board, original, original, moves, -1 * x, -1 * y, count + 1);

        if (newposition.getColumn() > 8 || newposition.getColumn() < 1) {
            return;
        } else if (newposition.getRow() > 8 || newposition.getRow() < 1) {
            return;
        } else if (board.getPiece(newposition) != null
                && board.getPiece(original).getTeamColor() == board.getPiece(newposition).getTeamColor()) {
            return;
        } else if (board.getPiece(newposition) != null
                && board.getPiece(original).getTeamColor() != board.getPiece(newposition).getTeamColor()) {
            moveAdder(moves, original, newposition, false);
            return;
        }

        moveAdder(moves, original, newposition, false);
    }

    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition original = position;

        if (board.getPiece(original).getTeamColor() == TeamColor.WHITE) {
            if (position.getRow() == 7) {
                rook(board, original, position, moves, 0, 1, true, true);
                bishop(board, original, position, moves, 1, 1, true, true);
                bishop(board, original, position, moves, -1, 1, true, true);
                return moves;
            }
            if (position.getRow() == 2) {
                pawnFirstMove(board, position, moves, 1);
            }
            rook(board, original, position, moves, 0, 1, true, false);
            bishop(board, original, position, moves, 1, 1, true, false);
            bishop(board, original, position, moves, -1, 1, true, false);
        } else {
            if (position.getRow() == 2) {
                rook(board, original, position, moves, 0, -1, true, true);
                bishop(board, original, position, moves, 1, -1, true, true);
                bishop(board, original, position, moves, -1, -1, true, true);
                return moves;
            }
            if (position.getRow() == 7) {
                pawnFirstMove(board, position, moves, -1);
            }
            rook(board, original, position, moves, 0, -1, true, false);
            bishop(board, original, position, moves, 1, -1, true, false);
            bishop(board, original, position, moves, -1, -1, true, false);
        }

        return moves;
    }

    public void pawnFirstMove(ChessBoard board, ChessPosition position, Collection<ChessMove> moves, int direction) {
        ChessPosition frist = new ChessPosition(position.getRow() + (1 * direction), position.getColumn());
        ChessPosition second = new ChessPosition(position.getRow() + (2 * direction), position.getColumn());

        if (board.getPiece(frist) == null && board.getPiece(second) == null) {
            moveAdder(moves, position, second, false);
        }
    }

    public void moveAdder(Collection<ChessMove> moves, ChessPosition original, ChessPosition newposition, boolean all) {
        if (all) {
            ChessMove moveq = new ChessMove(original, newposition, ChessPiece.PieceType.QUEEN);
            ChessMove moveb = new ChessMove(original, newposition, ChessPiece.PieceType.BISHOP);
            ChessMove mover = new ChessMove(original, newposition, ChessPiece.PieceType.ROOK);
            ChessMove movek = new ChessMove(original, newposition, ChessPiece.PieceType.KNIGHT);
            moves.add(moveq);
            moves.add(moveb);
            moves.add(mover);
            moves.add(movek);
        } else {
            ChessMove move = new ChessMove(original, newposition, null);
            moves.add(move);
        }
    }

    public Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition original = position;

        bishop(board, original, original, moves, 1, 1, false, false);
        bishop(board, original, original, moves, -1, 1, false, false);
        bishop(board, original, original, moves, 1, -1, false, false);
        bishop(board, original, original, moves, -1, -1, false, false);

        rook(board, original, original, moves, 1, 0, false, false);
        rook(board, original, original, moves, -1, 0, false, false);
        rook(board, original, original, moves, 0, 1, false, false);
        rook(board, original, original, moves, 0, -1, false, false);

        return moves;
    }

    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition original = position;

        rook(board, original, original, moves, 1, 0, false, false);
        rook(board, original, original, moves, -1, 0, false, false);
        rook(board, original, original, moves, 0, 1, false, false);
        rook(board, original, original, moves, 0, -1, false, false);

        return moves;
    }

    public void rook(ChessBoard board, ChessPosition original, ChessPosition position, Collection<ChessMove> moves,
            int x, int y, boolean once, boolean promote) {
        ChessPosition newposition = new ChessPosition(position.getRow() + y, position.getColumn() + x);
        if (newposition.getColumn() > 8 || newposition.getColumn() < 1) {
            return;
        } else if (newposition.getRow() > 8 || newposition.getRow() < 1) {
            return;
        } else if (board.getPiece(newposition) != null
                && board.getPiece(original).getTeamColor() == board.getPiece(newposition).getTeamColor()) {
            return;
        } else if (board.getPiece(newposition) != null
                && board.getPiece(original).getTeamColor() != board.getPiece(newposition).getTeamColor()) {
            if (board.getPiece(original).getPieceType() != PieceType.PAWN) {
                moveAdder(moves, original, newposition, promote);
            }
            return;
        }

        moveAdder(moves, original, newposition, promote);
        if (once) {
            return;
        }
        rook(board, original, newposition, moves, x, y, false, promote);
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
                return bishopMoves(board, myPosition);
            case KING:
                return kingMoves(board, myPosition);
            case KNIGHT:
                return knightMoves(board, myPosition);
            case PAWN:
                return pawnMoves(board, myPosition);
            case QUEEN:
                return queenMoves(board, myPosition);
            case ROOK:
                return rookMoves(board, myPosition);
            default:
                throw new RuntimeException("Incorrect");
        }
    }
}
