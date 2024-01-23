package chess.PieceMovesCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator extends BasicPieceMoves{
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        return bishopMoves(board, myPosition);
    }
}
