package client;

import dataAccess.DataAccessException;
import server.ServerFacade;
import model.*;

import java.util.Arrays;

public class ChessClient {
    private String visitorName = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.LOGGEDOUT;
    private String myAuthToken = null;

    public ChessClient(String serverUrl){
        this.serverUrl = serverUrl;
        server = new ServerFacade(serverUrl);
    }
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout(params);
//                case "list" -> listGames(params);
//                case "create" -> createGame(params);
//                case "join" -> joinGame(params);
//                case "observe" -> observeGame(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (DataAccessException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws DataAccessException {
        if (params.length >= 3) {
            var user = new User(params[0], params[1], params[2]);
            myAuthToken = server.register(user).authToken();
            state = State.LOGGEDIN;
//            visitorName = String.join("-", params);
//            ws = new WebSocketFacade(serverUrl, notificationHandler);
//            ws.enterPetShop(visitorName);
            return String.format("You registered with the following info \n %s.", user);
        }
        throw new DataAccessException(400, "Expected: <username> <password> <email>");
    }
    public String login(String... params) throws DataAccessException {
        if (params.length >= 1) {
            var user = new User(params[0], params[1], null);
            state = State.LOGGEDIN;
            myAuthToken = server.login(user).authToken();
//            visitorName = String.join("-", params);
//            ws = new WebSocketFacade(serverUrl, notificationHandler);
//            ws.enterPetShop(visitorName);
            return String.format("You logged in with the following info \n %s.", user);
        }
        throw new DataAccessException(400, "Expected: <yourname> <password>");
    }
    public String logout(String... params) throws DataAccessException {
//        if (params.length >= 1) {
        server.logout(myAuthToken);
        state = State.LOGGEDOUT;
//            visitorName = String.join("-", params);
//            ws = new WebSocketFacade(serverUrl, notificationHandler);
//            ws.enterPetShop(visitorName);
            return "You logged out";
//        }
//        throw new DataAccessException(400, "Expected: <yourname> <password>");
    }

//    public String rescuePet(String... params) throws ResponseException {
//        assertSignedIn();
//        if (params.length >= 2) {
//            var name = params[0];
//            var type = PetType.valueOf(params[1].toUpperCase());
//            var pet = new Pet(0, name, type);
//            pet = server.addPet(pet);
//            return String.format("You rescued %s. Assigned ID: %d", pet.name(), pet.id());
//        }
//        throw new ResponseException(400, "Expected: <name> <CAT|DOG|FROG>");
//    }

//    public String listPets() throws ResponseException {
//        assertSignedIn();
//        var pets = server.listPets();
//        var result = new StringBuilder();
//        var gson = new Gson();
//        for (var pet : pets) {
//            result.append(gson.toJson(pet)).append('\n');
//        }
//        return result.toString();
//    }
//
//    public String adoptPet(String... params) throws ResponseException {
//        assertSignedIn();
//        if (params.length == 1) {
//            try {
//                var id = Integer.parseInt(params[0]);
//                var pet = getPet(id);
//                if (pet != null) {
//                    server.deletePet(id);
//                    return String.format("%s says %s", pet.name(), pet.sound());
//                }
//            } catch (NumberFormatException ignored) {
//            }
//        }
//        throw new ResponseException(400, "Expected: <pet id>");
//    }
//
//    public String adoptAllPets() throws ResponseException {
//        assertSignedIn();
//        var buffer = new StringBuilder();
//        for (var pet : server.listPets()) {
//            buffer.append(String.format("%s says %s%n", pet.name(), pet.sound()));
//        }
//
//        server.deleteAllPets();
//        return buffer.toString();
//    }
//
//    public String signOut() throws ResponseException {
//        assertSignedIn();
//        ws.leavePetShop(visitorName);
//        ws = null;
//        state = State.SIGNEDOUT;
//        return String.format("%s left the shop", visitorName);
//    }
//
//    private Pet getPet(int id) throws ResponseException {
//        for (var pet : server.listPets()) {
//            if (pet.id() == id) {
//                return pet;
//            }
//        }
//        return null;
//    }
    public String help() {
        if (state == State.LOGGEDOUT) {
            return """
                    - login <USERNAME> <PASSWORD>
                    - register <USERNAME> <PASSWORD> <EMAIL>
                    - quit
                    """;
        }else if (state == State.LOGGEDIN) {
            return """
                - create <GAMENAME> - a game
                - list - games
                - join <ID> [WHITE|BLACK|<empty>] - a game
                - observe <ID> - a game
                - logout
                - quit
                - help - with possible commands
                """;
        } else return """
                "In gameplay"
                """;
    }
//    private void assertSignedIn() throws ResponseException {
//        if (state == State.SIGNEDOUT) {
//            throw new ResponseException(400, "You must sign in");
//        }
//    }
}
