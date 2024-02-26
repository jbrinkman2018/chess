package services.gameServices;

import dataAccess.authDAOs.AuthDAO;
import dataAccess.gameDAOs.GameDAO;
import model.Game;

public class JoinGameService extends GameService{
    public JoinGameService(GameDAO gameDAO, AuthDAO authDAO){
        super(gameDAO,authDAO);
    }
    public void joinGame(int gameID, String playerColor) {
        gameDAO.joinGame(gameID, playerColor);
    }
}
