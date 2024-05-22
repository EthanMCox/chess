import chess.*;
import dataaccess.*;
import dataaccess.inMemory.*;
import service.*;

public class Main {
    private static final AuthDAO authDAO = new MemoryAuthDAO();
    public static void main(String[] args) {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Server: " + piece);

        try {
            var port = 8080;
            if (args.length >= 1) {
                port = Integer.parseInt(args[0]);
            }

            var clearService = new ClearService(authDAO, userDAO, gameDAO);
        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }


    }
}