package chess.PieceMovesCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.ArrayList;

public abstract class BasicPieceMoves implements PieceMovesCalculator {
    public enum HorizDirection {
        LEFT,
        RIGHT,
    }
    public enum VertDirection {
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

   public Collection<ChessMove> moveHorizontalIndefinite(ChessBoard board, ChessPosition myPosition, HorizDirection direction, int curRow, int curCol, Collection<chess.ChessMove> availableMoves) {
       if ((curCol== 1 && direction == HorizDirection.LEFT) || (curCol == 8 && direction == HorizDirection.RIGHT)) {
           return availableMoves;
       }
       else {
           if (direction == HorizDirection.LEFT) {
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

   public Collection<ChessMove> moveVertIndefinite(ChessBoard board, ChessPosition myPosition, VertDirection direction, int curRow, int curCol, Collection<chess.ChessMove> availableMoves) {
       if ((curRow== 1 && direction == VertDirection.DOWN) || (curRow == 8 && direction == VertDirection.UP)){
           return availableMoves;
       }
       else {
           if (direction == VertDirection.UP) {
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

   public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition){
       ArrayList<chess.ChessMove> availableMoves = new ArrayList<>();
       int curRow = myPosition.getRow();
       int curCol = myPosition.getColumn();
       availableMoves.addAll(moveVertIndefinite(board, myPosition, VertDirection.UP, curRow, curCol, availableMoves));
       availableMoves.addAll(moveVertIndefinite(board, myPosition, VertDirection.DOWN, curRow, curCol, availableMoves));
       availableMoves.addAll(moveHorizontalIndefinite(board, myPosition, HorizDirection.LEFT, curRow, curCol, availableMoves));
       availableMoves.addAll(moveHorizontalIndefinite(board, myPosition, HorizDirection.RIGHT, curRow, curCol, availableMoves));
       return availableMoves;
   }
   @Override
   public abstract Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition);

    public void addMove(int curRow, int curCol, ChessBoard board, ChessPosition myPosition, ArrayList<chess.ChessMove> availableMoves) {
        if (curRow > 0 && curRow < 9 && curCol > 0 && curCol < 9){
            ChessPosition availablePosition = new ChessPosition(curRow, curCol);
            if (board.getPiece(availablePosition) != null) {
                if (board.getPiece(availablePosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {}
                else {
                    chess.ChessMove availableMove = new chess.ChessMove(myPosition,availablePosition,null);
                    availableMoves.add(availableMove);
                }
            }
            else {
                chess.ChessMove availableMove = new chess.ChessMove(myPosition, availablePosition, null);
                availableMoves.add(availableMove);
            }
        }
    }
}
