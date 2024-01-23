package chess.PieceMovesCalculators;

import chess.ChessBoard;
import chess.ChessPosition;

import java.util.Collection;
import java.util.ArrayList;

public class QueenMovesCalculator extends BasicPieceMoves {
    @Override
    public Collection<chess.ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<chess.ChessMove> availableMoves = new ArrayList<>();
        availableMoves.addAll(bishopMoves(board, myPosition));
        availableMoves.addAll(RookMoves(board, myPosition));
        return availableMoves;
    }
}
