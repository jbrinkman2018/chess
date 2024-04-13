package client;

import chess.*;
import client.BoardAritst;
import dataAccess.DataAccessException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class GamePlay {
    ChessGame.TeamColor playerColor;
    ChessGame game;
    GamePlay(ChessGame.TeamColor playerColor, ChessGame game) {
        this.playerColor = playerColor;
        if (game == null){
            this.game = new ChessGame(true);
        }
        else {
            this.game = game;
        }
    }
    public String redrawBoard(){
        return drawGameBoard(null);
    }
    public String showMoves(String... params) throws DataAccessException{
        if (params.length >= 1) {
            if (params[0].length() != 2){
                throw new DataAccessException(400, "Expected <Position> in format <COL><ROW> with COL a-g and row 1-8");
            }
            int col = charToCol(params[0].charAt(0));
            int row = Character.getNumericValue((params[0].charAt(1)));
            ChessPosition curPosition = new ChessPosition(row, col);
            ArrayList<ChessPosition> curPieceEndPositions = new ArrayList<ChessPosition>();
            if (game.getBoard().getPiece(curPosition) != null) {
                Collection<ChessMove> curPieceMoves = game.validMoves(curPosition);
                for (ChessMove curMove : curPieceMoves) {
                    curPieceEndPositions.add(curMove.getEndPosition());
                }
            }
            else {
                throw new DataAccessException(400, "No Chess Piece in this Position");
            }
            return drawGameBoard(curPieceEndPositions);
        }
        else {
            throw new DataAccessException(400,
                    "Expected: <POSITION> in format <COL><ROW> with COL a-g and row 1-8");
        }
    }
    public String makeMove(String... params) throws DataAccessException{
        if (params.length >= 2) {
            if (!game.getTeamTurn().equals(playerColor)){
                throw new DataAccessException(400,
                        "It's not your turn");
            }
            if (params[0].length() != 2) {
                throw new DataAccessException(400,
                        "Expected <STARTPOSITION><ENDPOSITION> in format <COL><ROW> with COL a-g and row 1-8");
            }
            int startCol = charToCol(params[0].charAt(0));
            int startRow = Character.getNumericValue((params[0].charAt(1)));
            ChessPosition startPosition = new ChessPosition(startRow, startCol);
            int endCol = charToCol(params[1].charAt(0));
            int endRow = Character.getNumericValue((params[1].charAt(1)));
            ChessPosition endPosition = new ChessPosition(endRow, endCol);
            ChessPiece.PieceType promotionPiece = null;
            if (((endRow == 8 && playerColor.equals(ChessGame.TeamColor.WHITE)) ||
                    (endRow == 1 && playerColor.equals(ChessGame.TeamColor.BLACK)))
                    &&
                    game.getBoard().getPiece(startPosition).getPieceType().equals(ChessPiece.PieceType.PAWN)) {

                System.out.println("What is your desired promotion piece?");
                Scanner scanner = new Scanner(System.in);
                promotionPiece = switch(scanner.nextLine().toLowerCase()) {
                    case "queen" -> ChessPiece.PieceType.QUEEN;
                    case "rook" -> ChessPiece.PieceType.ROOK;
                    case "knight" -> ChessPiece.PieceType.KNIGHT;
                    case "bishop" -> ChessPiece.PieceType.BISHOP;
                    default -> throw new DataAccessException(400, "invalid promotion piece");
                };
            }
            ChessMove chessMove = new ChessMove(startPosition, endPosition, promotionPiece);
            try{
                game.makeMove(chessMove);
            }catch (InvalidMoveException e){
                throw new DataAccessException(400, e.getMessage());
            }
            return redrawBoard();
        }
        else {
            throw new DataAccessException(400,
                "Expected <STARTPOSITION><ENDPOSITION> in format <COL><ROW> with COL a-g and row 1-8");
        }
    }
    public String resign(){
        return "You resigned";
    }
    private String drawGameBoard(ArrayList<ChessPosition> endPositions) {
        return new BoardAritst(game.getBoard(), playerColor, endPositions).draw();
    }
    private int charToCol(char colChar) throws DataAccessException {
        return switch (colChar) {
            case 'a' -> 1;
            case 'b' -> 2;
            case 'c' -> 3;
            case 'd' -> 4;
            case 'e' -> 5;
            case 'f' -> 6;
            case 'g' -> 7;
            case 'h' -> 8;
            default -> throw new DataAccessException(400, "Expected <Position> in format <COL><ROW> with COL a-g and row 1-8");
        };
    }
}
