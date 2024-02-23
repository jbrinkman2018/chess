package model;

public record User(String username, String password, String email) {
    public String getUsername() {
        return username;
    }
//    public String toString() {
//        return new Gson().toJson(this);
//    }
}
