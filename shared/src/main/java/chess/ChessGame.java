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
        if (turn == TeamColor.WHITE) {
            turn = TeamColor.BLACK;
        } else {
            turn = TeamColor.WHITE;
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        moves.addAll(board.getPiece(startPosition).pieceMoves(board, startPosition));
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    public void getmovesatking(Collection<ChessMove> moves, ChessPosition kingPosition, ChessGame.TeamColor teamColor) {
        for (int i = 1; i < 8; i++) {
            for (int j = 1; j < 8; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece == null) {
                    continue;
                }else if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    kingPosition = new ChessPosition(i, j);
                } else if (piece.getTeamColor() != teamColor) {
                    moves.addAll(validMoves(new ChessPosition(i, j)));
                }
            }
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
        ChessPosition kingPosition = null;
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        getmovesatking(moves, null, teamColor);

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
        Collection<ChessMove> atackingmoves = new ArrayList<ChessMove>();
        Collection<ChessMove> defendingmoves = new ArrayList<ChessMove>();
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
  
        getmovesatking(defendingmoves, null, teamColor);
        getmovesatking(atackingmoves, null, teamColor);

        if (isInCheck(teamColor)) {
            if (isInStalemate(teamColor)) {
                return true;
            } 
            // else if (validMoves(kingposition).isEmpty()) {
                
            // }
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
        throw new RuntimeException("Not implemented");
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
