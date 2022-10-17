package com.fitness_app.object_classes;

import java.util.ArrayList;

public class Diet {
    private final String date;
    private int entryID;
    private ArrayList<String> foodList;
    public static ArrayList<Food> foodCatalog;

    public Diet(String date) {
        this.date = date;
        this.foodList = new ArrayList<>();
    }
    public Diet(String date, ArrayList<String> foodList) {
        this.date = date;
        this.foodList = foodList;
    }

    public static Food checkCatalog(String foodName) {

        // Set bounds for the search algorithm
        int first = 0;
        int last = foodCatalog.size() - 1;

        // Binary search algorithm that iterates until first is greater than last or the desired food is found in the list.
        while (first <= last) {
            // Updates midpoint to be the average of both indexes
            int mid = (first + last) / 2;
            // Compares the desired food to the designated midpoint in the arraylist
            int comparison = foodName.compareTo(foodCatalog.get(mid).getName());
            // If the food name has a larger hash value than the midpoint food's name,
            // changes first to one spot in front of the midpoint. This shrinks the arrayList to half its size, taking the right half.
            if(comparison > 0) {
                first = mid + 1;
            }
            // If the food name has a smaller hash value than the midpoint food's name,
            // changes last to one spot in behind the midpoint. This shrinks the arrayList to half its size, taking the left half.
            else if(comparison < 0) {
                last = mid - 1;
            }
            // If the food name is found on the list, returns the food
            else {
                return foodCatalog.get(mid);
            }
        }
        // If the food is not found, returns null
        return null;
    }

    public String getDate() {
        return date;
    }
    public int getEntryID() { return entryID; }
    public ArrayList<String> getFoodList() {
        return foodList;
    }
    public void setEntryID(int entryID) { this.entryID = entryID; }
}
