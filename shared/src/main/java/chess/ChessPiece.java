package chess;

import chess.PieceMovesCalculators.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessPiece.PieceType type;
    private ChessGame.TeamColor pieceColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type = type;
        this.pieceColor = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if(getPieceType().equals("King")) {
            PieceMovesCalculator kingInterface = new KingMovesCalculator();
            return kingInterface.calculateMoves(board, myPosition);
        }
        if(getPieceType().equals("Queen")) {
            PieceMovesCalculator queenInterface = new QueenMovesCalculator();
            return queenInterface.calculateMoves(board, myPosition);
        }
        if(getPieceType().equals("Bishop")) {
            PieceMovesCalculator bishopInterface = new BishopMovesCalculator();
            return bishopInterface.calculateMoves(board, myPosition);
        }
        if(getPieceType().equals("Knight")) {
            PieceMovesCalculator knightInterface = new KnightMovesCalculator();
            return knightInterface.calculateMoves(board, myPosition);
        }
        if(getPieceType().equals("Pawn")) {
            PieceMovesCalculator pawnInterface = new PawnMovesCalculator();
            return pawnInterface.calculateMoves(board, myPosition);
        }
        if(getPieceType().equals("Rook")) {
            PieceMovesCalculator rookInterface = new RookMovesCalculator();
            return rookInterface.calculateMoves(board, myPosition);
        }
        return new ArrayList<>();
    }
}
