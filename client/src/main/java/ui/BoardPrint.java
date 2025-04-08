package ui;

import static ui.EscapeSequences.*;
import java.util.Map;

import java.util.ArrayList;
import java.util.Collection;

import chess.*;

public class BoardPrint {
  private static int[] numbers = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
  private static String[] letters = { "", "a", "b", "c", "d", "e", "f", "g", "h", "" };
  private static Map<ChessPiece.PieceType, String> piecesMap = Map.of(
    ChessPiece.PieceType.PAWN, "p",
    ChessPiece.PieceType.KNIGHT, "n",
    ChessPiece.PieceType.BISHOP, "b",
    ChessPiece.PieceType.ROOK, "r",
    ChessPiece.PieceType.QUEEN, "q",
    ChessPiece.PieceType.KING, "k");
  private static Map<ChessGame.TeamColor, String> colorMap = Map.of(
      ChessGame.TeamColor.WHITE, SET_TEXT_COLOR_RED,
      ChessGame.TeamColor.BLACK, SET_TEXT_COLOR_LIGHT_GREY);
  private static Collection<ChessMove> validMoves = new ArrayList<>();

  

  public static void drawBoard(String color, ChessGame game, ChessPosition highlight) {
    int previous = -1;

    ChessBoard board = game.getBoard().copy();
    int[] localNumbers = numbers.clone();

    if (highlight != null) {
      Collection<ChessMove> moves = new ArrayList<>();
      validMoves = game.validMoves(highlight);
    }

    if (!color.equals("WHITE")) {
      reverseArray(localNumbers);
      drawblack(previous, board, highlight, game, localNumbers);
      return;
    }

    System.out.print("\n");

    for (int i = 9; i >= 0; i--) {
      for (int j = 0; j < 10; j++) {
        ChessPosition position = new ChessPosition(i, j);
        ChessMove validMove = new ChessMove(highlight, position, null);

        if (i == 0 || i == 9 || j == 0 || j == 9) {
          margins(i, j);
        }
        else if (previous == 1) {
          previous = -1;
          white(i, j, board, position, highlight, validMove);
        } 
        
        else {
          previous = 1;
          black(i, j, board, position, highlight, validMove);
        }

      }
      System.out.println();
      previous = previous * -1;
    }
  }


  public static void margins(int i, int j) {
    if (i == 0 && j == 0) {
      System.out.print("   ");
    } else if (i == 0 && j == 9) {
      System.out.print("   ");
    } else if (i == 9 && j == 0) {
      System.out.print("   ");
    } else if (i == 9 && j == 9) {
      System.out.print("   ");
    } else if (i == 0 || i == 9) {
      if (j > 0 && j < 9) {
        System.out.print(SET_BG_COLOR_MAGENTA + SET_TEXT_COLOR_BLACK + " " + letters[j] + " " + RESET_TEXT_COLOR
            + RESET_BG_COLOR);
      } else {
        System.out.print(SET_BG_COLOR_MAGENTA + "   " + RESET_BG_COLOR);
      }
    } else if (j == 0 || j == 9) {
      if (i > 0 && i < 9) {
        System.out.print(SET_BG_COLOR_MAGENTA + SET_TEXT_COLOR_BLACK + " " + numbers[i] + " " + RESET_TEXT_COLOR
            + RESET_BG_COLOR);
      } else {
        System.out.print(SET_BG_COLOR_MAGENTA + "   " + RESET_BG_COLOR);
      }
    }
  }

  public static void white(int i, int j, ChessBoard board, ChessPosition position, ChessPosition highlight, ChessMove validMove) {
    if (board.getPiece(position) == null) {
      if (highlight != null && validMoves.contains(validMove)) {
        System.out.print(SET_BG_COLOR_YELLOW + "   " + RESET_BG_COLOR);
      } else {
        System.out.print(SET_BG_COLOR_WHITE + "   " + RESET_BG_COLOR);
      }
    } else {
      if (validMoves.contains(validMove)) {
        System.out.print(SET_BG_COLOR_YELLOW + colorMap.get(board.getPiece(position).getTeamColor()) + " "
            + piecesMap.get(board.getPiece(position).getPieceType()) + " " + RESET_TEXT_COLOR + RESET_BG_COLOR);
      } else {
        System.out.print(SET_BG_COLOR_WHITE + colorMap.get(board.getPiece(position).getTeamColor()) + " "
            + piecesMap.get(board.getPiece(position).getPieceType()) + " " + RESET_TEXT_COLOR + RESET_BG_COLOR);
      }
    }
  }

  public static void black(int i, int j, ChessBoard board, ChessPosition position, ChessPosition highlight, ChessMove validMove) {
    if (board.getPiece(position) == null) {
      if (highlight != null && validMoves.contains(validMove)) {
        System.out.print(SET_BG_COLOR_YELLOW + "   " + RESET_BG_COLOR);
      } else {
        System.out.print(SET_BG_COLOR_BLACK + "   " + RESET_BG_COLOR);
      }
    } else {
      if (validMoves.contains(validMove)) {
        System.out.print(SET_BG_COLOR_YELLOW + colorMap.get(board.getPiece(position).getTeamColor()) + " "
            + piecesMap.get(board.getPiece(position).getPieceType()) + " " + RESET_TEXT_COLOR + RESET_BG_COLOR);
      } else {
        System.out.print(SET_BG_COLOR_BLACK + colorMap.get(board.getPiece(position).getTeamColor()) + " "
            + piecesMap.get(board.getPiece(position).getPieceType()) + " " + RESET_TEXT_COLOR + RESET_BG_COLOR);
      }
    }
  }


  public static void drawblack(int previous, ChessBoard board, ChessPosition highlight, ChessGame game, int[] numbers) {

    System.out.print("\n");

    for (int i = 0; i < 10; i++) {
      for (int j = 9; j >= 0; j--) {
        ChessPosition position = new ChessPosition(i, j);
        ChessMove validMove = new ChessMove(highlight, position, null);

        if (i == 0 || i == 9 || j == 0 || j == 9) {
          margins(i, j);
        }
        else if (previous == 1) {
          previous = -1;
          white(i, j, board, position, highlight, validMove);
        } 
        
        else {
          previous = 1;
          black(i, j, board, position, highlight, validMove);
        }

      }
      System.out.println();
      previous = previous * -1;
    }
  }

  // Helper method to reverse an int array pulled from copilot
  private static void reverseArray(int[] array) {
    for (int i = 0; i < array.length / 2; i++) {
      int temp = array[i];
      array[i] = array[array.length - 1 - i];
      array[array.length - 1 - i] = temp;
    }
  }
}
