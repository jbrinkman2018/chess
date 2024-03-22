package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import client.ui.EscapeSequences;
import dataAccess.DataAccessException;

import java.util.zip.DataFormatException;

public class BoardAritst {
    private ChessBoard board;
    BoardAritst(ChessBoard chessBoard){
        this.board = chessBoard;
    }
    private enum orientation{
        TOP,
        BOTTOM
    }
    public String draw() {
            return borderBoardHARow() +
                    drawPieceRows(orientation.TOP) +
                borderBoardHARow() +
                "\n" +
                borderBoardAHRow() +
                    drawPieceRows(orientation.BOTTOM)+
                borderBoardAHRow();
    }
    private String drawBoardRow(int row, orientation orient) {
        StringBuilder str = new StringBuilder();
        if (orient == orientation.TOP){
            if (row %2 == 0) {
                str.append(colorRowDarkFirst(row));
            }
            else {
                str.append(colorRowLightFirst(row));
            }
        }
        else {
            if (row %2 == 0) {
                str.append(colorRowLightFirst(row));
            }
            else {
                str.append(colorRowDarkFirst(row));
            }
        }
        return str.toString();
    }
    private String borderBoardHARow() {
        return EscapeSequences.SET_TEXT_COLOR_BLUE + EscapeSequences.SET_BG_COLOR_WHITE +
                EscapeSequences.EMPTY + "h" + EscapeSequences.EMPTY + "g" +
                EscapeSequences.EMPTY +"f" + EscapeSequences.EMPTY + "e" + EscapeSequences.EMPTY + "d" +
                EscapeSequences.EMPTY + "c" + EscapeSequences.EMPTY + "b" + EscapeSequences.EMPTY + "a" +
                EscapeSequences.EMPTY+ EscapeSequences.RESET_BG_COLOR +
                "\n";
    }
    private String borderBoardAHRow() {
        return EscapeSequences.SET_TEXT_COLOR_BLUE + EscapeSequences.SET_BG_COLOR_WHITE +
                EscapeSequences.EMPTY + "a" + EscapeSequences.EMPTY + "b" +
                EscapeSequences.EMPTY + "c" + EscapeSequences.EMPTY + "d" + EscapeSequences.EMPTY + "e" +
                EscapeSequences.EMPTY + "f" + EscapeSequences.EMPTY + "g" + EscapeSequences.EMPTY + "h" +
                EscapeSequences.EMPTY + EscapeSequences.RESET_BG_COLOR+
                "\n";
    }
    private String drawPieceRows(orientation orient) {
        StringBuilder rowString = new StringBuilder();
        int upperBound = 9;
        if (orient == orientation.BOTTOM) {
            for (int row = upperBound-1; row> 0; row--){
                rowString.append(
                        EscapeSequences.SET_TEXT_COLOR_BLUE + EscapeSequences.SET_BG_COLOR_WHITE +
                                " " + row + " " + drawBoardRow(row, orient) +
                                EscapeSequences.SET_TEXT_COLOR_BLUE + EscapeSequences.SET_BG_COLOR_WHITE +
                                " " + row + " " + EscapeSequences.RESET_BG_COLOR + "\n");
            }
        }
        else {
            for (int row = 1; row<upperBound; row++){
                rowString.append(
                        EscapeSequences.SET_TEXT_COLOR_BLUE + EscapeSequences.SET_BG_COLOR_WHITE +
                                " " + row + " " + drawBoardRow(row,orient) +
                                EscapeSequences.SET_TEXT_COLOR_BLUE + EscapeSequences.SET_BG_COLOR_WHITE +
                                " " + row + " " + EscapeSequences.RESET_BG_COLOR + "\n");
            }
        }
        return rowString.toString();
    }

    private String colorRowDarkFirst(int row) {
        StringBuilder str = new StringBuilder();
        for (int col = 1; col < 9; col++) {
            if (col % 2 == 0) {
                str.append(EscapeSequences.SET_BG_COLOR_DARK_GREY);
                ChessPosition position = new ChessPosition(row, col);
                if (board.getPiece(position) == null) {
                    str.append(EscapeSequences.EMPTY);
                }
                else {
                    str.append(drawChessPiece(position));
                }
            } else {
                str.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                ChessPosition position = new ChessPosition(row, col);
                if (board.getPiece(position) == null) {
                    str.append(EscapeSequences.EMPTY);
                }
                else {
                    str.append(drawChessPiece(position));
                }
            }
        }
        return str.toString();
    }
    private String colorRowLightFirst(int row) {
        StringBuilder str = new StringBuilder();
        for (int col = 1; col < 9; col++) {
            if (col % 2 == 0) {
                str.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                ChessPosition position = new ChessPosition(row, col);
                if (board.getPiece(position) == null) {
                    str.append(EscapeSequences.EMPTY);
                }
                else {
                    str.append(drawChessPiece(position));
                }
            } else {
                str.append(EscapeSequences.SET_BG_COLOR_DARK_GREY);
                ChessPosition position = new ChessPosition(row, col);
                if (board.getPiece(position) == null) {
                    str.append(EscapeSequences.EMPTY);
                }
                else {
                    str.append(drawChessPiece(position));
                }
            }
        }
        return str.toString();
    }
    private String drawChessPiece(ChessPosition position) {
        ChessPiece chessPiece = board.getPiece(position);
        if (chessPiece.getPieceType() == ChessPiece.PieceType.KING){
            if (chessPiece.getTeamColor() == ChessGame.TeamColor.BLACK){
                return EscapeSequences.BLACK_KING;
            }
            else {
                return EscapeSequences.WHITE_KING;
            }
        }
        if (chessPiece.getPieceType() == ChessPiece.PieceType.QUEEN){
            if (chessPiece.getTeamColor() == ChessGame.TeamColor.BLACK){
                return EscapeSequences.BLACK_QUEEN;
            }
            else {
                return EscapeSequences.WHITE_QUEEN;
            }
        }
        if (chessPiece.getPieceType() == ChessPiece.PieceType.BISHOP){
            if (chessPiece.getTeamColor() == ChessGame.TeamColor.BLACK){
                return EscapeSequences.BLACK_BISHOP;
            }
            else {
                return EscapeSequences.WHITE_BISHOP;
            }
        }
        if (chessPiece.getPieceType() == ChessPiece.PieceType.KNIGHT){
            if (chessPiece.getTeamColor() == ChessGame.TeamColor.BLACK){
                return EscapeSequences.BLACK_KNIGHT;
            }
            else {
                return EscapeSequences.WHITE_KNIGHT;
            }
        }
        if (chessPiece.getPieceType() == ChessPiece.PieceType.ROOK){
            if (chessPiece.getTeamColor() == ChessGame.TeamColor.BLACK){
                return EscapeSequences.BLACK_ROOK;
            }
            else {
                return EscapeSequences.WHITE_ROOK;
            }
        }
        if (chessPiece.getPieceType() == ChessPiece.PieceType.PAWN){
            if (chessPiece.getTeamColor() == ChessGame.TeamColor.BLACK){
                return EscapeSequences.BLACK_PAWN;
            }
            else {
                return EscapeSequences.WHITE_PAWN;
            }
        }
        return null;
    }
}
