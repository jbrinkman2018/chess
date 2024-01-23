package chess.PieceMovesCalculators;

import chess.ChessBoard;
import chess.ChessPosition;

import java.util.Collection;

public interface PieceMovesCalculator {
    Collection<chess.ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition);
}
