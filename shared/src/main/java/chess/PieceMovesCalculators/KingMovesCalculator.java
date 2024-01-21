package chess.PieceMovesCalculators;

import chess.ChessBoard;
import chess.ChessPosition;

import java.util.ArrayList;

public class KingMovesCalculator implements PieceMovesCalculator{
    @Override
    public ArrayList<chess.ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {

        int myPosRow = myPosition.getRow();
        int myPosCol = myPosition.getColumn();
        ArrayList<chess.ChessMove> availableMoves = new ArrayList<>();


        // debug this
        for (int curRow = myPosRow-1; curRow < myPosRow+1; curRow++) {
            for (int curCol = myPosCol -1; curCol < myPosCol+1; curCol++){
                if (curCol > 0 && curCol < 9 && curRow > 0 && curRow < 9) {
                    ChessPosition availablePosition = new ChessPosition(curRow, curCol);
                    if (board.getPiece(availablePosition) != null) {
                        if (board.getPiece(availablePosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {}
                        else {
                            chess.ChessMove availableMove = new chess.ChessMove(myPosition,availablePosition,null);
                            availableMoves.add(availableMove);
                        }
                    }
                    else {
                        chess.ChessMove availableMove = new chess.ChessMove(myPosition, availablePosition, null);
                        availableMoves.add(availableMove);
                    }
                }
            }
        }

        return availableMoves;
    }
}
