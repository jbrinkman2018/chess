package client;

import chess.ChessBoard;
import chess.ChessGame;
import client.BoardAritst;

public class GamePlay {
    ChessGame.TeamColor playerColor;
    ChessBoard board;
    GamePlay(ChessGame.TeamColor playerColor, ChessBoard board) {
        this.playerColor = playerColor;
        this.board = board;
    }
    public String redrawBoard(){
        return drawGameBoard();
    }
    public String showMoves(String... params){
//        params
        return "";
//        return drawGameBoard(highlights);
    }
    public String makeMove(String... params){
        return " ";
    }
    public String resign(){
        return " ";
    }
    private String drawGameBoard(String... highlights) {
        return new BoardAritst(board, playerColor).draw();
    }
}
