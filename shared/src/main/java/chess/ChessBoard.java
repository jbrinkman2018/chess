package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {
        resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    public void removePiece(ChessPosition position) {
        squares[position.getRow() - 1][position.getColumn() - 1] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    private ChessPiece whiteRoyalPiece(int row, int col) {
        if (col == 1 || col == 8) {
            return new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        } else if (col == 2 || col == 7) {
            return new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        } else if (col == 3 || col == 6) {
            return new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        } else if (col == 4) {
            return new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        } else if (col == 5) {
            return new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        } else throw new RuntimeException("OUT OF BOUNDS");
    }

    private ChessPiece blackRoyalPiece(int row, int col) {
        if (col == 1 || col == 8) {
            return new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        } else if (col == 2 || col == 7) {
            return new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        } else if (col == 3 || col == 6) {
            return new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        } else if (col == 4) {
            return new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        } else if (col == 5) {
            return new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        } else throw new RuntimeException("OUT OF BOUNDS");
    }

    private void resetPieces(int row, int col) {
        if (col == 8) {
            if (row == 8) {
                return;
            } else {
                col = 0;
                row++;
            }
        }
        col++;
        ChessPosition position = new ChessPosition(row, col);
        removePiece(position);
        ChessPiece piece = null;
        if (row == 1) {
            piece = whiteRoyalPiece(row, col);
        } else if (row == 2) {
            piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        } else if (row == 7) {
            piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            ;
        } else if (row == 8) {
            piece = blackRoyalPiece(row, col);
        }
        addPiece(position, piece);
        resetPieces(row, col);
    }

    public void resetBoard() {
        int row = 1;
        int col = 0;
        resetPieces(row, col);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ChessBoard{");
        sb.append("squares=");
        for (int i = 0; i<8; i++){
            for (int j = 0; j < 8; j++){
                if (squares[i][j] != null) {
                    sb.append("row = ");
                    sb.append(Integer.toString(i));
                    sb.append("col = ");
                    sb.append(Integer.toString(j));
                    sb.append(squares[i][j].toString());
                }
            }
        }
        sb.append('}');
        return sb.toString();
    }
}
