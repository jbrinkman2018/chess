package chess.PieceMovesCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator extends BasicPieceMoves{
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<chess.ChessMove> availableMoves = new ArrayList<>();

        for (int curRow = myPosition.getRow()-1; curRow < myPosition.getRow()+2; curRow++) {
            for (int curCol = myPosition.getColumn() -1; curCol < myPosition.getColumn()+2; curCol++){
                addMove(curRow, curCol, board, myPosition, availableMoves);
            }
        }
        return availableMoves;
    }
}
