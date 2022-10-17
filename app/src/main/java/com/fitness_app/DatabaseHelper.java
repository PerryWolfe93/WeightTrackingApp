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

import com.fitness_app.object_classes.Journal;
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

    // Journal Data Table Constants
    public static final String JOURNAL_DATA_TABLE = "JOURNAL_DATA_TABLE";
    public static final String COLUMN_JOURNAL_ENTRY_ID = "JOURNAL_ENTRY_ID";
    public static final String COLUMN_JOURNAL_ENTRY = "JOURNAL_ENTRY";

    // Weight Data Table Constants
    public static final String WEIGHT_DATA_TABLE = "WEIGHT_DATA_TABLE";
    public static final String COLUMN_WEIGHT_ID = "WEIGHT_ID";
    public static final String COLUMN_WEIGHT = "WEIGHT";

    // Food Catalog Data Table Constants
    public static final String FOOD_CATALOG_DATA_TABLE = "FOOD_CATALOG_DATA_TABLE";
    public static final String COLUMN_FOOD_CATALOG_ID = "FOOD_CATALOG_ID";

    // Diet Data Table Constants
    public static final String DIET_DATA_TABLE = "DIET_DATA_TABLE";
    public static final String COLUMN_DIET_ID = "DIET_ID";

    // Food Data Table Constants
    public static final String FOOD_DATA_TABLE = "FOOD_DATA_TABLE";
    public static final String COLUMN_FOOD_ID = "FOOD_ID";
    public static final String COLUMN_FOOD_NAME = "NAME";
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

        String userDataTableIndex = "CREATE INDEX IDx1 ON " + USER_DATA_TABLE + "(" + COLUMN_USERNAME + ")";
        fitnessAppDB.execSQL(userDataTableIndex);

        String journalDataTable = "CREATE TABLE " + JOURNAL_DATA_TABLE + " (" +
                COLUMN_JOURNAL_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                COLUMN_USER_ID + " INTEGER NOT NULL, " +
                COLUMN_JOURNAL_ENTRY + " TEXT NOT NULL, " +
                COLUMN_DATE + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_USER_ID + ") " +
                "REFERENCES " + USER_DATA_TABLE + " (" + COLUMN_USER_ID + "))";
        fitnessAppDB.execSQL(journalDataTable);

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

        String foodCatalogDataTable = "CREATE TABLE " + FOOD_CATALOG_DATA_TABLE + " (" +
                COLUMN_FOOD_CATALOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                COLUMN_USER_ID + " INTEGER NOT NULL, " +
                COLUMN_FOOD_NAME + " TEXT NOT NULL, " +
                COLUMN_CALORIES + " TEXT NOT NULL, " +
                COLUMN_PROTEIN + " TEXT NOT NULL, " +
                COLUMN_CARB + " TEXT NOT NULL, " +
                COLUMN_FAT + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_USER_ID + ") " +
                "REFERENCES " + USER_DATA_TABLE + " (" + COLUMN_USER_ID + "))";
        fitnessAppDB.execSQL(foodCatalogDataTable);

        String foodCatalogDataTableIndex = "CREATE INDEX IDx2 ON " + FOOD_CATALOG_DATA_TABLE + "(" + COLUMN_USER_ID + ")";
        fitnessAppDB.execSQL(foodCatalogDataTableIndex);

        String foodDataTable = "CREATE TABLE " + FOOD_DATA_TABLE + " (" +
                COLUMN_FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_DIET_ID + " INTEGER NOT NULL, " +
                COLUMN_FOOD_NAME + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_DIET_ID + ") " +
                "REFERENCES " + DIET_DATA_TABLE + " (" + COLUMN_DIET_ID + "))";
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

    // Called whenever a new version of the database is available
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("FitnessApp", "onUpgrade invoked");
    }


    // Database methods

    // Database methods for new user activity


    // This method checks the database for the specified username,
    // returning true if the username exists or false if it does not
    public boolean checkUsername(String username) {
        SQLiteDatabase fitnessTrackerDB = this.getReadableDatabase();
        String[] tableColumns = {COLUMN_USERNAME};
        String whereClause = COLUMN_USERNAME + " = ?";
        String[] whereArgs = {username};
        Cursor cursor = fitnessTrackerDB.query(USER_DATA_TABLE, tableColumns, whereClause, whereArgs, null, null, null);
        boolean result = cursor.getCount()>0;
        cursor.close();
        return result;
    }

    // Adds new user to SQLite database
    public boolean addUser(User user) {
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


    // Database methods for login activity

    // This method checks that password entry matches username entry in database
    public boolean checkPassword(String username, String password) {
        SQLiteDatabase fitnessAppDB = this.getReadableDatabase();
        String[] tableColumns = {COLUMN_USERNAME};
        String whereClause = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] whereArgs = {username, password};
        Cursor cursor = fitnessAppDB.query(USER_DATA_TABLE, tableColumns, whereClause, whereArgs, null, null, null);
        boolean result = cursor.getCount()>0;
        cursor.close();
        return result;
    }


    // This method is used to get the current UserID from the SQLite database
    public int getUserID(String username) {
        SQLiteDatabase fitnessAppDB = this.getReadableDatabase();
        String[] tableColumns = {COLUMN_USER_ID};
        String whereClause = COLUMN_USERNAME + " = ?";
        String[] whereArgs = {username};
        Cursor cursor = fitnessAppDB.query(USER_DATA_TABLE, tableColumns, whereClause, whereArgs, null, null, null);
        if(cursor.moveToFirst()) {
            int result = cursor.getInt(0);
            cursor.close();
            return result;
        } else {
            cursor.close();
            return -1;
        }
    }


    // Database methods for user profile activity

    // Retrieves current user info from the database
    public User getCurrentUserInfo() {
        SQLiteDatabase fitnessAppDB = this.getReadableDatabase();
        String[] tableColumns = {COLUMN_GENDER, COLUMN_AGE, COLUMN_HEIGHT, COLUMN_FITNESS_PLAN, COLUMN_BMR, COLUMN_ACTIVITY_LEVEL, COLUMN_FITNESS_AND_WEIGHT_EVALUATION_DATE, COLUMN_CURRENT_WEIGHT, COLUMN_BMR_ADJUSTMENT, COLUMN_LAST_WEEK_EXERCISE_TIME, COLUMN_CALORIE_DIFFERENCE_FROM_LAST_WEEK, COLUMN_WEIGHT_CHANGE, COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_EMAIL, COLUMN_PHONE_NUMBER};
        String whereClause = COLUMN_USER_ID + " = ?";
        String[] whereArgs = {String.valueOf(User.userID)};
        Cursor cursor = fitnessAppDB.query(USER_DATA_TABLE, tableColumns, whereClause, whereArgs, null, null, null);

        if(cursor.moveToFirst()) {
            String gender = cursor.getString(0);
            int age = cursor.getInt(1);
            int height = cursor.getInt(2);
            String fitnessPlan = cursor.getString(3);
            double bmr = cursor.getDouble(4);
            double activityLevel = cursor.getDouble(5);
            String lastEvaluationDate = cursor.getString(6);
            double currentWeight = cursor.getDouble(7);
            double bmrAdjustment = cursor.getDouble(8);
            int lastWeekExercise = cursor.getInt(9);
            int calorieDifference = cursor.getInt(10);
            double weightChange = cursor.getDouble(11);
            String username = cursor.getString(12);
            String password = cursor.getString(13);
            String email = cursor.getString(14);
            String phoneNumber = cursor.getString(15);

            cursor.close();

            return new User(username, password, email, phoneNumber, gender, age, height, fitnessPlan, bmr, activityLevel, lastEvaluationDate, currentWeight, bmrAdjustment, lastWeekExercise, calorieDifference, weightChange);
        } else {
            cursor.close();
            return null;
        }
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


    // Database methods for journal activity

    // Inserts journal entry into the database
    public void addJournal(Journal journal) {

        SQLiteDatabase fitnessTrackerDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_ID, getUserID(User.currentUser));
        cv.put(COLUMN_JOURNAL_ENTRY, journal.getJournalEntry());
        cv.put(COLUMN_DATE, journal.getDate());

        fitnessTrackerDB.insert(JOURNAL_DATA_TABLE, null, cv);
    }
    // Gets today's user journal data
    public Journal getTodaysJournalEntry() {
        SQLiteDatabase fitnessTrackerDB = this.getReadableDatabase();
        String[] tableColumns = {COLUMN_JOURNAL_ENTRY};
        String whereClause = COLUMN_USER_ID + " = ? AND " + COLUMN_DATE + " = ?";
        String[] whereArgs = {String.valueOf(getUserID(User.currentUser)), java.time.LocalDate.now().toString()};
        Cursor cursor = fitnessTrackerDB.query(JOURNAL_DATA_TABLE, tableColumns, whereClause, whereArgs, null, null, null);

        Journal todaysJournal;
        if(cursor.moveToFirst()) {
            todaysJournal = new Journal(java.time.LocalDate.now().toString(), cursor.getString(0));
        } else {
            todaysJournal = new Journal(java.time.LocalDate.now().toString(), "Journal is empty");
        }
        cursor.close();
        return todaysJournal;
    }
    // Gets all user journal data
    public ArrayList<Journal> getJournalEntries() {
        SQLiteDatabase fitnessTrackerDB = this.getReadableDatabase();
        String[] tableColumns = {COLUMN_JOURNAL_ENTRY, COLUMN_DATE};
        String whereClause = COLUMN_USER_ID + " = ?";
        String[] whereArgs = {String.valueOf(getUserID(User.currentUser))};
        Cursor cursor = fitnessTrackerDB.query(JOURNAL_DATA_TABLE, tableColumns, whereClause, whereArgs, null, null, null);

        ArrayList<Journal> journalEntryList = new ArrayList<>();

        if(cursor.moveToFirst()) {
            do{
                String entry = cursor.getString(0);
                String date = cursor.getString(1);

                journalEntryList.add(new Journal(date, entry));
            } while(cursor.moveToNext());
        }
        cursor.close();
        return journalEntryList;
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

    // Gets all of the user's saved foods from the database
    public ArrayList<Food> getFoodCatalog() {
        ArrayList<Food> foodCatalog = new ArrayList<>();
        SQLiteDatabase fitnessAppDB = this.getReadableDatabase();
        String[] tableColumns = {COLUMN_FOOD_NAME, COLUMN_CALORIES, COLUMN_PROTEIN, COLUMN_CARB, COLUMN_FAT};
        String whereClause = COLUMN_USER_ID + " = ?";
        String[] whereArgs = {String.valueOf(User.userID)};
        Cursor cursor = fitnessAppDB.query(FOOD_CATALOG_DATA_TABLE, tableColumns, whereClause, whereArgs, null, null, COLUMN_FOOD_NAME);
        if(cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                int calories = cursor.getInt(1);
                int protein = cursor.getInt(2);
                int carbs = cursor.getInt(3);
                int fat = cursor.getInt(4);
                Food food = new Food(name, calories, protein, carbs, fat);
                foodCatalog.add(food);
            } while(cursor.moveToNext());
        } else {
            cursor.close();
            return null;
        }
        cursor.close();
        return foodCatalog;
    }

    public boolean addToCatalog(Food food) {
        SQLiteDatabase fitnessAppDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_ID, User.userID);
        cv.put(COLUMN_FOOD_NAME, food.getName());
        cv.put(COLUMN_CALORIES, food.getCalories());
        cv.put(COLUMN_PROTEIN, food.getProtein());
        cv.put(COLUMN_CARB, food.getCarbs());
        cv.put(COLUMN_FAT, food.getFat());

        long insert = fitnessAppDB.insert(FOOD_CATALOG_DATA_TABLE, null, cv);
        return insert != -1;
    }

    // Checks database for diet table data from the current day
    public boolean checkForDietData() {
        SQLiteDatabase fitnessAppDB = this.getReadableDatabase();
        Cursor cursor = fitnessAppDB.rawQuery("Select * from " + DIET_DATA_TABLE + " where " + COLUMN_USER_ID + " = " + getUserID(User.currentUser) + " and " + COLUMN_DATE + " = ?", new String[] {String.valueOf(LocalDate.now())});
        boolean result = cursor.getCount()>0;
        cursor.close();
        return result;
    }
    // Inserts a diet into the database
    public boolean addDiet() {
        SQLiteDatabase fitnessAppDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        
        cv.put(COLUMN_USER_ID, getUserID(User.currentUser));
        cv.put(COLUMN_DATE, LocalDate.now().toString());
        
        long insert = fitnessAppDB.insert(DIET_DATA_TABLE, null, cv);
        return insert != -1;
    }
    public int getTodaysDiet() {
        int todaysDietID;
        SQLiteDatabase fitnessAppDB = this.getReadableDatabase();
        String[] tableColumns = {COLUMN_DIET_ID};
        String whereClause = COLUMN_USER_ID + " = ? AND " + COLUMN_DATE + " = ?";
        String[] whereArgs = {String.valueOf(User.userID), LocalDate.now().toString()};
        Cursor cursor = fitnessAppDB.query(DIET_DATA_TABLE, tableColumns, whereClause, whereArgs, null, null, null);
        if(cursor.moveToFirst()) {
            todaysDietID = cursor.getInt(0);
        } else {
            cursor.close();
            todaysDietID = -1;
        }
        cursor.close();
        return todaysDietID;
    }
    // Inserts a food into the database
    public void addFood(String foodName) {
        SQLiteDatabase fitnessAppDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_DIET_ID, getTodaysDiet());
        cv.put(COLUMN_FOOD_NAME, foodName);

        fitnessAppDB.insert(FOOD_DATA_TABLE, null, cv);
    }
    // Generates a list of diet data to use in the diet recyclerview
    public ArrayList<Diet> getDietList() {
        ArrayList<Diet> dietList = new ArrayList<>();
        SQLiteDatabase fitnessAppDB = this.getReadableDatabase();

        String[] tableColumns = {COLUMN_DIET_ID, COLUMN_DATE};
        String whereClause = COLUMN_USER_ID + " = ?";
        String[] whereArgs = {String.valueOf(User.userID)};

        Cursor cursor = fitnessAppDB.query(DIET_DATA_TABLE, tableColumns, whereClause, whereArgs, null, null, null);

        if(cursor.moveToFirst()) {
            do {
                String date = cursor.getString(1);
                ArrayList<String> foodList = new ArrayList<>();

                String[] tableColumnsTwo = {COLUMN_FOOD_NAME};
                String whereClauseTwo = COLUMN_DIET_ID + " = ?";
                String[] whereArgsTwo = {String.valueOf(cursor.getInt(0))};

                Cursor cursorTwo = fitnessAppDB.query(FOOD_DATA_TABLE, tableColumnsTwo, whereClauseTwo, whereArgsTwo, null, null, null);

                if(cursorTwo.moveToFirst()) {
                    do {
                        String foodName = cursorTwo.getString(0);
                        foodList.add(foodName);
                    } while(cursorTwo.moveToNext());
                }
                cursorTwo.close();

                Diet dietObj = new Diet(date, foodList);
                dietList.add(dietObj);

            } while(cursor.moveToNext());
        }

        cursor.close();
        return dietList;
    }
    // Generates a list of food data to use in the food recyclerview
    public ArrayList<String> getFoodList() {

        ArrayList<String> foodList = new ArrayList<>();
        SQLiteDatabase fitnessAppDB = this.getReadableDatabase();

        String[] tableColumns = {COLUMN_FOOD_NAME};
        String whereClause = COLUMN_DIET_ID + " = ?";
        String[] whereArgs = {String.valueOf(getTodaysDiet())};
        Cursor cursor = fitnessAppDB.query(FOOD_DATA_TABLE, tableColumns, whereClause, whereArgs, null, null, COLUMN_FOOD_NAME);

        if(cursor.moveToFirst()) {
            do {
                String foodName = cursor.getString(0);
                foodList.add(foodName);
            } while(cursor.moveToNext());

        } else {
            cursor.close();
            return  null;
        }
        cursor.close();
        return foodList;
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
