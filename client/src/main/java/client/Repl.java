package client;
import java.util.Scanner;

public class Repl {
    private final ChessClient chessClient;
    public Repl(String serverUrl){
        this.chessClient= new ChessClient(serverUrl);
    }
    public void run(){
        System.out.println("Welcome to Chess");
        System.out.println(chessClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (!result.equals("quit")){
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = chessClient.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }
    private void printPrompt() {
        System.out.print("\n" + ">>> ");
    }
}
