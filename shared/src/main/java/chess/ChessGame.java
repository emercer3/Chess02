package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board = new ChessBoard();
    private ChessGame.TeamColor turn = TeamColor.WHITE;

    public ChessGame() {
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    private boolean canKingEscape(ChessPosition position, Collection<ChessMove> moves, TeamColor teamcolor) {
        ChessBoard boardcopy = board.copy();
        for (ChessMove move : moves) {
            // try {
            ChessMove originalmove = new ChessMove(move.getEndPosition(), move.getStartPosition(),
                    move.getPromotionPiece());
            ChessPiece piece1 = board.getPiece(move.getStartPosition());
            ChessPiece piece2 = board.getPiece(move.getEndPosition());
            makeTestMove(move, boardcopy);

            if (isInCheck(teamcolor) == false) {
                return true;
            } else if (piece1.getPieceType() == ChessPiece.PieceType.KING) {
                moves.remove(move);
            }

            makeTestMove(originalmove, boardcopy);
            board.addPiece(move.getEndPosition(), piece2);
            // } catch (InvalidMoveException e) {
            // continue;
            // }
        }
        return false;
    }

    private void makeTestMove(ChessMove move, ChessBoard boardcopy) {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        board.addPiece(move.getEndPosition(), piece);
        board.addPiece(move.getStartPosition(), null);
    }

    private Collection<ChessMove> getValidMoves(ChessPosition position, Collection<ChessMove> moves,
            TeamColor teamcolor) {
        ChessBoard boardcopy = board.copy();
        Collection<ChessMove> finalListFlaged = new ArrayList<ChessMove>();
        Collection<ChessMove> finalList = new ArrayList<ChessMove>();
        boolean flag = false;
        for (ChessMove move : moves) {
            ChessMove originalmove = new ChessMove(move.getEndPosition(), move.getStartPosition(),
                    move.getPromotionPiece());
            ChessPiece piece2 = board.getPiece(move.getEndPosition());

            if (isInCheck(teamcolor)) {
                makeTestMove(move, boardcopy);
                if (!(isInCheck(teamcolor))) {
                    finalListFlaged.add(move);
                    flag = true;
                }
            } else if (!(isInCheck(teamcolor))) {
                makeTestMove(move, boardcopy);
                if (!(isInCheck(teamcolor))) {
                    finalList.add(move);
                }
            }

            makeTestMove(originalmove, boardcopy);
            board.addPiece(move.getEndPosition(), piece2);
        }

        if (flag) {
            return finalListFlaged;
        } else {
            return finalList;
        }
    }

    private ChessPosition getKingPosition(ChessGame.TeamColor color) {

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece == null) {
                    continue;
                } else if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == color) {
                    return new ChessPosition(i, j);
                }
            }
        }

        return null;
    }

    private Collection<ChessMove> getMovesAttacking(ChessGame.TeamColor teamColor) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece == null) {
                    continue;
                } else if (piece.getTeamColor() != teamColor) {
                    moves.addAll(piece.pieceMoves(board, new ChessPosition(i, j)));
                }
            }
        }
        return moves;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     *         startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        moves.addAll(board.getPiece(startPosition).pieceMoves(board, startPosition));
        Collection<ChessMove> validmoves = getValidMoves(startPosition, moves,
                board.getPiece(startPosition).getTeamColor());
        return validmoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (board.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException();
        } else if (validMoves(move.getStartPosition()).contains(move)) {
            ChessPiece piece = board.getPiece(move.getStartPosition());
            if (piece.getTeamColor() == turn) {
                if (move.getPromotionPiece() != null) {
                    board.addPiece(move.getEndPosition(), new ChessPiece(turn, move.getPromotionPiece()));
                    board.addPiece(move.getStartPosition(), null);
                } else {
                    board.addPiece(move.getEndPosition(), piece);
                    board.addPiece(move.getStartPosition(), null);
                }

                if (turn == ChessGame.TeamColor.WHITE) {
                    setTeamTurn(ChessGame.TeamColor.BLACK);
                } else {
                    setTeamTurn(ChessGame.TeamColor.WHITE);
                }
            } else {
                throw new InvalidMoveException();
            }
        } else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Boolean check = false;
        ChessPosition kingPosition = getKingPosition(teamColor);
        Collection<ChessMove> moves = getMovesAttacking(teamColor);

        for (ChessMove move : moves) {
            if (move.getEndPosition().equals(kingPosition)) {
                check = true;
            }
        }
        return check;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition kingposition = getKingPosition(teamColor);
        Collection<ChessMove> kingmoves = validMoves(kingposition);

        if (canKingEscape(kingposition, kingmoves, teamColor)) {
            return false;
        }
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece == null) {
                    continue;
                } else if (board.getPiece(new ChessPosition(i, j)).getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = validMoves(new ChessPosition(i, j));
                    if (canKingEscape(new ChessPosition(i, j), moves, teamColor)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        if (isInCheck(teamColor) == false) {
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                    if (piece == null) {
                        continue;
                    } else if (piece.getTeamColor() == teamColor) {
                        moves.addAll(validMoves(new ChessPosition(i, j)));
                    }
                }
            }
            if (moves.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
