package com.fitness_app.object_classes;

import java.util.ArrayList;

public class Diet {
    private String date;
    private ArrayList<Food> foodList;

    public Diet(String date) {
        this.date = date;
        this.foodList = new ArrayList<>();
    }
    public Diet(String date, ArrayList<Food> foodList) {
        this.date = date;
        this.foodList = foodList;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<Food> getFoodList() {
        return foodList;
    }

    public void setFoodList(ArrayList<Food> foodList) {
        this.foodList = foodList;
    }
}
