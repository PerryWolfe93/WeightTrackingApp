package com.zybooks.perrywolfe_weighttrackingapp;

public class Weight {

    private double weight;
    private String date;

    public Weight(double weight, String date) {
        this.weight = weight;
        this.date = date;
    }

    public double getWeight() {
        return weight;
    }

    public String getDate() {
        return date;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

}
