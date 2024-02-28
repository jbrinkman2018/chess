package chess.PieceMovesCalculators;

import chess.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collection;

public class PawnMovesCalculator extends BasicPieceMoves{
    private void addNormalMove(int curRow, int curCol, ChessBoard board, ChessPosition myPosition, ArrayList<chess.ChessMove> availableMoves) {
        if (curRow < 8 && curRow >1) {
            ChessPosition availablePosition = new ChessPosition(curRow, curCol);
            if (board.getPiece(availablePosition) == null) {
                chess.ChessMove availableMove = new chess.ChessMove(myPosition, availablePosition, null);
                availableMoves.add(availableMove);
                if ((myPosition.getRow() == 2 && board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) || (myPosition.getRow() == 7 && board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK)) { // first move for that pawn can go forward two spaces
                    if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK) curRow--;
                    else curRow++;
                    availablePosition = new ChessPosition(curRow, curCol);
                    if (board.getPiece(availablePosition) == null) {
                        availableMove = new chess.ChessMove(myPosition, availablePosition, null);
                        availableMoves.add(availableMove);
                    }
                }
            }
        }
    }

    private void addPromotionPieceOption(int curRow, int curCol, chess.ChessBoard board, chess.ChessPosition myPosition, HashSet<ChessMove> promotionMoves){
        ChessPosition availablePosition = new ChessPosition(curRow, curCol);
        if (board.getPiece(availablePosition) == null) {
            for (ChessPiece.PieceType pieceType: ChessPiece.PieceType.values()) {
                if (pieceType != ChessPiece.PieceType.KING && pieceType != ChessPiece.PieceType.PAWN) {
                    chess.ChessMove availableMove = new chess.ChessMove(myPosition, availablePosition, pieceType);
                    promotionMoves.add(availableMove);
                }
            }
        }
    }

    private void addCapture(int curRow, int curCol, chess.ChessBoard board, chess.ChessPosition myPosition, HashSet<ChessMove> promotionMoves, ArrayList<ChessMove> availableMoves) {
        if (curRow < 9 && curRow > 0) {
            ChessPosition availablePosition = new ChessPosition(curRow, curCol);
            if (board.getPiece(availablePosition) != null) {
                if (board.getPiece(availablePosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) { // if it's the other team
                    if (curRow == 1 || curRow == 8) { // if we need to promote
                        for (ChessPiece.PieceType pieceType : ChessPiece.PieceType.values()) {
                            if (pieceType != ChessPiece.PieceType.KING && pieceType != ChessPiece.PieceType.PAWN) {
                                chess.ChessMove availableMove = new chess.ChessMove(myPosition, availablePosition, pieceType);
                                promotionMoves.add(availableMove);
                                availableMove = null;
                            }
                        }
                    } else {
                        chess.ChessMove availableMove = new chess.ChessMove(myPosition, availablePosition, null);
                        availableMoves.add(availableMove);
                    }
                }
            }
        }
    }
    @Override
    public Collection<chess.ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        int myPosRow = myPosition.getRow();
        int myPosCol = myPosition.getColumn();

        ArrayList<chess.ChessMove> availableMoves = new ArrayList<>(); // normal moves
        HashSet<chess.ChessMove> promotionMoves = new HashSet<>(); // Promotion moves

        int curRow = myPosRow;
        int curCol = myPosCol;

        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE){ // WHITE moves
            curRow ++;
            if (curRow == 8 || curRow == 1) {
                addPromotionPieceOption(curRow, curCol, board, myPosition, promotionMoves); // promotion option
            }
            else {
                addNormalMove(curRow, curCol, board, myPosition, availableMoves); // normal move one forward
            }
            curRow = myPosRow+1;
            if (curCol>1) {
                curCol--;
                addCapture(curRow, curCol, board, myPosition, promotionMoves, availableMoves);
            }
            if (curCol <7) {
                curCol += 2;
                addCapture(curRow, curCol, board, myPosition, promotionMoves, availableMoves);
            }
        }
        else { // BLACK moves
            curRow --;
            if (curRow == 8 || curRow == 1) {
                addPromotionPieceOption(curRow, curCol, board, myPosition, promotionMoves); // promotion option for black
            }
            else {
                addNormalMove(curRow, curCol, board, myPosition, availableMoves); // normal pawn move
            }
            curRow = myPosRow -1;
            if (curCol>1) {
                curCol--;
                addCapture(curRow, curCol, board, myPosition, promotionMoves, availableMoves);
            }
            if (curCol<7) {
                curCol += 2;
                addCapture(curRow, curCol, board, myPosition, promotionMoves, availableMoves);
            }
        }
        if (promotionMoves.isEmpty()) return availableMoves;
        else return promotionMoves;
    }
}
