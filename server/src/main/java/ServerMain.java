import dataAccess.DataAccessException;
import server.*;
import dataAccess.*;


public class ServerMain {
    public static void main(String[] args) {
        int port = 0;
        try {
            port = 8081;
            var server = new Server().run(port);
//            var server = new Server(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO()).run(port);
        }
        catch (Throwable ex) {
            System.out.println("Unable to start server");
        }
//        System.out.printf("Chess Server: %d", port);
    }
}