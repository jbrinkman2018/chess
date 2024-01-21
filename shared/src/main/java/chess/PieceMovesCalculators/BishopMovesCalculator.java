package chess.PieceMovesCalculators;

import chess.ChessBoard;
import chess.ChessPosition;

import java.util.ArrayList;

public class BishopMovesCalculator implements PieceMovesCalculator{
    @Override
    public ArrayList<chess.ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {

        int myPosRow = myPosition.getRow();
        int myPosCol = myPosition.getColumn();
        ArrayList<chess.ChessMove> availableMoves = new ArrayList<>();

        int curRow = myPosRow;
        int curCol = myPosCol;

        while (curRow != 8 && curCol != 8) {
            curRow ++;
            curCol ++;
            ChessPosition availablePosition = new ChessPosition(curRow, curCol);
            if (board.getPiece(availablePosition) != null) {
                if (board.getPiece(availablePosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                    break;
                }
            }
            chess.ChessMove availableMove = new chess.ChessMove(myPosition,availablePosition,null);
            availableMoves.add(availableMove);
            if (board.getPiece(availablePosition) != null) {
                break;}
        }
        curRow = myPosRow;
        curCol = myPosCol;
        while (curRow != 1 && curCol != 1) {
            curRow --;
            curCol --;
            ChessPosition availablePosition = new ChessPosition(curRow, curCol);
            if (board.getPiece(availablePosition) != null) {
                if (board.getPiece(availablePosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                    break;
                }
            }
            chess.ChessMove availableMove = new chess.ChessMove(myPosition,availablePosition,null);
            availableMoves.add(availableMove);
            if (board.getPiece(availablePosition) != null) {
                break;}
        }
        curRow = myPosRow;
        curCol = myPosCol;
        while (curRow != 1 && curCol != 8) {
            curRow --;
            curCol ++;
            ChessPosition availablePosition = new ChessPosition(curRow, curCol);
            if (board.getPiece(availablePosition) != null) {
                if (board.getPiece(availablePosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                    break;
                }
            }
            chess.ChessMove availableMove = new chess.ChessMove(myPosition,availablePosition,null);
            availableMoves.add(availableMove);
            if (board.getPiece(availablePosition) != null) {
                break;}
        }
        curRow = myPosRow;
        curCol = myPosCol;
        while (curRow != 8 && curCol != 1) {
            curRow ++;
            curCol --;
            ChessPosition availablePosition = new ChessPosition(curRow, curCol);
            if (board.getPiece(availablePosition) != null) {
                if (board.getPiece(availablePosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                    break;
                }
            }
            chess.ChessMove availableMove = new chess.ChessMove(myPosition,availablePosition,null);
            availableMoves.add(availableMove);
            if (board.getPiece(availablePosition) != null) {
                break;}
        }

        return availableMoves;
    }
}
