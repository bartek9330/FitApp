package com.fitwebappclient.loginandregistration.models;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class TokenJWN {
     public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @SerializedName("token")
    private String token;
}
