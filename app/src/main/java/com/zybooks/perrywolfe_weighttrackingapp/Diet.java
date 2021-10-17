package com.zybooks.perrywolfe_weighttrackingapp;

public class Diet {
    private String date;
    private int calories;
    private int protein;
    private int carb;
    private int fat;

    public Diet(String date, int calories, int protein, int carb, int fat) {
        this.date = date;
        this.calories = calories;
        this.protein = protein;
        this.carb = carb;
        this.fat = fat;
    }

    public String getDate() {
        return date;
    }
    public int getCalories() {
        return calories;
    }
    public int getProtein() {
        return protein;
    }
    public int getCarb() {
        return carb;
    }
    public int getFat() {
        return fat;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }
    public void setProtein(int protein) {
        this.protein = protein;
    }
    public void setCarb(int carb) {
        this.carb = carb;
    }
    public void setFat(int fat) {
        this.fat = fat;
    }

}
