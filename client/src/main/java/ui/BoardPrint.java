package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Arrays;

import static ui.EscapeSequences.*;

public class BoardPrint {
  
  public static void drawBoard(String color) {
    int previous = 1;
    int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};

    if (!color.equals("white")) {
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
          System.out.print(SET_BG_COLOR_MAGENTA + SET_TEXT_COLOR_BLACK + " " + letters[i] + " " + RESET_TEXT_COLOR + RESET_BG_COLOR);
        } else if (j == 0 || j == 9) {
          System.out.print(SET_BG_COLOR_MAGENTA + SET_TEXT_COLOR_BLACK + " " + numbers[i] + " " + RESET_TEXT_COLOR + RESET_BG_COLOR);
        } else if (previous == 1) {
          System.out.print(SET_BG_COLOR_WHITE + "   " + RESET_BG_COLOR);
          previous = -1;
        } else {
          System.out.print(SET_BG_COLOR_BLACK + "   " + RESET_BG_COLOR);
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
