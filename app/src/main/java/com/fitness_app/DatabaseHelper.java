package com.fitness_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.time.LocalDate;
import java.util.ArrayList;

import androidx.annotation.Nullable;

import com.fitness_app.object_classes.Diet;
import com.fitness_app.object_classes.Exercise;
import com.fitness_app.object_classes.Food;
import com.fitness_app.object_classes.User;
import com.fitness_app.object_classes.Weight;

public class DatabaseHelper extends SQLiteOpenHelper {

    // User Data Table Constants
    public static final String USER_DATA_TABLE = "USER_DATA_TABLE";
    public static final String COLUMN_USER_ID = "ID";
    public static final String COLUMN_USERNAME = "USERNAME";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_PHONE_NUMBER = "PHONE_NUMBER";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_GENDER = "GENDER";
    public static final String COLUMN_AGE = "AGE";
    public static final String COLUMN_HEIGHT = "HEIGHT";
    public static final String COLUMN_FITNESS_PLAN = "FITNESS_PLAN";
    public static final String COLUMN_BMR = "BMR";
    public static final String COLUMN_ACTIVITY_LEVEL = "ACTIVITY_LEVEL";
    public static final String COLUMN_FITNESS_AND_WEIGHT_EVALUATION_DATE = " FITNESS_AND_WEIGHT_EVALUATION_DATE";
    public static final String COLUMN_CURRENT_WEIGHT = "CURRENT_WEIGHT";
    public static final String COLUMN_BMR_ADJUSTMENT = "BMR_ADJUSTMENT";
    public static final String COLUMN_LAST_WEEK_EXERCISE_TIME = "LAST_WEEK_EXERCISE_TIME";
    public static final String COLUMN_CALORIE_DIFFERENCE_FROM_LAST_WEEK = "CALORIE_DIFFERENCE_FROM_LAST_WEEK";
    public static final String COLUMN_WEIGHT_CHANGE = "WEIGHT_CHANGE";

    // Weight Data Table Constants
    public static final String WEIGHT_DATA_TABLE = "WEIGHT_DATA_TABLE";
    public static final String COLUMN_WEIGHT_ID = "WEIGHT_ID";
    public static final String COLUMN_WEIGHT = "WEIGHT";

    // Diet Data Table Constants
    public static final String DIET_DATA_TABLE = "DIET_DATA_TABLE";
    public static final String COLUMN_DIET_ID = "DIET_ID";

    // Food Data Table Constants
    public static final String FOOD_DATA_TABLE = "FOOD_DATA_TABLE";
    public static final String COLUMN_FOOD_ID = "FOOD_ID";
    public static final String COLUMN_FOOD = "FOOD";
    public static final String COLUMN_CALORIES = "CALORIES";
    public static final String COLUMN_PROTEIN = "PROTEIN";
    public static final String COLUMN_CARB = "CARB";
    public static final String COLUMN_FAT = "FAT";

    // Exercise Data Table Constants
    public static final String EXERCISE_DATA_TABLE = "EXERCISE_DATA_TABLE";
    public static final String COLUMN_EXERCISE_ID = "EXERCISE_ID";
    public static final String COLUMN_EXERCISE_TIME = "EXERCISE_TIME";
    public static final String COLUMN_EXERCISE_TYPE = "EXERCISE_TYPE";

    // The Date String Constant is shared between three tables
    public static final String COLUMN_DATE = "DATE";

    // Default constructor for database helper
    public DatabaseHelper(@Nullable Context context) {
        super(context, "Fitness App Database", null, 3);
    }

    // Called the first time the database is accessed. Generates all database tables
    @Override
    public void onCreate(SQLiteDatabase fitnessAppDB) {
        Log.e("FitnessApp", "onCreate invoked");

        String userDataTable = "CREATE TABLE " + USER_DATA_TABLE + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USERNAME + " TEXT NOT NULL," +
                COLUMN_PASSWORD + " TEXT NOT NULL," +
                COLUMN_PHONE_NUMBER + " TEXT NOT NULL, " +
                COLUMN_EMAIL + " TEXT NOT NULL," +
                COLUMN_GENDER + " TEXT," +
                COLUMN_AGE + " INTEGER," +
                COLUMN_HEIGHT + " INTEGER," +
                COLUMN_FITNESS_PLAN + " TEXT," +
                COLUMN_BMR + " REAL," +
                COLUMN_ACTIVITY_LEVEL + " REAL," +
                COLUMN_FITNESS_AND_WEIGHT_EVALUATION_DATE + " TEXT," +
                COLUMN_CURRENT_WEIGHT + " REAL," +
                COLUMN_BMR_ADJUSTMENT + " REAL," +
                COLUMN_LAST_WEEK_EXERCISE_TIME + " INTEGER," +
                COLUMN_CALORIE_DIFFERENCE_FROM_LAST_WEEK + " INTEGER," +
                COLUMN_WEIGHT_CHANGE + " REAL)";
        fitnessAppDB.execSQL(userDataTable);

        String weightDataTable = "CREATE TABLE " + WEIGHT_DATA_TABLE + " (" +
                COLUMN_WEIGHT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                COLUMN_USER_ID + " INTEGER NOT NULL, " +
                COLUMN_WEIGHT + " REAL NOT NULL, " +
                COLUMN_DATE + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_USER_ID + ") " +
                "REFERENCES " + USER_DATA_TABLE + " (" + COLUMN_USER_ID + "))";
        fitnessAppDB.execSQL(weightDataTable);

        String dietDataTable = "CREATE TABLE " + DIET_DATA_TABLE + " (" +
                COLUMN_DIET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                COLUMN_USER_ID + " INTEGER NOT NULL, " +
                COLUMN_DATE + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_USER_ID + ") " +
                "REFERENCES " + USER_DATA_TABLE + " (" + COLUMN_USER_ID + "))";
        fitnessAppDB.execSQL(dietDataTable);

        String foodDataTable = "CREATE TABLE " + FOOD_DATA_TABLE + " (" +
                COLUMN_FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_WEIGHT_ID + " INTEGER NOT NULL, " +
                COLUMN_DATE + " TEXT NOT NULL, " +
                COLUMN_FOOD + " TEXT NOT NULL, " +
                COLUMN_CALORIES + " INTEGER NOT NULL, " +
                COLUMN_PROTEIN + " INTEGER, " +
                COLUMN_CARB + " INTEGER, " +
                COLUMN_FAT + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_WEIGHT_ID + ") " +
                "REFERENCES " + DIET_DATA_TABLE + " (" + COLUMN_WEIGHT_ID + "))";
        fitnessAppDB.execSQL(foodDataTable);

        String exerciseDataTable = "CREATE TABLE " + EXERCISE_DATA_TABLE + " (" +
                COLUMN_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                COLUMN_USER_ID + " INTEGER NOT NULL, " +
                COLUMN_EXERCISE_TIME + " INTEGER NOT NULL, " +
                COLUMN_EXERCISE_TYPE + " TEXT NOT NULL, " +
                COLUMN_DATE + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_USER_ID + ") " +
                "REFERENCES " + USER_DATA_TABLE + " (" + COLUMN_USER_ID + "))";
        fitnessAppDB.execSQL(exerciseDataTable);
    }

    // Called whenever a new version of the application is available
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("FitnessApp", "onUpgrade invoked");
    }

    // Database methods

    // Method for getting current user
    public int getUserID(String username) {
        SQLiteDatabase fitnessAppDB = this.getReadableDatabase();
        Cursor cursor = fitnessAppDB.rawQuery("Select " + COLUMN_USER_ID + " from " + USER_DATA_TABLE + " where " + COLUMN_USERNAME + " = ?", new String[] {username});
        if(cursor.moveToFirst()) {
            int result = cursor.getInt(0);
            cursor.close();
            return result;
        } else {
            cursor.close();
            return -1;
        }
    }


    // Database methods for new users/login

    // Adds new user to SQLite database
    public boolean addOneUser(User user) {
        SQLiteDatabase fitnessAppDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USERNAME, user.getUsername());
        cv.put(COLUMN_PASSWORD, user.getPassword());
        cv.put(COLUMN_PHONE_NUMBER, user.getPhoneNumber());
        cv.put(COLUMN_EMAIL, user.getEmail());
        cv.put(COLUMN_GENDER, user.getGender());
        cv.put(COLUMN_AGE, user.getAge());
        cv.put(COLUMN_HEIGHT, user.getHeight());
        cv.put(COLUMN_FITNESS_PLAN, user.getFitnessPlan());
        cv.put(COLUMN_BMR, user.getBMR());
        cv.put(COLUMN_ACTIVITY_LEVEL, user.getActivityLevel());
        cv.put(COLUMN_FITNESS_AND_WEIGHT_EVALUATION_DATE, user.getLastEvaluationDate());
        cv.put(COLUMN_CURRENT_WEIGHT, user.getCurrentWeight());
        cv.put(COLUMN_BMR_ADJUSTMENT, user.getBmrAdjustment());
        cv.put(COLUMN_LAST_WEEK_EXERCISE_TIME, user.getLastWeekExerciseTime());
        cv.put(COLUMN_CALORIE_DIFFERENCE_FROM_LAST_WEEK, user.getCalorieDifferenceFromGoal());
        cv.put(COLUMN_WEIGHT_CHANGE, user.getWeightChange());

        long insert = fitnessAppDB.insert(USER_DATA_TABLE, null, cv);
        return insert != -1;
    }
    // Checks that user exists in database
    public boolean checkUsername(String username) {
        SQLiteDatabase fitnessAppDB = this.getReadableDatabase();
        Cursor cursor = fitnessAppDB.rawQuery("Select * from " + USER_DATA_TABLE +
                " where " + COLUMN_USERNAME + " = ?", new String[] {username});
        boolean result = cursor.getCount()>0;
        cursor.close();
        return result;
    }
    // Checks that password entry matches username entry in database
    public boolean checkPassword(String username, String password) {
        SQLiteDatabase fitnessAppDB = this.getReadableDatabase();
        Cursor cursor = fitnessAppDB.rawQuery("Select * from " + USER_DATA_TABLE +
                " where " + COLUMN_USERNAME + " = ? and " + COLUMN_PASSWORD + " = ?", new String[] {username, password});
        boolean result = cursor.getCount()>0;
        cursor.close();
        return result;
    }


    // Database methods for user profile activity
    // Retrieves current user info from the database
    public User getCurrentUserInfo() {
        SQLiteDatabase fitnessAppDB = this.getReadableDatabase();
        Cursor cursor = fitnessAppDB.rawQuery("Select * from " + USER_DATA_TABLE + " where " + COLUMN_USER_ID + " = ?",  new String[] {String.valueOf(getUserID(User.currentUser))});
        cursor.moveToFirst();
        String gender = cursor.getString(5);
        int age = cursor.getInt(6);
        int height = cursor.getInt(7);
        String fitnessPlan = cursor.getString(8);
        double bmr = cursor.getDouble(9);
        double activityLevel = cursor.getDouble(10);
        String lastEvaluationDate = cursor.getString(11);
        double currentWeight = cursor.getDouble(12);
        double bmrAdjustment = cursor.getDouble(13);
        int lastWeekExercise = cursor.getInt(14);
        int calorieDifference = cursor.getInt(15);
        double weightChange = cursor.getDouble(16);

        return new User(gender, age, height, fitnessPlan, bmr, activityLevel, lastEvaluationDate,
                currentWeight, bmrAdjustment, lastWeekExercise, calorieDifference, weightChange);
    }
    // Updates current user info in the database
    public boolean updateUserInfo(User user) {

        SQLiteDatabase fitnessAppDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_ID, getUserID(User.currentUser));
        cv.put(COLUMN_AGE, user.getAge());
        cv.put(COLUMN_GENDER, user.getGender());
        cv.put(COLUMN_HEIGHT, user.getHeight());
        cv.put(COLUMN_FITNESS_PLAN, user.getFitnessPlan());
        cv.put(COLUMN_BMR, user.getBMR());
        cv.put(COLUMN_ACTIVITY_LEVEL, user.getActivityLevel());
        cv.put(COLUMN_FITNESS_AND_WEIGHT_EVALUATION_DATE, user.getLastEvaluationDate());
        cv.put(COLUMN_CURRENT_WEIGHT, user.getCurrentWeight());
        cv.put(COLUMN_BMR_ADJUSTMENT, user.getCurrentWeight());
        cv.put(COLUMN_LAST_WEEK_EXERCISE_TIME, user.getLastWeekExerciseTime());
        cv.put(COLUMN_CALORIE_DIFFERENCE_FROM_LAST_WEEK, user.getCalorieDifferenceFromGoal());
        cv.put(COLUMN_WEIGHT_CHANGE, user.getWeightChange());

        long update = fitnessAppDB.update(USER_DATA_TABLE, cv, COLUMN_USER_ID + " = " + getUserID(User.currentUser), null);
        return update != -1;
    }
    // Retrieves at least 1 week of exercises
    public ArrayList<Exercise> getLastWeekExercises() {
        ArrayList<Exercise> lastWeekExercises = new ArrayList<>();
        SQLiteDatabase fitnessAppDB = this.getReadableDatabase();
        String[] columns = {COLUMN_EXERCISE_TYPE, COLUMN_EXERCISE_TIME, COLUMN_DATE};
        Cursor cursor = fitnessAppDB.query(EXERCISE_DATA_TABLE, columns, COLUMN_USER_ID + " = " + getUserID(User.currentUser), null, null, null, COLUMN_EXERCISE_ID);
        int i = 0;

        if(cursor.moveToLast()) {
            do {
                Exercise exercise = new Exercise(cursor.getString(2), cursor.getString(1), cursor.getInt(0));
                lastWeekExercises.add(exercise);
                i++;
            } while(cursor.moveToPrevious() && i <= 7);
        } else {
            cursor.close();
            return null;
        }
        cursor.close();
        return lastWeekExercises;
    }
    // Retrieves at least 2 weeks of weights
    public ArrayList<Weight> getLastTwoWeeksWeights() {
        ArrayList<Weight> lastTwoWeeksWeights = new ArrayList<>();
        SQLiteDatabase fitnessAppDB = this.getReadableDatabase();
        String[] columns = {COLUMN_WEIGHT, COLUMN_DATE};
        Cursor cursor = fitnessAppDB.query(WEIGHT_DATA_TABLE, columns, COLUMN_USER_ID + " = " + getUserID(User.currentUser), null, null, null, COLUMN_WEIGHT_ID);
        int i = 0;

        if(cursor.moveToLast()) {
            do {
                Weight weight = new Weight(cursor.getDouble(0), cursor.getString(1));
                lastTwoWeeksWeights.add(weight);
                i++;
            } while(cursor.moveToPrevious() && i <= 14);
        } else {
            cursor.close();
            return null;
        }
        cursor.close();
        return lastTwoWeeksWeights;
    }
    // Retrieves at least 1 week of diet data
    public ArrayList<Diet> getLastWeekDiets() {
        ArrayList<Diet> lastWeekDiets = new ArrayList<>();
        SQLiteDatabase fitnessAppDB = this.getReadableDatabase();
        String[] columns = {COLUMN_DATE};
        Cursor cursor = fitnessAppDB.query(DIET_DATA_TABLE, columns, COLUMN_USER_ID + " = " + getUserID(User.currentUser), null, null, null, COLUMN_DIET_ID);
        int i = 0;

        if(cursor.moveToLast()) {
            do {
                Diet diet = new Diet(cursor.getString(2));
                lastWeekDiets.add(diet);
                i++;
            } while(cursor.moveToPrevious() && i <= 7);
        } else {
            cursor.close();
            return null;
        }
        cursor.close();
        return lastWeekDiets;
    }


    // Database methods for weight activity

    // Checks database for weight table data from the current day
    public boolean checkForWeightData() {
        SQLiteDatabase weightTrackerDB = this.getReadableDatabase();
        Cursor cursor = weightTrackerDB.rawQuery("Select * from " + WEIGHT_DATA_TABLE + " where " + COLUMN_USER_ID + " = " + getUserID(User.currentUser) + " and " + COLUMN_DATE + " = ?", new String[] {String.valueOf(LocalDate.now())});
        boolean result = cursor.getCount()>0;
        cursor.close();
        return result;
    }
    // Inserts a weight into the database
    public boolean addOneWeight(Weight weight) {

        SQLiteDatabase weightTrackerDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_ID, getUserID(User.currentUser));
        cv.put(COLUMN_WEIGHT, weight.getWeight());
        cv.put(COLUMN_DATE, weight.getDate());

        long insert = weightTrackerDB.insert(WEIGHT_DATA_TABLE, null, cv);
        return insert != -1;
    }
    // Updates the current date's data
    public boolean updateWeight(Weight weight) {

        SQLiteDatabase weightTrackerDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_ID, getUserID(User.currentUser));
        cv.put(COLUMN_WEIGHT, weight.getWeight());
        cv.put(COLUMN_DATE, weight.getDate());

        long update = weightTrackerDB.update(WEIGHT_DATA_TABLE, cv, COLUMN_USER_ID + " = " + getUserID(User.currentUser) + " and " + COLUMN_DATE + " = ?", new String[] {String.valueOf(LocalDate.now())});
        return update != -1;
    }
    // Generates a list of weight data to use in the weight recyclerview
    public ArrayList<Weight> getWeightList() {

        ArrayList<Weight> weightList = new ArrayList<>();

        SQLiteDatabase weightTrackerDB = this.getReadableDatabase();

        Cursor cursor = weightTrackerDB.rawQuery("Select " + COLUMN_DATE + ", " + COLUMN_WEIGHT + " from " + WEIGHT_DATA_TABLE + " where " + COLUMN_USER_ID + " = " + getUserID(User.currentUser), null);

        if(cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0);
                float weight = cursor.getFloat(1);

                Weight weightObj = new Weight(weight, date);
                weightList.add(weightObj);

            } while(cursor.moveToNext());
        } else {
            // Empty List
        }
        cursor.close();
        return weightList;
    }


    // Database methods for food and diet data

    // Checks database for diet table data from the current day
    public boolean checkForDietData() {
        SQLiteDatabase fitnessAppDB = this.getReadableDatabase();
        Cursor cursor = fitnessAppDB.rawQuery("Select * from " + DIET_DATA_TABLE + " where " + COLUMN_USER_ID + " = " + getUserID(User.currentUser) + " and " + COLUMN_DATE + " = ?", new String[] {String.valueOf(LocalDate.now())});
        boolean result = cursor.getCount()>0;
        cursor.close();
        return result;
    }
    // Inserts a diet into the database
    public boolean addOneDiet() {
        SQLiteDatabase fitnessAppDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        
        cv.put(COLUMN_USER_ID, getUserID(User.currentUser));
        cv.put(COLUMN_DATE, LocalDate.now().toString());
        
        long insert = fitnessAppDB.insert(DIET_DATA_TABLE, null, cv);
        return insert != -1;
    }
    // Inserts a food into the database
    public boolean addOneFood(Food food) {
        SQLiteDatabase fitnessAppDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_ID, getUserID(User.currentUser));
        cv.put(COLUMN_FOOD, food.getName());
        cv.put(COLUMN_DATE, LocalDate.now().toString());
        cv.put(COLUMN_CALORIES, food.getCalories());
        cv.put(COLUMN_PROTEIN, food.getProtein());
        cv.put(COLUMN_CARB, food.getCarbs());
        cv.put(COLUMN_FAT, food.getFat());

        long insert = fitnessAppDB.insert(FOOD_DATA_TABLE, null, cv);
        return insert != -1;
    }
    // Generates a list of diet data to use in the diet recyclerview
    public ArrayList<Diet> getDietList() {
        ArrayList<Diet> dietList = new ArrayList<>();
        SQLiteDatabase fitnessAppDB = this.getReadableDatabase();
        
        Cursor cursor = fitnessAppDB.rawQuery("Select " + COLUMN_DATE + " from " + DIET_DATA_TABLE + " where " + COLUMN_USER_ID + " = " + getUserID(User.currentUser), null);

        if(cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0);
                ArrayList<Food> foodList = new ArrayList<>();

                Cursor cursorTwo = fitnessAppDB.rawQuery("Select " + COLUMN_FOOD + ", " + COLUMN_CALORIES + ", " + COLUMN_PROTEIN + ", " + COLUMN_CARB + ", " + COLUMN_FAT + " from " + FOOD_DATA_TABLE + " where " + COLUMN_USER_ID + " = " + getUserID(User.currentUser) + " and " + COLUMN_DATE + " = ?", new String[] {date});

                if(cursorTwo.moveToFirst()) {
                    do {
                        String foodName = cursorTwo.getString(0);
                        int calories = cursorTwo.getInt(1);
                        int protein = cursorTwo.getInt(2);
                        int carbs = cursorTwo.getInt(3);
                        int fat = cursorTwo.getInt(4);

                        Food food = new Food(foodName, calories, protein, carbs, fat);
                        foodList.add(food);

                    } while(cursorTwo.moveToNext());
                }

                Diet dietObj = new Diet(date, foodList);
                dietList.add(dietObj);

            } while(cursor.moveToNext());
        }

        cursor.close();
        return dietList;
    }
    // Generates a list of food data to use in the food recyclerview
    public ArrayList<Food> getFoodList() {
        ArrayList<Food> foodList = new ArrayList<>();
        SQLiteDatabase fitnessAppDB = this.getReadableDatabase();

        Cursor cursor = fitnessAppDB.rawQuery("Select * from " + FOOD_DATA_TABLE + " where " + COLUMN_USER_ID + " = " + getUserID(User.currentUser) + " and " + COLUMN_DATE + " = ?", new String[] {Food.dateClicked});

        if(cursor.moveToFirst()) {
            do {

                String foodName = cursor.getString(2);
                int calories = cursor.getInt(3);
                int protein = cursor.getInt(4);
                int carbs = cursor.getInt(5);
                int fat = cursor.getInt(6);

                Food food = new Food(foodName, calories, protein, carbs, fat);
                foodList.add(food);

            } while(cursor.moveToNext());
        } else {
            cursor.close();
            return  foodList;
        }

        cursor.close();
        
        ArrayList<Food> searchList = new ArrayList<>();
        searchList.addAll(foodList);
        ArrayList<Food> sortedFoodList = new ArrayList<>();

        while(sortedFoodList.size() < foodList.size()) {
            Food lowestValue = searchList.get(0);
            for(int i = 0; i < searchList.size(); i++) {
                if(searchList.get(i).getName().compareTo(lowestValue.getName()) < 0) {
                    lowestValue = searchList.get(i);
                }
            }
            sortedFoodList.add(lowestValue);
            searchList.remove(lowestValue);
        }

        

        return sortedFoodList;
    }
    // Returns the amount of calories recorded for the given date
    public int getCaloriesFromDate(int entryID) {
        SQLiteDatabase fitnessAppDB = this.getReadableDatabase();
        String[] columns = {COLUMN_CALORIES};
        int totalCalories = 0;
        Cursor cursor = fitnessAppDB.query(FOOD_DATA_TABLE, columns, COLUMN_WEIGHT_ID + " = " + entryID, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                totalCalories += cursor.getInt(0);
            } while(cursor.moveToNext());
        }
        return totalCalories;
    }


    // Database methods for exercise data

    // Checks database for exercise table data from the current day
    public boolean checkForExerciseData() {
        SQLiteDatabase weightTrackerDB = this.getReadableDatabase();
        Cursor cursor = weightTrackerDB.rawQuery("Select * from " + EXERCISE_DATA_TABLE + " where " + COLUMN_USER_ID + " = " + getUserID(User.currentUser) + " and " + COLUMN_DATE + " = ?", new String[] {String.valueOf(LocalDate.now())});
        boolean result = cursor.getCount()>0;
        cursor.close();
        return result;
    }
    // Inserts an exercise into the database
    public boolean addOneExercise(Exercise exercise) {

        SQLiteDatabase weightTrackerDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_ID, getUserID(User.currentUser));
        cv.put(COLUMN_EXERCISE_TIME, exercise.getTime());
        cv.put(COLUMN_EXERCISE_TYPE, exercise.getExerciseType());
        cv.put(COLUMN_DATE, exercise.getDate());

        long insert = weightTrackerDB.insert(EXERCISE_DATA_TABLE, null, cv);
        return insert != -1;
    }
    // Updates the current date's data
    public boolean updateExercise(Exercise exercise) {

        SQLiteDatabase weightTrackerDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_ID, getUserID(User.currentUser));
        cv.put(COLUMN_EXERCISE_TIME, exercise.getTime());
        cv.put(COLUMN_EXERCISE_TYPE, exercise.getExerciseType());
        cv.put(COLUMN_DATE, exercise.getDate());

        long update = weightTrackerDB.update(EXERCISE_DATA_TABLE, cv, COLUMN_USER_ID + " = " + getUserID(User.currentUser) + " and " + COLUMN_DATE + " = " + LocalDate.now().toString(), null);
        return update != -1;
    }
    // Generates a list of exercise data to use in the exercise recyclerview
    public ArrayList<Exercise> getExerciseList() {

        ArrayList<Exercise> exerciseList = new ArrayList<>();

        SQLiteDatabase weightTrackerDB = this.getReadableDatabase();

        Cursor cursor = weightTrackerDB.rawQuery("Select " + COLUMN_DATE + ", " + COLUMN_EXERCISE_TYPE + ", " + COLUMN_EXERCISE_TIME + " from " + EXERCISE_DATA_TABLE + " where " + COLUMN_USER_ID + " = " + getUserID(User.currentUser), null);

        if(cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0);
                String exerciseType = cursor.getString(1);
                int exerciseTime = cursor.getInt(2);

                Exercise exerciseObj = new Exercise(date, exerciseType, exerciseTime);
                exerciseList.add(exerciseObj);

            } while(cursor.moveToNext());
        } else {
            // Empty List
        }

        cursor.close();
        return exerciseList;
    }


    // Database methods for administration

    public boolean deleteUser(User user) {
        return true;
    }
}
