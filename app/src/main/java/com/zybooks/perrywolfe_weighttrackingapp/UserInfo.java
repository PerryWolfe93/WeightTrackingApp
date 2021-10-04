package com.zybooks.perrywolfe_weighttrackingapp;

public class UserInfo {

    private String gender;
    private int age;
    private int height;
    private float goalWeight;
    private String fitnessPlan;

    public UserInfo() {
        this.gender = null;
        this.age = 0;
        this.height = 0;
        this.goalWeight = 0;
        this.fitnessPlan = null;
    }

    // Getters
    public String getGender() {
        return gender;
    }
    public int getAge() {
        return age;
    }
    public int getHeight() {
        return height;
    }
    public float getGoalWeight() {
        return goalWeight;
    }
    public String getFitnessPlan() { return fitnessPlan; }

    // Setters
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public void setGoalWeight(float goalWeight) {
        this.goalWeight = goalWeight;
    }
    public void setFitnessPlan(String fitnessPlan) {this.fitnessPlan = fitnessPlan; }
}
