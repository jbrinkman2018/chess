package chess.PieceMovesCalculators;


import chess.ChessBoard;
import chess.ChessPosition;

import java.util.Collection;
import java.util.ArrayList;

public class KnightMovesCalculator extends BasicPieceMoves {
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
