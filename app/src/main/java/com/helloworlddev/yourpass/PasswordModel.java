package com.helloworlddev.yourpass;

public class PasswordModel {
    private String user;
    private String password;
    private String date;

    public PasswordModel() {
        // Default constructor required for Firebase serialization
    }

    public PasswordModel(String user, String password, String date) {
        this.user = user;
        this.password = password;
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
