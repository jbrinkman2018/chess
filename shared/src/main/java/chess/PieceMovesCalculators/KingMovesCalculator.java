package chess.PieceMovesCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<chess.ChessMove> availableMoves = new ArrayList<>();

        for (int curRow = myPosition.getRow()-1; curRow < myPosition.getRow()+2; curRow++) {
            for (int curCol = myPosition.getColumn() -1; curCol < myPosition.getColumn()+2; curCol++){
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
