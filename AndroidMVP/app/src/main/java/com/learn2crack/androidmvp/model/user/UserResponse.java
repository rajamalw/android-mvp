package com.learn2crack.androidmvp.model.user;

public class UserResponse {

    private String message;
    private String token;
    private String name;
    private String email;
    private String created_at;

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCreated_at() {
        return created_at;
    }
}
