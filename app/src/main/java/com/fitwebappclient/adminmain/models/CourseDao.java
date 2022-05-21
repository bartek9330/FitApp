package com.fitwebappclient.adminmain.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CourseDao implements Serializable {

    @SerializedName("name")
    private String name;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @SerializedName("image")
    private String image;


    @SerializedName("numberOfSteps")
    private int numberOfSteps;


    @SerializedName("description")
    private String description;

    @SerializedName("steps")
    private StepDao[] steps;

    public StepDao[] getSteps() {
        return steps;
    }

    public void setSteps(StepDao[] steps) {
        this.steps = steps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfSteps() {
        return numberOfSteps;
    }

    public void setNumberOfSteps(int numberOfSteps) {
        this.numberOfSteps = numberOfSteps;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @SerializedName("id")
    private int id;
}
