package com.zybooks.perrywolfe_weighttrackingapp;

public class Diet {
    private String date;
    private int calories;

    public Diet(String date, int calories) {
        this.date = date;
        this.calories = calories;
    }

    public String getDate() {
        return date;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }
}
