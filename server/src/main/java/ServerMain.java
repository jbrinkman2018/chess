
import server.*;


public class ServerMain {
    public static void main(String[] args) {
        int port = 0;
        try {
            port = 8081;
            var server = new Server().run(port);
        }
        catch (Throwable ex) {
            System.out.println("Unable to start server");
        }
    }
}