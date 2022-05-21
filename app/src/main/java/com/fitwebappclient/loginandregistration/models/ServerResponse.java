package com.fitwebappclient.loginandregistration.models;

import com.google.gson.annotations.SerializedName;

public class ServerResponse {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("message")
    private String message;
}
