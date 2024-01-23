package chess.PieceMovesCalculators;

import chess.ChessBoard;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
public class RookMovesCalculator extends BasicPieceMoves{
    @Override
    public Collection<chess.ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        return RookMoves(board, myPosition);
    }
}
