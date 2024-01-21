package chess.PieceMovesCalculators;

import chess.ChessBoard;
import chess.ChessPosition;

import java.util.ArrayList;

public class BishopMovesCalculator implements PieceMovesCalculator{
    @Override
    public ArrayList<chess.ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        // if we are 1,1 then we can only go 2,2 3,3 4,4 5,5 6,6 7,7 8,8
        // if we are 2,2 then we ca go 1,1 3,1 1,3 3,3 4,4 5,5 6,6 7,7 8,8
        // if we are 3,3 then we can go 1,1 2,2 3,3 4,2 5,1 2,4, 1,5

        int myPosRow = myPosition.getRow();
        int myPosCol = myPosition.getColumn();
        ArrayList<chess.ChessMove> availableMoves = new ArrayList<>();

        int curRow = myPosRow;
        int curCol = myPosCol;


        // recursion
        while (curRow != 8 && curCol != 8) {
            curRow ++;
            curCol ++;
            ChessPosition availablePosition = new ChessPosition(curRow, curCol);
            chess.ChessMove availableMove = new chess.ChessMove(myPosition,availablePosition,null);
            availableMoves.add(availableMove);
        }
        curRow = myPosRow;
        curCol = myPosCol;
        while (curRow != 1 && curCol != 1) {
            curRow --;
            curCol --;
            ChessPosition availablePosition = new ChessPosition(curRow, curCol);
            chess.ChessMove availableMove = new chess.ChessMove(myPosition,availablePosition,null);
            availableMoves.add(availableMove);
        }
        curRow = myPosRow;
        curCol = myPosCol;
        while (curRow != 1 && curCol != 8) {
            curRow --;
            curCol ++;
            ChessPosition availablePosition = new ChessPosition(curRow, curCol);
            chess.ChessMove availableMove = new chess.ChessMove(myPosition,availablePosition,null);
            availableMoves.add(availableMove);
        }
        curRow = myPosRow;
        curCol = myPosCol;
        while (curRow != 8 && curCol != 1) {
            curRow ++;
            curCol --;
            ChessPosition availablePosition = new ChessPosition(curRow, curCol);
            chess.ChessMove availableMove = new chess.ChessMove(myPosition,availablePosition,null);
            availableMoves.add(availableMove);
        }
        //for (int i= 0; i < 8; i++) {
            //for (int j = 0; j < 8; j++) {
                //if ([i,j] == myPosition) {
                    // our end positions are all spaces [+1 +1], [-1,-1] and [=1,-1], [-1, +1], etc...
                //}
            //}
        //}

        return availableMoves;
    }
}
