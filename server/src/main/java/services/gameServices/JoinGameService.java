package services.gameServices;

import dataAccess.DataAccessException;
import dataAccess.authDAOs.AuthDAO;
import dataAccess.gameDAOs.GameDAO;
import chess.*;

public class JoinGameService extends GameService{
    public JoinGameService(GameDAO gameDAO, AuthDAO authDAO){
        super(gameDAO,authDAO);
    }
    public void joinGame(int gameID, ChessGame.TeamColor playerColor, String username) throws DataAccessException {
        gameDAO.updateGame(gameID, playerColor, username);
    }
    public void verifyGameID(int gameID) throws DataAccessException {
        if (gameDAO.getGame(gameID) == null) {
            throw new DataAccessException(400, "bad request");
        }
    }
}
