package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGame() {}

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection <ChessMove> possibleMoves = board.getPiece(startPosition).pieceMoves(board, startPosition);
        Collection <ChessMove> validMoves = new HashSet<ChessMove>();
        ChessBoard copyBoard = deepCopyBoard();
        if (board.getPiece(startPosition) != null && possibleMoves != null) {
            for (ChessMove curMove:possibleMoves){
                TeamColor curTeamColor = board.getPiece(startPosition).getTeamColor();
                board.addPiece(curMove.getEndPosition(), board.getPiece(curMove.getStartPosition()));
                board.removePiece(curMove.getStartPosition());
                if (isInCheck(curTeamColor)) {
                } else {
                    validMoves.add(curMove);
                }
                retractMove(copyBoard);
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // if no piece on the start position of the
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());

        if (move.getStartPosition() == null)
            throw new InvalidMoveException("No piece to move");

        if (validMoves.isEmpty()) {
            throw new InvalidMoveException("Piece has no valid moves");
        }

        if (validMoves.contains(move)) {
            if (board.getPiece(move.getStartPosition()).getTeamColor() == teamTurn) {

//                ChessBoard copyBoard = deepCopyBoard();

                if (move.getPromotionPiece() != null) {
                    board.addPiece(move.getEndPosition(), new ChessPiece(teamTurn, move.getPromotionPiece()));
                }
                else {
                    board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
                }
                board.removePiece(move.getStartPosition());

//                if (isInCheck(teamTurn)){
//                    retractMove(copyBoard);
//                    throw new InvalidMoveException("Moved into Check");
//                }
                if (teamTurn == TeamColor.WHITE){
                    setTeamTurn(TeamColor.BLACK);
                }
                else {
                    setTeamTurn(TeamColor.WHITE);
                }
            }
            else {
                throw new InvalidMoveException("Team moved out of turn");
            }
        }
        else {
            throw new InvalidMoveException("Not a valid move");
        }
    }
    private void retractMove(ChessBoard copyBoard) {
        setBoard(copyBoard);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return board.equals(chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor){
        Collection<ChessPosition> enemyEndPositions = new HashSet<ChessPosition>();
        ChessPosition kingLocation = null;
        for (int row = 1; row<9;row++) {
            for (int col = 1; col < 9;col++) {
                ChessPosition queryPosition = new ChessPosition(row, col);
                if (board.getPiece(queryPosition) != null) {
                    if (board.getPiece(queryPosition).getTeamColor() != teamColor) {
                        Collection<ChessMove> enemyMoves = board.getPiece(queryPosition).pieceMoves(board,queryPosition);
                        for (ChessMove enemyMove : enemyMoves) {
                            enemyEndPositions.add(enemyMove.getEndPosition());
                        }
                    } else if (board.getPiece(queryPosition).getPieceType() == ChessPiece.PieceType.KING) {
                        kingLocation = queryPosition;
                    }
                }
            }
        }
//        if (kingLocation == null){
//            throw new InvalidMoveException("No King Found");
//        }
        if (enemyEndPositions.contains(kingLocation)) {
            return true;
        }
        else {
            return false;
        }

    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        Collection<ChessMove> escapeMoves = new HashSet<ChessMove>();
        ChessPosition queryPosition = null;

        if (isInCheck(teamColor)) {
            for (int row = 1; row<9;row++) {
                for (int col = 1; col < 9;col++) {
                    queryPosition = new ChessPosition(row, col);
                    if (board.getPiece(queryPosition) != null) {
                        if (board.getPiece(queryPosition).getTeamColor() == teamColor) {
                            escapeMoves.addAll(validMoves(queryPosition));
                        }
                    }
                }
            }
            // Copy the current board
            ChessBoard copyBoard = deepCopyBoard();
            if (!escapeMoves.isEmpty()) {
                for (ChessMove escapeMove:escapeMoves) {
                    try {
                        makeMove(escapeMove);
                        if (isInCheck(teamColor)){
                            retractMove(copyBoard);
                        }
                        else {
                            retractMove(copyBoard);
                            return false;
                        }
                    }
                    catch (InvalidMoveException e){
                        System.out.println(e.getMessage());
                    }
//                    finally{
//                        retractMove(escapeMove);
//                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        teamTurn = teamColor;
        Collection<ChessMove> totalAvailableMoves = new HashSet<ChessMove>();
        for (int row = 1; row<9;row++) {
            for (int col = 1; col < 9;col++) {
                ChessPosition piecePosition = new ChessPosition(row,col);
                if (board.getPiece(piecePosition) != null) {
                    if (board.getPiece(piecePosition).getTeamColor() == teamColor) {
                        totalAvailableMoves.addAll(validMoves(piecePosition));
                    }
                }
            }
        }
        for (ChessMove curMove: totalAvailableMoves) {
            try {
                makeMove(curMove);
                return false;
            }
            catch(InvalidMoveException e){
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard newBoard) {
        this.board = new ChessBoard();
        for (int row = 1; row<9;row++) {
            for (int col = 1; col < 9;col++) {
                ChessPosition piecePosition = new ChessPosition(row,col);
                if (newBoard.getPiece(piecePosition) != null) {
                    board.addPiece(piecePosition, newBoard.getPiece(piecePosition));
                }
            }
        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
    private ChessBoard deepCopyBoard() {
        ChessBoard copyBoard = new ChessBoard();
        for (int row = 1; row<9;row++) {
            for (int col = 1; col < 9;col++) {
                ChessPosition copyPosition = new ChessPosition(row,col);
                if (board.getPiece(copyPosition) != null) {
                    copyBoard.addPiece(copyPosition, board.getPiece(copyPosition));
                }
            }
        }
        return copyBoard;
    }
}
