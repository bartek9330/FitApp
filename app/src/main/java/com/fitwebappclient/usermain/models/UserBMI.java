package com.fitwebappclient.usermain.models;

import com.google.gson.annotations.SerializedName;

public class UserBMI {
    @SerializedName("weight")
    private int weight;

    @SerializedName("height")
    private int height;

    @SerializedName("date")
    private String date;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
