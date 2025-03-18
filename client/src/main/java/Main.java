import chess.*;
import client.*;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        var serverUrl = "http://localhost:8080";
        System.out.println("♕ 240 Chess Client: " + piece);

        new REPL(serverUrl).run();
    }
}