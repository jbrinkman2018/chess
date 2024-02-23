import chess.*;
import server.*;

public class ServerMain {
    public static void main(String[] args) {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Server: " + piece);

        int port = 8080;
//        int (args.length >=1) {
//            port = Integer.parseInt(args[0]);
//        }
        var server = new Server().run(port);
//        port = server.port();
    }
}