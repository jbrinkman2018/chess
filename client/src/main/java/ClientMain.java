
import client.Repl;

public class ClientMain {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8081";
        new Repl(serverUrl).run();
    }
}