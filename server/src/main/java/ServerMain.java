import dataAccess.DataAccessException;
import server.*;


public class ServerMain {
    public static void main(String[] args) {
        int port = 0;
        try {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Server: " + piece);
            port = 8081;
            var server = new Server().run(port);
        }
        catch (Throwable ex) {
            System.out.printf("Unable to run server: %s%n", ex.getMessage());
        }
        System.out.printf("Chess Server: %d", port);
    }
}