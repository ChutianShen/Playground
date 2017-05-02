package com.example.kevin_sct.beastchat.entites;

/**
 * Created by kevin_sct on 4/29/17.
 */

public class User {
    private String email;
    private String userPicture;
    private String userName;
    private boolean hasLoggedIn;

    public User() {
    }

    public User(String email, String picture, String userName, boolean hasLoggedIn) {
        this.email = email;
        this.userPicture = picture;
        this.userName = userName;
        this.hasLoggedIn = hasLoggedIn;
    }

    public String getEmail() {
        return email;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isHasLoggedIn() {
        return hasLoggedIn;
    }

}
