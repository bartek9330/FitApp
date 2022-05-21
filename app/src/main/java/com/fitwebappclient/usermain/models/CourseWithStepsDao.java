package com.fitwebappclient.usermain.models;


import com.fitwebappclient.adminmain.models.StepDao;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CourseWithStepsDao implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("image")
    private String image;

    @SerializedName("steps")
    private StepDao[] steps;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public StepDao[] getSteps() {
        return steps;
    }

    public void setSteps(StepDao[] steps) {
        this.steps = steps;
    }
}
