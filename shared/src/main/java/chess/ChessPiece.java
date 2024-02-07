package chess;

import chess.PieceMovesCalculators.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

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
        return pieceColor;
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
    // pretty similar code here I think we could shorten
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if(getPieceType().equals(PieceType.KING)) {
            PieceMovesCalculator kingInterface = new KingMovesCalculator();
            return kingInterface.calculateMoves(board, myPosition);
        }
        if(getPieceType().equals(PieceType.QUEEN)) {
            PieceMovesCalculator queenInterface = new QueenMovesCalculator();
            return queenInterface.calculateMoves(board, myPosition);
        }
        if(getPieceType().equals(PieceType.BISHOP)) {
            PieceMovesCalculator bishopInterface = new BishopMovesCalculator();
            return bishopInterface.calculateMoves(board, myPosition);
        }
        if(getPieceType().equals(PieceType.KNIGHT)) {
            PieceMovesCalculator knightInterface = new KnightMovesCalculator();
            return knightInterface.calculateMoves(board, myPosition);
        }
        if(getPieceType().equals(PieceType.PAWN)) {
            PieceMovesCalculator pawnInterface = new PawnMovesCalculator();
            return pawnInterface.calculateMoves(board, myPosition);
        }
        if(getPieceType().equals(PieceType.ROOK)) {
            PieceMovesCalculator rookInterface = new RookMovesCalculator();
            return rookInterface.calculateMoves(board, myPosition);
        }
        return new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return type == that.type && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, pieceColor);
    }
//    public String toString() {
//        return String.format("Piece [%s, %s] ", type, pieceColor);
//    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ChessPiece{");
        sb.append("type=").append(type);
        sb.append(", pieceColor=").append(pieceColor);
        sb.append('}');
        return sb.toString();
    }
}
