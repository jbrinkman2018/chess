package client;

import chess.*;
import client.ui.EscapeSequences;

import java.util.ArrayList;

public class BoardArtist {
    private ChessBoard board;
    private ChessGame.TeamColor playerColor;
    private ArrayList<ChessPosition> endPositions;
    private RowOrientation rowOrientation;
    public BoardArtist(ChessBoard chessBoard, ChessGame.TeamColor playerColor, ArrayList<ChessPosition> endPositions){
        this.board = chessBoard;
        this.playerColor = playerColor;
        if (endPositions == null){
            this.endPositions = new ArrayList<ChessPosition>();
        }
        else {
            this.endPositions = endPositions;
        }
    }
    private enum Orientation {
        TOP,
        BOTTOM
    }
    private enum RowOrientation {
        AH,
        HA
    }
    public String draw() {
        var orient = Orientation.BOTTOM;
        rowOrientation = rowOrientation.HA;
        String boardDrawing =  "\n" +
                colorBorderRow() +
                drawPieceRows(orient) +
                colorBorderRow() +
                EscapeSequences.SET_TEXT_COLOR_BLUE;
        if (playerColor.equals(ChessGame.TeamColor.BLACK)){
            rowOrientation = rowOrientation.AH;
            orient = Orientation.TOP;
            boardDrawing =  "\n" +
                    colorBorderRow() +
                    drawPieceRows(orient) +
                    colorBorderRow() +
                    EscapeSequences.SET_TEXT_COLOR_BLUE;
        }
        return  boardDrawing;
    }
    private String drawBoardRow(int row, Orientation orient) {
        StringBuilder str = new StringBuilder();
        if (playerColor.equals(ChessGame.TeamColor.BLACK)){
            if (row %2 == 0) {
                str.append(colorRowLightFirst(row));
            }
            else {
                str.append(colorRowDarkFirst(row));
            }
        }
        else {
            if (row %2 == 0) {
                str.append(colorRowDarkFirst(row));
            }
            else {
                str.append(colorRowLightFirst(row));
            }
        }
        return str.toString();
    }
    private String colorBorderRow() {
        StringBuilder str = new StringBuilder();
        str.append(EscapeSequences.SET_TEXT_COLOR_BLUE + EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.EMPTY);
        for (int col = 1; col < 9; col++) {
            if (rowOrientation == rowOrientation.HA) {
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
            } else {
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
    private String drawPieceRows(Orientation orient) {
        StringBuilder rowString = new StringBuilder();
        int upperBound = 9;
        if (orient == Orientation.BOTTOM) {
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
            ChessPosition position = new ChessPosition(row, col);
            if (playerColor == ChessGame.TeamColor.BLACK){
                position = new ChessPosition(row, (9-col));
            }
            if (col % 2 == 0) {
                str.append(colorDarkGreyOrYellow(position));
            } else {
                str.append(colorLightGreyOrYellow(position));
            }
        }
        return str.toString();
    }
    private String colorRowLightFirst(int row) {
        StringBuilder str = new StringBuilder();
        for (int col = 1; col < 9; col++) {
            ChessPosition position = new ChessPosition(row, col);
            if (playerColor == ChessGame.TeamColor.BLACK){
                position = new ChessPosition(row, (9-col));
            }
            if (col % 2 == 0) {
                str.append(colorLightGreyOrYellow(position));
            } else {
                str.append(colorDarkGreyOrYellow(position));
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
    private String colorDarkGreyOrYellow(ChessPosition position){
        StringBuilder str = new StringBuilder();
        if (endPositions.contains(position)) {
            str.append(EscapeSequences.SET_BG_COLOR_YELLOW);
        }
        else {
            str.append(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        }
        if (board.getPiece(position) == null) {
            str.append(EscapeSequences.EMPTY);
        }
        else {
            str.append(drawChessPiece(position));
        }
        return str.toString();
    }
    private String colorLightGreyOrYellow(ChessPosition position) {
        StringBuilder str = new StringBuilder();
        if (endPositions.contains(position)) {
            str.append(EscapeSequences.SET_BG_COLOR_YELLOW);
        }
        else {
            str.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        }
        if (board.getPiece(position) == null) {
            str.append(EscapeSequences.EMPTY);
        }
        else {
            str.append(drawChessPiece(position));
        }
        return str.toString();
    }
}
