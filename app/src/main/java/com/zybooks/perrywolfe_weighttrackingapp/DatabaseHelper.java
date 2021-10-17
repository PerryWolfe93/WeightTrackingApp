package com.zybooks.perrywolfe_weighttrackingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.time.LocalDate;
import java.util.ArrayList;
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
    public static final String COLUMN_GOAL_WEIGHT = "GOAL_WEIGHT";
    public static final String COLUMN_FITNESS_PLAN = "FITNESS_PLAN";

    // Weight Data Table Constants
    public static final String WEIGHT_DATA_TABLE = "WEIGHT_DATA_TABLE";
    public static final String COLUMN_WEIGHT = "WEIGHT";

    // Diet Data Table Constants
    public static final String DIET_DATA_TABLE = "DIET_DATA_TABLE";
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
        super(context, "Weight Tracker Database", null, 1);
    }

    // Called the first time the database is accessed. Generates all database tables
    @Override
    public void onCreate(SQLiteDatabase weightTrackerDB) {

        String loginTable = "CREATE TABLE " + LOGIN_TABLE + " (" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_PHONE_NUMBER + " TEXT, " +
                COLUMN_EMAIL + " TEXT)";
        weightTrackerDB.execSQL(loginTable);

        String userDataTable = "CREATE TABLE " + USER_DATA_TABLE + " (" +
                USER_ID + " INTEGER, " +
                COLUMN_GENDER + " TEXT, " +
                COLUMN_AGE + " INTEGER, " +
                COLUMN_HEIGHT + " DOUBLE, " +
                COLUMN_GOAL_WEIGHT + " DOUBLE, " +
                COLUMN_FITNESS_PLAN + " TEXT)";
        weightTrackerDB.execSQL(userDataTable);

        String weightDataTable = "CREATE TABLE " + WEIGHT_DATA_TABLE + " (" +
                USER_ID + " INTEGER, " +
                COLUMN_WEIGHT + " DOUBLE, " +
                COLUMN_DATE + " TEXT)";
        weightTrackerDB.execSQL(weightDataTable);

        String dietDataTable = "CREATE TABLE " + DIET_DATA_TABLE + " (" +
                USER_ID + " INTEGER, " +
                COLUMN_CALORIES + " INTEGER, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_PROTEIN + " INTEGER, " +
                COLUMN_CARB + " INTEGER, " +
                COLUMN_FAT + " INTEGER)";
        weightTrackerDB.execSQL(dietDataTable);

        String exerciseDataTable = "CREATE TABLE " + EXERCISE_DATA_TABLE + " (" +
                USER_ID + " INTEGER, " +
                COLUMN_EXERCISE_TIME + " INTEGER, " +
                COLUMN_EXERCISE_TYPE + " TEXT, " +
                COLUMN_DATE + " TEXT)";
        weightTrackerDB.execSQL(exerciseDataTable);
    }

    // Called whenever a new version of the application is available
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    // Database methods

    // Database method for checking if a field is null
    public boolean checkForData(String column, String table) {
        SQLiteDatabase weightTrackerDB = this.getReadableDatabase();
        Cursor cursor = weightTrackerDB.rawQuery("Select " + column + " from " + table + " where " + USER_ID + " = " + getUserID(User.currentUser), null);
        cursor.moveToFirst();
        boolean result = cursor.getCount()>0;
        cursor.close();
        return result;
    }
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
    public double getFloatData(String column, String table) {
        SQLiteDatabase weightTrackerDB = this.getReadableDatabase();
        Cursor cursor = weightTrackerDB.rawQuery("Select " + column + " from " + table + " where " + USER_ID + " = " + getUserID(User.currentUser), null);
        cursor.moveToFirst();
        double data = cursor.getDouble(0);
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
        cv.put(COLUMN_GOAL_WEIGHT, userInfo.getGoalWeight());
        cv.put(COLUMN_FITNESS_PLAN, userInfo.getFitnessPlan());


        long insert = weightTrackerDB.insert(USER_DATA_TABLE, null, cv);
        return insert != -1;
    }
    // Creates Diet object with current date's data
    public UserInfo getCurrentUserInfo() {
        SQLiteDatabase weightTrackerDB = this.getReadableDatabase();
        Cursor cursor = weightTrackerDB.rawQuery("Select " + COLUMN_AGE + ", " + COLUMN_GENDER + ", " + COLUMN_HEIGHT + ", " + COLUMN_GOAL_WEIGHT + ", " + COLUMN_FITNESS_PLAN + " from " + USER_DATA_TABLE + " where " + USER_ID + " = " + getUserID(User.currentUser), null);
        cursor.moveToFirst();
        return new UserInfo(cursor.getString(1), 0, 2, cursor.getFloat(3), cursor.getString(4));
    }
    public boolean updateUserInfo(UserInfo userInfo) {

        SQLiteDatabase weightTrackerDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(USER_ID, getUserID(User.currentUser));
        cv.put(COLUMN_AGE, userInfo.getAge());
        cv.put(COLUMN_GENDER, userInfo.getGender());
        cv.put(COLUMN_HEIGHT, userInfo.getHeight());
        cv.put(COLUMN_GOAL_WEIGHT, userInfo.getGoalWeight());
        cv.put(COLUMN_FITNESS_PLAN, userInfo.getFitnessPlan());

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


    // Database methods for diet data

    // Checks database for diet table data from the current day
    public boolean checkForCalorieData() {
        SQLiteDatabase weightTrackerDB = this.getReadableDatabase();
        Cursor cursor = weightTrackerDB.rawQuery("Select * from " + DIET_DATA_TABLE + " where " + USER_ID + " = " + getUserID(User.currentUser) + " and " + COLUMN_DATE + " = ?", new String[] {String.valueOf(LocalDate.now())});
        boolean result = cursor.getCount()>0;
        cursor.close();
        return result;
    }
    // Creates Diet object with current date's data
    public Diet getCurrentDiet() {
        SQLiteDatabase weightTrackerDB = this.getReadableDatabase();
        Cursor cursor = weightTrackerDB.rawQuery("Select " + COLUMN_DATE + ", " + COLUMN_CALORIES + ", " + COLUMN_PROTEIN + ", " + COLUMN_CARB + ", " + COLUMN_FAT + " from " + DIET_DATA_TABLE + " where " + USER_ID + " = " + getUserID(User.currentUser) + " and " + COLUMN_DATE + " = ?", new String[] {String.valueOf(LocalDate.now())});
        cursor.moveToFirst();
        return new Diet(cursor.getString(0), 0, 0, 0, 0);
    }
    // Inserts a diet into the database
    public boolean addOneDiet(Diet diet) {
        SQLiteDatabase weightTrackerDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        
        cv.put(USER_ID, getUserID(User.currentUser));
        cv.put(COLUMN_CALORIES, diet.getCalories());
        cv.put(COLUMN_DATE, diet.getDate());
        cv.put(COLUMN_PROTEIN, diet.getProtein());
        cv.put(COLUMN_CARB, diet.getCarb());
        cv.put(COLUMN_FAT, diet.getFat());
        
        long insert = weightTrackerDB.insert(DIET_DATA_TABLE, null, cv);
        return insert != -1;
    }
    // Updates the current date's data
    public boolean updateDiet(Diet diet) {
        SQLiteDatabase weightTrackerDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(USER_ID, getUserID(User.currentUser));
        cv.put(COLUMN_CALORIES, diet.getCalories());
        cv.put(COLUMN_DATE, diet.getDate());
        cv.put(COLUMN_PROTEIN, diet.getProtein());
        cv.put(COLUMN_CARB, diet.getCarb());
        cv.put(COLUMN_FAT, diet.getFat());
        
        long update = weightTrackerDB.update(DIET_DATA_TABLE, cv, USER_ID + " = " + getUserID(User.currentUser) + " and " + COLUMN_DATE + " = ?", new String[] {String.valueOf(LocalDate.now())});
        return update != -1;
    }
    // Generates a list of diet data to use in the diet recyclerview
    public ArrayList<Diet> getDietList() {
        ArrayList<Diet> dietList = new ArrayList<>();
        SQLiteDatabase weightTrackerDB = this.getReadableDatabase();
        
        Cursor cursor = weightTrackerDB.rawQuery("Select " + COLUMN_DATE + ", " + COLUMN_CALORIES + ", " + COLUMN_PROTEIN + ", " + COLUMN_CARB + ", " + COLUMN_FAT + " from " + DIET_DATA_TABLE + " where " + USER_ID + " = " + getUserID(User.currentUser), null);
        
        if(cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0);
                int calories = cursor.getInt(1);
                int protein = cursor.getInt(2);
                int carb = cursor.getInt(3);
                int fat = cursor.getInt(4);

                Diet dietObj = new Diet(date, calories, protein, carb, fat);
                dietList.add(dietObj);

            } while(cursor.moveToNext());
        } else {
            // Empty cursor
        }
        cursor.close();
        return dietList;
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


    // Database methods for administration
    public boolean deleteUser(User user) {
        return true;
    }
}
