package client;
import java.util.Scanner;
import client.ui.*;

public class Repl {
    private final ChessClient chessClient;
    public Repl(String serverUrl){
        this.chessClient= new ChessClient(serverUrl);
    }
    public void run(){
        StringBuilder str = new StringBuilder();
        str.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLUE + "Welcome to Chess, the competition is waiting " +
                EscapeSequences.BLACK_KING);
        System.out.println(str);
        System.out.print(chessClient.help());

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
        System.out.print("\n" + ">>> [" + chessClient.state + "]\n");
//        System.out.println(chessClient.help());
    }
}
