package com.fitness_app.object_classes;

import java.sql.Connection;

public class User {
    public static String currentUser;
    public static Connection connection;
    public static boolean online;
    public static int userID;
    private String username;
    private String password;
    private String phoneNumber;
    private String email;
    private String gender;
    private int age;
    private int height;
    private String fitnessPlan;
    private double BMR;
    private double activityLevel;
    private String lastEvaluationDate;
    private double currentWeight;
    private double bmrAdjustment;
    private int lastWeekExerciseTime;
    private int calorieDifferenceFromGoal;
    private double weightChange;

    public User(String username, String password, String phoneNumber, String email,
                String gender, int age, int height, String fitnessPlan, double BMR,
                double activityLevel, String lastEvaluationDate, double currentWeight,
                double bmrAdjustment, int lastWeekExerciseTime, int calorieDifferenceFromGoal,
                double weightChange) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.fitnessPlan = fitnessPlan;
        this.BMR = BMR;
        this.activityLevel = activityLevel;
        this.lastEvaluationDate = lastEvaluationDate;
        this.currentWeight = currentWeight;
        this.bmrAdjustment = bmrAdjustment;
        this.lastWeekExerciseTime = lastWeekExerciseTime;
        this.calorieDifferenceFromGoal = calorieDifferenceFromGoal;
        this.weightChange = weightChange;
    }

    // Getters

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getEmail() {
        return email;
    }
    public String getGender() {
        return gender;
    }
    public int getAge() {
        return age;
    }
    public int getHeight() {
        return height;
    }
    public String getFitnessPlan() { return fitnessPlan; }
    public double getBMR() { return BMR; }
    public double getActivityLevel() { return activityLevel; }
    public String getLastEvaluationDate() { return lastEvaluationDate; }
    public double getCurrentWeight() { return currentWeight; }
    public double getBmrAdjustment() { return bmrAdjustment; }
    public int getLastWeekExerciseTime() { return  lastWeekExerciseTime; }
    public double getCalorieDifferenceFromGoal() {
        return calorieDifferenceFromGoal;
    }
    public double getWeightChange() {
        return weightChange;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public void setFitnessPlan(String fitnessPlan) { this.fitnessPlan = fitnessPlan; }
    public void setBMR(double BMR) { this.BMR = BMR; }
    public void setActivityLevel(double activityLevel) { this.activityLevel = activityLevel; }
    public void setLastEvaluationDate(String lastEvaluationDate) { this.lastEvaluationDate = lastEvaluationDate; }
    public void setCurrentWeight(double currentWeight)  { this.currentWeight = currentWeight; }
    public void setBmrAdjustment(double bmrAdjustment) { this.bmrAdjustment = bmrAdjustment; }
    public void setLastWeekExerciseTime(int lastWeekExerciseTime) { this.lastWeekExerciseTime = lastWeekExerciseTime;}
    public void setCalorieDifferenceFromGoal(int calorieDifferenceFromGoal) {
        this.calorieDifferenceFromGoal = calorieDifferenceFromGoal;
    }
    public void setWeightChange(double weightChange) {
        this.weightChange = weightChange;
    }
}
