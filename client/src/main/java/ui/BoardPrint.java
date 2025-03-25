package ui;

import static ui.EscapeSequences.*;

public class BoardPrint {
  
  public static void drawBoard(String color) {
    int previous = -1;
    int redPawn = 2;
    int greyPawn = 7;
    int redPieces = 1;
    int greyPieces = 8;
    int[] numbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    reverseArray(numbers);
    String[] letters = {"", "a", "b", "c", "d", "e", "f", "g", "h"};
    String[] pieces = {"", "r", "k", "b", "q", "k", "b", "k", "r"};
    

    if (!color.equals("white")) {
      redPawn = 7;
      greyPawn = 2;
      redPieces = 8;
      greyPieces = 1;
      previous = 1;
      reverseArray(numbers);
      reverseArray(letters);
  }
    
    for (int i = 0; i < 10; i++) {
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
            System.out.print(SET_BG_COLOR_MAGENTA + SET_TEXT_COLOR_BLACK + " " + letters[j] + " " + RESET_TEXT_COLOR + RESET_BG_COLOR);
          } else {
            System.out.print(SET_BG_COLOR_MAGENTA + "   " + RESET_BG_COLOR);
          }
        } else if (j == 0 || j == 9) {
          if (i > 0 && i < 9) {
            System.out.print(SET_BG_COLOR_MAGENTA + SET_TEXT_COLOR_BLACK + " " + numbers[i] + " " + RESET_TEXT_COLOR + RESET_BG_COLOR);
          } else {
            System.out.print(SET_BG_COLOR_MAGENTA + "   " + RESET_BG_COLOR);
          }
        } else if (previous == 1) {
          if (i == greyPawn) {
            System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_LIGHT_GREY + " " + "p" + " " + RESET_TEXT_COLOR + RESET_BG_COLOR);
          } else if (i == redPawn) {
            System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_RED + " " + "p" + " " + RESET_TEXT_COLOR + RESET_BG_COLOR);
          } else if (i == greyPieces) {
            System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_LIGHT_GREY + " " + pieces[j] + " " + RESET_TEXT_COLOR + RESET_BG_COLOR);
          } else if (i == redPieces) {
            System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_RED + " " + pieces[j] + " " + RESET_TEXT_COLOR + RESET_BG_COLOR);
          } else {
            System.out.print(SET_BG_COLOR_WHITE + "   " + RESET_BG_COLOR);
          }
          previous = -1;
        } else {
          if (i == greyPawn) {
            System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_LIGHT_GREY + " " + "p" + " " + RESET_TEXT_COLOR + RESET_BG_COLOR);
          } else if (i == redPawn) {
            System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_RED + " " + "p" + " " + RESET_TEXT_COLOR + RESET_BG_COLOR);
          } else if (i == greyPieces) {
            System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_LIGHT_GREY + " " + pieces[j] + " " + RESET_TEXT_COLOR + RESET_BG_COLOR);
          } else if (i == redPieces) {
            System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_RED + " " + pieces[j] + " " + RESET_TEXT_COLOR + RESET_BG_COLOR);
          } else {
            System.out.print(SET_BG_COLOR_BLACK + "   " + RESET_BG_COLOR);
          }
          previous = 1;
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

// Helper method to reverse a String array pulled from copilot
private static void reverseArray(String[] array) {
  for (int i = 0; i < array.length / 2; i++) {
      String temp = array[i];
      array[i] = array[array.length - 1 - i];
      array[array.length - 1 - i] = temp;
  }
}
}
