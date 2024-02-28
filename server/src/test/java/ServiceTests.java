import dataAccess.DataAccessException;
import dataAccess.authDAOs.*;
import dataAccess.gameDAOs.*;
import dataAccess.userDAOs.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.*;
import services.*;
import services.gameServices.*;
import services.userServices.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.*;

public class ServiceTests {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    @BeforeEach
    public void setup() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
    }

    @Test
    @DisplayName("register Positive Unit Test")
    public void regPosUnitTest(){
        try {
            Service service = new RegisterService(userDAO, authDAO);
            String username = "username";
            String password = "password";
            String email = "email";
            User user = new User(username, password, email);
            service.register(user);
        }
        catch(DataAccessException ex){

        }
    }

    @Test
    @DisplayName("register Negative Unit Test")
    public void regNegUnitTest(){}

    @Test
    @DisplayName("login Positive Unit Test")
    public void loginPosUnitTest(){}

    @Test
    @DisplayName("login Negative Unit Test")
    public void loginNegUnitTest(){}

    @Test
    @DisplayName("logout Positive Unit Test")
    public void logoutPosUnitTest(){}

    @Test
    @DisplayName("logout Negative Unit Test")
    public void logoutNegUnitTest(){}

    @Test
    @DisplayName("list Game Positive Unit Test")
    public void listPosUnitTest(){}

    @Test
    @DisplayName("list Game Negative Unit Test")
    public void listNegUnitTest(){}

    @Test
    @DisplayName("create Game Positive Unit Test")
    public void createPosUnitTest(){}

    @Test
    @DisplayName("create Game Negative Unit Test")
    public void createNegUnitTest(){}

    @Test
    @DisplayName("join Game Negative Unit Test")
    public void joinPosUnitTest(){}

    @Test
    @DisplayName("join Game Positive Unit Test")
    public void joinNegUnitTest(){}

    @Test
    @DisplayName("clear Unit Test")
    public void clearUnitTest(){}

}
