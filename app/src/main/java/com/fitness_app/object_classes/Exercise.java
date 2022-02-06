package com.fitness_app.object_classes;

public class Exercise {

    private String date;
    private String exerciseType;
    private int time;

    public Exercise(String date, String exerciseType, int time) {
        this.date = date;
        this.exerciseType = exerciseType;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public int getTime() {
        return time;
    }
}
