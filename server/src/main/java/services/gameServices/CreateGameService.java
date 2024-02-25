package services.gameServices;

import dataAccess.authDAOs.AuthDAO;
import dataAccess.gameDAOs.GameDAO;
import model.*;

public class CreateGameService extends GameService{
    public CreateGameService(GameDAO gameDAO, AuthDAO authDAO){
        super(gameDAO,authDAO);
    }
    public int createGame(Game game) {
        return gameDAO.createGame(game);
    }
}
