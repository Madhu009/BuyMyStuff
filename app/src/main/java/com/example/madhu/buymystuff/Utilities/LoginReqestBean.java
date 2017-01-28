package com.example.madhu.buymystuff.Utilities;

/**
 * Created by Madhu on 28-01-2017.
 */
public class LoginReqestBean {
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
