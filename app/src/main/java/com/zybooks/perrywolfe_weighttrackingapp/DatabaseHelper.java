package com.zybooks.perrywolfe_weighttrackingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Constants

    // Login Table Constants
    public static final String LOGIN_TABLE = "LOGIN_TABLE";
    public static final String USER_ID = "ID";
    public static final String COLUMN_USERNAME = "USERNAME";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_PHONE_NUMBER = "PHONE_NUMBER";
    public static final String COLUMN_EMAIL = "EMAIL";

    // User Data Table Constants
    public static final String USER_DATA_TABLE = "USER_DATA_TABLE";
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
    public static final String COLUMN_WEIGHT = "WEIGHT";

    // Diet Data Table Constants
    public static final String DIET_DATA_TABLE = "DIET_DATA_TABLE";

    // Food Data Table Constants
    public static final String FOOD_DATA_TABLE = "FOOD_DATA_TABLE";
    public static final String COLUMN_FOOD = "FOOD";
    public static final String COLUMN_CALORIES = "CALORIES";
    public static final String COLUMN_PROTEIN = "PROTEIN";
    public static final String COLUMN_CARB = "CARB";
    public static final String COLUMN_FAT = "FAT";

    // Exercise Data Table Constants
    public static final String EXERCISE_DATA_TABLE = "EXERCISE_DATA_TABLE";
    public static final String COLUMN_EXERCISE_TIME = "EXERCISE_TIME";
    public static final String COLUMN_EXERCISE_TYPE = "EXERCISE_TYPE";

    // The Date Is Shared Between Three Tables
    public static final String COLUMN_DATE = "DATE";

    // Default constructor for database helper
    public DatabaseHelper(@Nullable Context context) {
        super(context, "Fitness Application Database", null, 1);
    }

    // Called the first time the database is accessed. Generates all database tables
    @Override
    public void onCreate(SQLiteDatabase fitnessAppDB) {

        String loginTable = "CREATE TABLE " + LOGIN_TABLE + " (" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_PHONE_NUMBER + " TEXT, " +
                COLUMN_EMAIL + " TEXT)";
        fitnessAppDB.execSQL(loginTable);

        String userDataTable = "CREATE TABLE " + USER_DATA_TABLE + " (" +
                USER_ID + " INTEGER, " +
                COLUMN_GENDER + " TEXT, " +
                COLUMN_AGE + " INTEGER, " +
                COLUMN_HEIGHT + " DOUBLE, " +
                COLUMN_FITNESS_PLAN + " TEXT, " +
                COLUMN_BMR + " DOUBLE, " +
                COLUMN_ACTIVITY_LEVEL + " DOUBLE, " +
                COLUMN_FITNESS_AND_WEIGHT_EVALUATION_DATE + " TEXT, " +
                COLUMN_CURRENT_WEIGHT + " DOUBLE, " +
                COLUMN_BMR_ADJUSTMENT + " DOUBLE, " +
                COLUMN_LAST_WEEK_EXERCISE_TIME + " INTEGER, " +
                COLUMN_CALORIE_DIFFERENCE_FROM_LAST_WEEK + " DOUBLE, " +
                COLUMN_WEIGHT_CHANGE + " DOUBLE)";
        fitnessAppDB.execSQL(userDataTable);

        String weightDataTable = "CREATE TABLE " + WEIGHT_DATA_TABLE + " (" +
                USER_ID + " INTEGER, " +
                COLUMN_WEIGHT + " DOUBLE, " +
                COLUMN_DATE + " TEXT)";
        fitnessAppDB.execSQL(weightDataTable);

        String dietDataTable = "CREATE TABLE " + DIET_DATA_TABLE + " (" +
                USER_ID + " INTEGER, " +
                COLUMN_DATE + " TEXT)";
        fitnessAppDB.execSQL(dietDataTable);

        String foodDataTable = "CREATE TABLE " + FOOD_DATA_TABLE + " (" +
                USER_ID + " INTEGER, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_FOOD + " TEXT, " +
                COLUMN_CALORIES + " INTEGER, " +
                COLUMN_PROTEIN + " INTEGER, " +
                COLUMN_CARB + " INTEGER, " +
                COLUMN_FAT + " INTEGER)";
        fitnessAppDB.execSQL(foodDataTable);

        String exerciseDataTable = "CREATE TABLE " + EXERCISE_DATA_TABLE + " (" +
                USER_ID + " INTEGER, " +
                COLUMN_EXERCISE_TIME + " INTEGER, " +
                COLUMN_EXERCISE_TYPE + " TEXT, " +
                COLUMN_DATE + " TEXT)";
        fitnessAppDB.execSQL(exerciseDataTable);
    }

    // Called whenever a new version of the application is available
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    // Database methods

    // Database methods for getting a single data entry
    public String getStringData(String column, String table) {
        SQLiteDatabase weightTrackerDB = this.getReadableDatabase();
        Cursor cursor = weightTrackerDB.rawQuery("Select " + column + " from " + table + " where " + USER_ID + " = " + getUserID(User.currentUser), null);
        cursor.moveToFirst();
        String data = cursor.getString(0);
        cursor.close();
        return data;
    }
    public int getIntData(String column, String table) {
        SQLiteDatabase weightTrackerDB = this.getReadableDatabase();
        Cursor cursor = weightTrackerDB.rawQuery("Select " + column + " from " + table + " where " + USER_ID + " = " + getUserID(User.currentUser), null);
        cursor.moveToFirst();
        int data = cursor.getInt(0);
        cursor.close();
        return data;
    }

    // Database methods for new users/login
    public boolean addOneUser(User user) {
        SQLiteDatabase weightTrackerDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USERNAME, user.getUsername());
        cv.put(COLUMN_PASSWORD, user.getPassword());
        cv.put(COLUMN_EMAIL, user.getEmail());
        cv.put(COLUMN_PHONE_NUMBER, user.getPhoneNumber());

        long insert = weightTrackerDB.insert(LOGIN_TABLE, null, cv);
        return insert != -1;
    }
    public boolean checkUsername(String username) {
        SQLiteDatabase weightTrackerDB = this.getReadableDatabase();
        Cursor cursor = weightTrackerDB.rawQuery("Select * from " + LOGIN_TABLE + " where " + COLUMN_USERNAME + " = ?", new String[] {username});
        boolean result = cursor.getCount()>0;
        cursor.close();
        return result;
    }
    public boolean checkPassword(String username, String password) {
        SQLiteDatabase weightTrackerDB = this.getReadableDatabase();
        Cursor cursor = weightTrackerDB.rawQuery("Select * from " + LOGIN_TABLE + " where " + COLUMN_USERNAME + " = ? and " + COLUMN_PASSWORD + " = ?", new String[] {username, password});
        boolean result = cursor.getCount()>0;
        cursor.close();
        return result;
    }
    public int getUserID(String username) {
        SQLiteDatabase weightTrackerDB = this.getReadableDatabase();
        Cursor cursor = weightTrackerDB.rawQuery("Select " + USER_ID + " from " + LOGIN_TABLE + " where " + COLUMN_USERNAME + " = ?", new String[] {username});
        if(cursor.moveToFirst()) {
            int result = cursor.getInt(0);
            cursor.close();
            return result;
        } else {
            cursor.close();
            return -1;
        }
    }

    // Database methods for existing user info
    public boolean checkUserInfo(int id) {
        SQLiteDatabase weightTrackerDB = this.getReadableDatabase();
        Cursor cursor = weightTrackerDB.rawQuery("Select " + USER_ID + " from " + USER_DATA_TABLE + " where " + USER_ID + " = ?", new String[] {String.valueOf(id)});
        boolean result = cursor.getCount()>0;
        cursor.close();
        return result;
    }
    // Inserts user info into the database
    public boolean addUserInfo(UserInfo userInfo) {

        SQLiteDatabase weightTrackerDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(USER_ID, getUserID(User.currentUser));
        cv.put(COLUMN_AGE, userInfo.getAge());
        cv.put(COLUMN_GENDER, userInfo.getGender());
        cv.put(COLUMN_HEIGHT, userInfo.getHeight());
        cv.put(COLUMN_FITNESS_PLAN, userInfo.getFitnessPlan());
        cv.put(COLUMN_BMR, userInfo.getBMR());
        cv.put(COLUMN_ACTIVITY_LEVEL, userInfo.getActivityLevel());
        cv.put(COLUMN_FITNESS_AND_WEIGHT_EVALUATION_DATE, userInfo.getLastEvaluationDate());
        cv.put(COLUMN_CURRENT_WEIGHT, userInfo.getCurrentWeight());
        cv.put(COLUMN_BMR_ADJUSTMENT, userInfo.getBmrAdjustment());
        cv.put(COLUMN_LAST_WEEK_EXERCISE_TIME, userInfo.getLastWeekExerciseTime());
        cv.put(COLUMN_CALORIE_DIFFERENCE_FROM_LAST_WEEK, userInfo.getCalorieDifferenceFromGoal());
        cv.put(COLUMN_WEIGHT_CHANGE, userInfo.getWeightChange());

        long insert = weightTrackerDB.insert(USER_DATA_TABLE, null, cv);
        return insert != -1;
    }
    // Creates Diet object with current date's data
    public UserInfo getCurrentUserInfo() {
        SQLiteDatabase weightTrackerDB = this.getReadableDatabase();
        Cursor cursor = weightTrackerDB.rawQuery("Select " + COLUMN_AGE + ", " + COLUMN_GENDER + ", " + COLUMN_HEIGHT + ", " + COLUMN_FITNESS_PLAN + ", " + COLUMN_BMR + ", " + COLUMN_ACTIVITY_LEVEL + ", " + COLUMN_FITNESS_AND_WEIGHT_EVALUATION_DATE + ", " + COLUMN_CURRENT_WEIGHT + ", " + COLUMN_BMR_ADJUSTMENT + ", " + COLUMN_LAST_WEEK_EXERCISE_TIME + ", " + COLUMN_CALORIE_DIFFERENCE_FROM_LAST_WEEK + ", " + COLUMN_WEIGHT_CHANGE + " from " + USER_DATA_TABLE + " where " + USER_ID + " = " + getUserID(User.currentUser), null);
        cursor.moveToFirst();
        return new UserInfo(cursor.getString(1), 0, 2, cursor.getString(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getString(6), cursor.getDouble(7), cursor.getDouble(8), cursor.getInt(9), cursor.getDouble(10), cursor.getDouble(11));
    }
    public boolean updateUserInfo(UserInfo userInfo) {

        SQLiteDatabase weightTrackerDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(USER_ID, getUserID(User.currentUser));
        cv.put(COLUMN_AGE, userInfo.getAge());
        cv.put(COLUMN_GENDER, userInfo.getGender());
        cv.put(COLUMN_HEIGHT, userInfo.getHeight());
        cv.put(COLUMN_FITNESS_PLAN, userInfo.getFitnessPlan());
        cv.put(COLUMN_BMR, userInfo.getBMR());
        cv.put(COLUMN_ACTIVITY_LEVEL, userInfo.getActivityLevel());
        cv.put(COLUMN_FITNESS_AND_WEIGHT_EVALUATION_DATE, userInfo.getLastEvaluationDate());
        cv.put(COLUMN_CURRENT_WEIGHT, userInfo.getCurrentWeight());
        cv.put(COLUMN_BMR_ADJUSTMENT, userInfo.getCurrentWeight());
        cv.put(COLUMN_LAST_WEEK_EXERCISE_TIME, userInfo.getLastWeekExerciseTime());
        cv.put(COLUMN_CALORIE_DIFFERENCE_FROM_LAST_WEEK, userInfo.getCalorieDifferenceFromGoal());
        cv.put(COLUMN_WEIGHT_CHANGE, userInfo.getWeightChange());

        long update = weightTrackerDB.update(USER_DATA_TABLE, cv, USER_ID + " = " + getUserID(User.currentUser), null);
        return update != -1;
    }


    // Database methods for weight data

    // Checks database for weight table data from the current day
    public boolean checkForWeightData() {
        SQLiteDatabase weightTrackerDB = this.getReadableDatabase();
        Cursor cursor = weightTrackerDB.rawQuery("Select * from " + WEIGHT_DATA_TABLE + " where " + USER_ID + " = " + getUserID(User.currentUser) + " and " + COLUMN_DATE + " = ?", new String[] {String.valueOf(LocalDate.now())});
        boolean result = cursor.getCount()>0;
        cursor.close();
        return result;
    }
    // Inserts a weight into the database
    public boolean addOneWeight(Weight weight) {

        SQLiteDatabase weightTrackerDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(USER_ID, getUserID(User.currentUser));
        cv.put(COLUMN_WEIGHT, weight.getWeight());
        cv.put(COLUMN_DATE, weight.getDate());

        long insert = weightTrackerDB.insert(WEIGHT_DATA_TABLE, null, cv);
        return insert != -1;
    }
    // Updates the current date's data
    public boolean updateWeight(Weight weight) {

        SQLiteDatabase weightTrackerDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(USER_ID, getUserID(User.currentUser));
        cv.put(COLUMN_WEIGHT, weight.getWeight());
        cv.put(COLUMN_DATE, weight.getDate());

        long update = weightTrackerDB.update(WEIGHT_DATA_TABLE, cv, USER_ID + " = " + getUserID(User.currentUser) + " and " + COLUMN_DATE + " = ?", new String[] {String.valueOf(LocalDate.now())});
        return update != -1;
    }
    // Generates a list of weight data to use in the weight recyclerview
    public ArrayList<Weight> getWeightList() {

        ArrayList<Weight> weightList = new ArrayList<>();

        SQLiteDatabase weightTrackerDB = this.getReadableDatabase();

        Cursor cursor = weightTrackerDB.rawQuery("Select " + COLUMN_DATE + ", " + COLUMN_WEIGHT + " from " + WEIGHT_DATA_TABLE + " where " + USER_ID + " = " + getUserID(User.currentUser), null);

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
    /* Returns the difference in average weight between last week and the week before to determine
     if weight is trending up, down, or staying the same */
    public double getWeeklyWeightAverageDifference() {

        SQLiteDatabase weightTrackerDB = this.getReadableDatabase();
        Cursor cursor = weightTrackerDB.rawQuery("Select " + COLUMN_DATE + ", " + COLUMN_WEIGHT + " from " + WEIGHT_DATA_TABLE + " where " + USER_ID + " = " + getUserID(User.currentUser), null);
        double firstWeeklyRollingAverage = 0.0;
        double secondWeeklyRollingAverage = 0.0;
        int count = 0;

        if(cursor.moveToLast()) {
            do {

                String date = cursor.getString(0);
                double weight = cursor.getDouble(1);

                for(int i = 1; i <= 7; i++) {
                    if(date.equals(LocalDate.now().plus(-i, ChronoUnit.DAYS).toString())) {
                        count++;
                        firstWeeklyRollingAverage += weight;
                    }
                }

            } while(cursor.moveToPrevious());
        } else {
            cursor.close();
            return -100.0;
        }
        if(count == 0) {
            return -100.0;
        }
        firstWeeklyRollingAverage /= count;

        count = 0;
        if(cursor.moveToLast()) {
            do {

                String date = cursor.getString(0);
                float weight = cursor.getFloat(1);

                for(int i = 8; i <= 14; i++) {
                    if(date.equals(LocalDate.now().plus(-i, ChronoUnit.DAYS).toString())) {
                        count++;
                        secondWeeklyRollingAverage += weight;
                    }
                }

            } while(cursor.moveToPrevious());
        } else {
            cursor.close();
            return -100.0;
        }
        if(count == 0) {
            return -100.0;
        }
        secondWeeklyRollingAverage /= count;

        cursor.close();
        return firstWeeklyRollingAverage - secondWeeklyRollingAverage;
    }


    // Database methods for food and diet data

    // Checks database for diet table data from the current day
    public boolean checkForDietData() {
        SQLiteDatabase fitnessAppDB = this.getReadableDatabase();
        Cursor cursor = fitnessAppDB.rawQuery("Select * from " + DIET_DATA_TABLE + " where " + USER_ID + " = " + getUserID(User.currentUser) + " and " + COLUMN_DATE + " = ?", new String[] {String.valueOf(LocalDate.now())});
        boolean result = cursor.getCount()>0;
        cursor.close();
        return result;
    }
    // Inserts a diet into the database
    public boolean addOneDiet() {
        SQLiteDatabase fitnessAppDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        
        cv.put(USER_ID, getUserID(User.currentUser));
        cv.put(COLUMN_DATE, LocalDate.now().toString());
        
        long insert = fitnessAppDB.insert(DIET_DATA_TABLE, null, cv);
        return insert != -1;
    }
    // Inserts a food into the database
    public boolean addOneFood(Food food) {
        SQLiteDatabase fitnessAppDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(USER_ID, getUserID(User.currentUser));
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
        
        Cursor cursor = fitnessAppDB.rawQuery("Select " + COLUMN_DATE + " from " + DIET_DATA_TABLE + " where " + USER_ID + " = " + getUserID(User.currentUser), null);

        if(cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0);
                ArrayList<Food> foodList = new ArrayList<>();

                Cursor cursorTwo = fitnessAppDB.rawQuery("Select " + COLUMN_FOOD + ", " + COLUMN_CALORIES + ", " + COLUMN_PROTEIN + ", " + COLUMN_CARB + ", " + COLUMN_FAT + " from " + FOOD_DATA_TABLE + " where " + USER_ID + " = " + getUserID(User.currentUser) + " and " + COLUMN_DATE + " = ?", new String[] {date});

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

        Cursor cursor = fitnessAppDB.rawQuery("Select * from " + FOOD_DATA_TABLE + " where " + USER_ID + " = " + getUserID(User.currentUser) + " and " + COLUMN_DATE + " = ?", new String[] {Food.dateClicked});

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
    // Returns daily average of calories from entries within the last week
    public double getLastWeekAverageCalories() {

        double totalCalories = 0;
        double numberOfEntries = 0;
        boolean flag = true;

        SQLiteDatabase fitnessAppDB = this.getReadableDatabase();
        Cursor cursor = fitnessAppDB.rawQuery("Select " + COLUMN_DATE + " from " + DIET_DATA_TABLE + " where " + USER_ID + " = " + getUserID(User.currentUser), null);

        if(cursor.moveToLast()) {
            do {
                String date = cursor.getString(0);

                for(int i = 1; i < 7; i++) {
                    if(date.equals(LocalDate.now().plus(-i, ChronoUnit.DAYS).toString())) {

                        Cursor cursorTwo = fitnessAppDB.rawQuery("Select " + COLUMN_CALORIES + " from " + FOOD_DATA_TABLE + " where " + USER_ID + " = " + getUserID(User.currentUser) + " and " + COLUMN_DATE + " = ?", new String[] {date});

                        if(cursorTwo.moveToFirst()) {
                            do {
                                totalCalories += cursorTwo.getInt(1);
                            } while (cursorTwo.moveToNext());
                            numberOfEntries++;
                        }
                        break;
                    } else {
                        flag = false;
                    }
                }
                if(numberOfEntries > 6) {
                    break;
                }
            } while(cursor.moveToPrevious() && flag);
        } else {
            cursor.close();
            return 0;
        }

        cursor.close();

        return totalCalories / numberOfEntries;
    }


    // Database methods for exercise data

    // Checks database for exercise table data from the current day
    public boolean checkForExerciseData() {
        SQLiteDatabase weightTrackerDB = this.getReadableDatabase();
        Cursor cursor = weightTrackerDB.rawQuery("Select * from " + EXERCISE_DATA_TABLE + " where " + USER_ID + " = " + getUserID(User.currentUser) + " and " + COLUMN_DATE + " = ?", new String[] {String.valueOf(LocalDate.now())});
        boolean result = cursor.getCount()>0;
        cursor.close();
        return result;
    }
    // Inserts an exercise into the database
    public boolean addOneExercise(Exercise exercise) {

        SQLiteDatabase weightTrackerDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(USER_ID, getUserID(User.currentUser));
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

        cv.put(USER_ID, getUserID(User.currentUser));
        cv.put(COLUMN_EXERCISE_TIME, exercise.getTime());
        cv.put(COLUMN_EXERCISE_TYPE, exercise.getExerciseType());
        cv.put(COLUMN_DATE, exercise.getDate());

        long update = weightTrackerDB.update(EXERCISE_DATA_TABLE, cv, USER_ID + " = " + getUserID(User.currentUser) + " and " + COLUMN_DATE + " = " + LocalDate.now().toString(), null);
        return update != -1;
    }
    // Generates a list of exercise data to use in the exercise recyclerview
    public ArrayList<Exercise> getExerciseList() {

        ArrayList<Exercise> exerciseList = new ArrayList<>();

        SQLiteDatabase weightTrackerDB = this.getReadableDatabase();

        Cursor cursor = weightTrackerDB.rawQuery("Select " + COLUMN_DATE + ", " + COLUMN_EXERCISE_TYPE + ", " + COLUMN_EXERCISE_TIME + " from " + EXERCISE_DATA_TABLE + " where " + USER_ID + " = " + getUserID(User.currentUser), null);

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
    // Returns total exercise time from entries within the last week
    public int getLastWeekExerciseTime() {

        ArrayList<Exercise> lastWeekExercises = new ArrayList<>();
        SQLiteDatabase fitnessAppDB = this.getReadableDatabase();
        Cursor cursor = fitnessAppDB.rawQuery("Select " + COLUMN_DATE + ", " + COLUMN_EXERCISE_TYPE + ", " + COLUMN_EXERCISE_TIME + " from " + EXERCISE_DATA_TABLE + " where " + USER_ID + " = " + getUserID(User.currentUser), null);

        if(cursor.moveToLast()) {
            do {
                int count = 0;
                String date = cursor.getString(0);
                String exerciseType = cursor.getString(1);
                int exerciseTime = cursor.getInt(2);

                for(int i = 1; i <= 7; i++) {
                    if(date.equals(LocalDate.now().plus(-i, ChronoUnit.DAYS).toString())) {
                        Exercise exercise = new Exercise(date, exerciseType, exerciseTime);
                        lastWeekExercises.add(exercise);
                    }
                }
                count++;
                if(count > 6) {
                    break;
                }
            } while(cursor.moveToPrevious());
        } else {
            cursor.close();
            return -100;
        }

        cursor.close();

        int timeExercisedLastWeek = 0;
        for(int i = 0; i < lastWeekExercises.size(); i++) {
            timeExercisedLastWeek += lastWeekExercises.get(i).getTime();
        }
        return timeExercisedLastWeek;
    }


    // Database methods for administration

    public boolean deleteUser(User user) {
        return true;
    }
}
