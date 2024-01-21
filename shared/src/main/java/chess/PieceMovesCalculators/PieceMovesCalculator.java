package chess.PieceMovesCalculators;

import chess.ChessBoard;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public interface PieceMovesCalculator {
    ArrayList<chess.ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition);
}
