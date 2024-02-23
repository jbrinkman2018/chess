package model;

public record Auth(String username, String authToken) {
    public String getAuthToken() {
        return authToken;
    }
}
