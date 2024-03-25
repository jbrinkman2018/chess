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
    private enum rowOrientation {
        AH,
        HA
    }
    public String draw() {
            return  colorBorderRow(rowOrientation.HA) +
                    drawPieceRows(orientation.TOP) +
                    colorBorderRow(rowOrientation.HA) +
                "\n" +
                    colorBorderRow(rowOrientation.AH) +
                    drawPieceRows(orientation.BOTTOM) +
                    colorBorderRow(rowOrientation.AH) +
                    EscapeSequences.SET_TEXT_COLOR_BLUE;
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
    private String colorBorderRow(rowOrientation rowOrient) {
        StringBuilder str = new StringBuilder();
        str.append(EscapeSequences.SET_TEXT_COLOR_BLUE + EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.EMPTY);
        for (int col = 1; col < 9; col++){
            if (rowOrient == rowOrientation.AH) {
                switch (col) {
                    case 1 -> str.append(" a ");
                    case 2 -> str.append(" b ");
                    case 3 -> str.append(" c ");
                    case 4 -> str.append(" d ");
                    case 5 -> str.append(" e ");
                    case 6 -> str.append(" f ");
                    case 7 -> str.append(" g ");
                    case 8 -> str.append(" h ");
                }
            }
            else {
                switch (col) {
                    case 1 -> str.append(" h ");
                    case 2 -> str.append(" g ");
                    case 3 -> str.append(" f ");
                    case 4 -> str.append(" e ");
                    case 5 -> str.append(" d ");
                    case 6 -> str.append(" c ");
                    case 7 -> str.append(" b ");
                    case 8 -> str.append(" a ");
                }
            }
        }
        str.append(EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "\n");
        return str.toString();
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
                                " " + row + " " + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "\n");
            }
        }
        else {
            for (int row = 1; row<upperBound; row++){
                rowString.append(
                        EscapeSequences.SET_TEXT_COLOR_BLUE + EscapeSequences.SET_BG_COLOR_WHITE +
                                " " + row + " " + drawBoardRow(row,orient) +
                                EscapeSequences.SET_TEXT_COLOR_BLUE + EscapeSequences.SET_BG_COLOR_WHITE +
                                " " + row + " " + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "\n");
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
