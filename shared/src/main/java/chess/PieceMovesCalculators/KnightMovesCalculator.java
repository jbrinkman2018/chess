package chess.PieceMovesCalculators;


import chess.ChessBoard;
import chess.ChessPosition;

import java.util.Collection;
import java.util.ArrayList;

public class KnightMovesCalculator implements PieceMovesCalculator {
    public void addMove(int curRow, int curCol, ChessBoard board, ChessPosition myPosition, ArrayList<chess.ChessMove> availableMoves) {
        if (curRow > 0 && curRow < 9 && curCol > 0 && curCol < 9){
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
        @Override
        public Collection<chess.ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
            ArrayList<chess.ChessMove> availableMoves = new ArrayList<>();

            int curRow = myPosition.getRow();
            int curCol = myPosition.getColumn();

            curRow +=2;
            curCol ++;

            addMove(curRow, curCol, board, myPosition, availableMoves);
            curCol -=2;

            addMove(curRow, curCol, board, myPosition, availableMoves);

            curRow = myPosition.getRow();
            curCol = myPosition.getColumn();

            curRow -=2;
            curCol ++;

            addMove(curRow, curCol, board, myPosition, availableMoves);

            curCol -=2;

            addMove(curRow, curCol, board, myPosition, availableMoves);

            curRow = myPosition.getRow();
            curCol = myPosition.getColumn();

            curRow ++;
            curCol +=2;

            addMove(curRow, curCol, board, myPosition, availableMoves);

            curRow -=2;

            addMove(curRow, curCol, board, myPosition, availableMoves);

            curRow = myPosition.getRow();
            curCol = myPosition.getColumn();

            curRow ++;
            curCol -=2;

            addMove(curRow, curCol, board, myPosition, availableMoves);

            curRow -=2;

            addMove(curRow, curCol, board, myPosition, availableMoves);

            return availableMoves;
        }
}
