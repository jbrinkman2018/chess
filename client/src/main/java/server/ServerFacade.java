package server;

import com.google.gson.Gson;
import DataAccessException.DataAccessException;
import model.*;
import java.io.*;
import java.net.*;
import java.net.HttpURLConnection;

public class ServerFacade {
    private final String serverUrl;
    public ServerFacade(String serverUrl){
        this.serverUrl = serverUrl;
    }
    public ServerFacade(int port){
        this.serverUrl = "http://localhost:" + port;
    }

    public Auth register(User user) throws DataAccessException {
        var path = "/user";
        return this.makeRequest("POST", path, user, null, Auth.class);
    }
    public Auth login(User user) throws DataAccessException {
        var path = "/session";
        return this.makeRequest("POST", path, user, null, Auth.class);
    }

    public void logout(String authToken) throws DataAccessException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, authToken,  null);
    }

    public Game[] listGames(String authToken) throws DataAccessException {
        var path = "/game";
        record ListGamesResponse(Game[] games) {}
        var response = this.makeRequest("GET", path,null, authToken, ListGamesResponse.class);
        return response.games();
    }
    public Game createGame(Game game, String authToken) throws DataAccessException {
        var path = "/game";
        return this.makeRequest("POST", path, game, authToken, Game.class);
    }
    public Game joinGame(JoinRequest joinRequest, String authToken) throws DataAccessException {
        var path = "/game";
        return this.makeRequest("PUT", path, joinRequest, authToken, Game.class);
    }
    public void clear() throws DataAccessException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, String auth, Class<T> responseClass) throws DataAccessException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            if (request != null) {
                http.addRequestProperty("Content-Type", "application/json");
            }
            writeAuth(auth, http);
            if (method.equals("POST") || method.equals("PUT")) {
                http.setDoOutput(true);
            }
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new DataAccessException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = new BufferedOutputStream(http.getOutputStream())) {
                reqBody.write(reqData.getBytes());
            }
        }
    }
    private static void writeAuth(String auth, HttpURLConnection http) throws IOException {
        if (auth != null) {
            http.addRequestProperty("Authorization", auth);
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, DataAccessException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new DataAccessException(status, "failure: " + status);
        }
    }
//
    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}

