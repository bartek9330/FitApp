package com.fitwebappclient.usermain.services;

public interface BMI {
    default double calcBMI(Double weight, Double height) {
        return weight / Math.pow(height, 2);
    }
}
