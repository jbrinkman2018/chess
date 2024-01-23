package chess.PieceMovesCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.ArrayList;

public abstract class BasicPieceMoves implements PieceMovesCalculator {
    public enum horizDirection {
        LEFT,
        RIGHT,
    }
    public enum vertDirection {
        UP,
        DOWN,
    }
    // implement method to simplify bishop movement code i.e. make it cleaner i.e. remove repeat code by making calcdiagonalmoves method
   public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition){
       ArrayList<chess.ChessMove> availableMoves = new ArrayList<>();

       int curRow = myPosition.getRow();
       int curCol = myPosition.getColumn();

       while (curRow != 8 && curCol != 8) { // diagonal in another direction
           curRow ++;
           curCol ++;
           ChessPosition availablePosition = new ChessPosition(curRow, curCol);
           if (board.getPiece(availablePosition) != null) {
               if (board.getPiece(availablePosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                   break;
               }
           }
           chess.ChessMove availableMove = new chess.ChessMove(myPosition,availablePosition,null);
           availableMoves.add(availableMove);
           if (board.getPiece(availablePosition) != null) {
               break;}
       }
       curRow = myPosition.getRow();
       curCol = myPosition.getColumn();

       while (curRow != 1 && curCol != 1) { // diagonal in another direction
           curRow --;
           curCol --;
           ChessPosition availablePosition = new ChessPosition(curRow, curCol);
           if (board.getPiece(availablePosition) != null) {
               if (board.getPiece(availablePosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                   break;
               }
           }
           chess.ChessMove availableMove = new chess.ChessMove(myPosition,availablePosition,null);
           availableMoves.add(availableMove);
           if (board.getPiece(availablePosition) != null) {
               break;}
       }
       curRow = myPosition.getRow();
       curCol = myPosition.getColumn();

       while (curRow != 1 && curCol != 8) { // diagonal in another direction
           curRow --;
           curCol ++;
           ChessPosition availablePosition = new ChessPosition(curRow, curCol);
           if (board.getPiece(availablePosition) != null) {
               if (board.getPiece(availablePosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                   break;
               }
           }
           chess.ChessMove availableMove = new chess.ChessMove(myPosition,availablePosition,null);
           availableMoves.add(availableMove);
           if (board.getPiece(availablePosition) != null) {
               break;}
       }
       curRow = myPosition.getRow();
       curCol = myPosition.getColumn();

       while (curRow != 8 && curCol != 1) { // diagoal in one direction
           curRow ++;
           curCol --;
           ChessPosition availablePosition = new ChessPosition(curRow, curCol);
           if (board.getPiece(availablePosition) != null) {
               if (board.getPiece(availablePosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                   break;
               }
           }
           chess.ChessMove availableMove = new chess.ChessMove(myPosition,availablePosition,null);
           availableMoves.add(availableMove);
           if (board.getPiece(availablePosition) != null) {
               break;
           }
       }
       return availableMoves;
   }

   public boolean addBasicMove(ChessBoard board, ChessPosition myPosition, int curRow, int curCol, Collection<chess.ChessMove> availableMoves) {
       ChessPosition availablePosition = new ChessPosition(curRow, curCol);
       if (board.getPiece(availablePosition) != null) {
           if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.PAWN) {
               return false; // stop adding pieces
           }
           else {
               if (board.getPiece(availablePosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                   return false; // stop adding pieces
               }
               else {
                   chess.ChessMove availableMove = new chess.ChessMove(myPosition, availablePosition, null);
                   availableMoves.add(availableMove);
                   return false; // stop adding pieces
               }
           }
       }
       else {
           chess.ChessMove availableMove = new chess.ChessMove(myPosition, availablePosition, null);
           availableMoves.add(availableMove);
           return true; // keep adding pieces
       }
    }

   public Collection<ChessMove> moveHorizontalIndefinite(ChessBoard board, ChessPosition myPosition, horizDirection direction, int curRow, int curCol, Collection<chess.ChessMove> availableMoves) {
       if ((curCol== 1 && direction == horizDirection.LEFT) || (curCol == 8 && direction == horizDirection.RIGHT)) {
           return availableMoves;
       }
       else {
           if (direction == horizDirection.LEFT) {
               curCol--;
           }
           else {
               curCol++;
           }
           if (addBasicMove(board, myPosition, curRow, curCol, availableMoves)) {
               return moveHorizontalIndefinite(board, myPosition, direction, curRow, curCol, availableMoves);
           }
           else {
               return availableMoves;
           }
       }
   }

   public Collection<ChessMove> moveVertIndefinite(ChessBoard board, ChessPosition myPosition, vertDirection direction, int curRow, int curCol, Collection<chess.ChessMove> availableMoves) {
       if ((curRow== 1 && direction == vertDirection.DOWN) || (curRow == 8 && direction == vertDirection.UP)){
           return availableMoves;
       }
       else {
           if (direction == vertDirection.UP) {
               curRow++;
           }
           else {
               curRow--;
           }
           if (addBasicMove(board, myPosition, curRow, curCol, availableMoves)) {
               return moveVertIndefinite(board, myPosition, direction, curRow, curCol, availableMoves);
           }
           else {
               return availableMoves;
           }
       }
   }

   public Collection<ChessMove> RookMoves(ChessBoard board, ChessPosition myPosition){
       ArrayList<chess.ChessMove> availableMoves = new ArrayList<>();
       int curRow = myPosition.getRow();
       int curCol = myPosition.getColumn();
       availableMoves.addAll(moveVertIndefinite(board, myPosition, vertDirection.UP, curRow, curCol, availableMoves));
       availableMoves.addAll(moveVertIndefinite(board, myPosition, vertDirection.DOWN, curRow, curCol, availableMoves));
       availableMoves.addAll(moveHorizontalIndefinite(board, myPosition, horizDirection.LEFT, curRow, curCol, availableMoves));
       availableMoves.addAll(moveHorizontalIndefinite(board, myPosition, horizDirection.RIGHT, curRow, curCol, availableMoves));
       return availableMoves;
   }
   @Override
   public abstract Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition);
}
