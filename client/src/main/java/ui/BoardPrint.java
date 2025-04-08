package ui;

import static ui.EscapeSequences.*;
import java.util.Map;

import java.util.ArrayList;
import java.util.Collection;

import chess.*;

public class BoardPrint {

  public static void drawBoard(String color, ChessGame game, ChessPosition highlight) {
    int previous = -1;

    int[] numbers = { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 };
    reverseArray(numbers);
    String[] letters = { "", "a", "b", "c", "d", "e", "f", "g", "h", "" };

    ChessBoard board = game.getBoard().copy();
    Map<ChessPiece.PieceType, String> piecesMap = Map.of(
        ChessPiece.PieceType.PAWN, "p",
        ChessPiece.PieceType.KNIGHT, "n",
        ChessPiece.PieceType.BISHOP, "b",
        ChessPiece.PieceType.ROOK, "r",
        ChessPiece.PieceType.QUEEN, "q",
        ChessPiece.PieceType.KING, "k");
    Map<ChessGame.TeamColor, String> colorMap = Map.of(
        ChessGame.TeamColor.WHITE, SET_TEXT_COLOR_RED,
        ChessGame.TeamColor.BLACK, SET_TEXT_COLOR_LIGHT_GREY);
    Collection<ChessMove> validMoves = new ArrayList<>();

    if (highlight != null) {
      Collection<ChessMove> moves = new ArrayList<>();
      validMoves = game.validMoves(highlight);
    }

    if (!color.equals("WHITE")) {
      // board = flipBoard(board);
      // reverseArray(numbers);
      // reverseArray(letters);
      drawblack(letters, numbers, previous, board, highlight, piecesMap, colorMap, game, validMoves);
      return;
    }

    System.out.print("\n");

    for (int i = 9; i >= 0; i--) {
      for (int j = 0; j < 10; j++) {
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
        } else if (previous == 1) {
          previous = -1;
          ChessPosition position = new ChessPosition(i, j);
          ChessMove validMove = new ChessMove(highlight, position, null);

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

        } else {
          previous = 1;
          ChessPosition position = new ChessPosition(i, j);
          ChessMove validMove = new ChessMove(highlight, position, null);

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

      }
      System.out.println();
      previous = previous * -1;
    }
  }

  public static void drawblack(
      String[] letters,
      int[] numbers,
      int previous,
      ChessBoard board,
      ChessPosition highlight,
      Map<ChessPiece.PieceType, String> piecesMap,
      Map<ChessGame.TeamColor, String> colorMap,
      ChessGame game,
      Collection<ChessMove> validMoves) {
    System.out.print("\n");

    for (int i = 0; i < 10; i++) {
      for (int j = 9; j >= 0; j--) {
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
        } else if (previous == 1) {
          previous = -1;
          ChessPosition position = new ChessPosition(i, j);
          ChessMove validMove = new ChessMove(highlight, position, null);

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

        } else {
          previous = 1;
          ChessPosition position = new ChessPosition(i, j);
          ChessMove validMove = new ChessMove(highlight, position, null);

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

      }
      System.out.println();
      previous = previous * -1;
    }
  }

  public static ChessBoard flipBoard(ChessBoard original) {
    ChessBoard flipped = new ChessBoard();
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        ChessPiece piece = original.getPiece(new ChessPosition(i + 1, j + 1));
        if (piece != null) {
          // Place the piece in the flipped position
          flipped.addPiece(new ChessPosition(8 - i, 8 - j), piece);
        }
      }
    }
    return flipped;
  }

  // Helper method to reverse an int array pulled from copilot
  private static void reverseArray(int[] array) {
    for (int i = 0; i < array.length / 2; i++) {
      int temp = array[i];
      array[i] = array[array.length - 1 - i];
      array[array.length - 1 - i] = temp;
    }
  }

  // Helper method to reverse a String array pulled from copilot
  private static void reverseArray(String[] array) {
    for (int i = 0; i < array.length / 2; i++) {
      String temp = array[i];
      array[i] = array[array.length - 1 - i];
      array[array.length - 1 - i] = temp;
    }
  }
}
