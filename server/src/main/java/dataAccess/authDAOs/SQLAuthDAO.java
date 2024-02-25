package dataAccess.authDAOs;
import model.*;

public class SQLAuthDAO implements AuthDAO {

    @Override
    public String createAuth(String username) {
        return "null";
    }
    @Override
    public void deleteAuth(String authToken){}
    @Override
    public Auth getAuth(String authToken){
        return new Auth("null", "null");
    }
    @Override
    public void clear(){
    }
}
