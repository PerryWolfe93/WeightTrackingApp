package com.zybooks.perrywolfe_weighttrackingapp;

public class Weight {

    private float weight;
    private String date;

    public Weight(float weight, String date) {
        this.weight = weight;
        this.date = date;
    }

    public float getWeight() {
        return weight;
    }

    public String getDate() {
        return date;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

}
