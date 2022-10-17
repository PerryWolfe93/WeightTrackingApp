package com.fitness_app.activities;

//TODO Fix seek bars to show value while scrolling

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fitness_app.BackgroundAnimator;
import com.fitness_app.DatabaseHelper;
import com.fitness_app.object_classes.Diet;
import com.fitness_app.object_classes.Exercise;
import com.fitness_app.object_classes.User;
import com.fitness_app.R;
import com.fitness_app.object_classes.Weight;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static java.time.temporal.ChronoUnit.DAYS;

public class UserProfileActivity extends AppCompatActivity {

    // Variable declaration for database
    private DatabaseHelper fitnessAppDB;

    // Variable declaration for User
    private User user;

    // Variable declarations for widgets
    private RadioGroup genderSelect;
    private RadioButton genderOptions;
    private TextView genderData;
    private SeekBar ageBar;
    private TextView ageData;
    private SeekBar heightBar;
    private TextView heightData;
    private RadioGroup fitnessPlanSelect;
    private RadioButton fitnessPlanOptions;
    private TextView fitnessPlanData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize instances of User Info and DatabaseHelper
        fitnessAppDB = new DatabaseHelper(UserProfileActivity.this);

        // Call class method for background animation
        // Variable declaration for background animator
        BackgroundAnimator backgroundAnimator = new BackgroundAnimator();
        backgroundAnimator.animateBackground(findViewById(R.id.user_profile_layout));

        // Set page title to username
        TextView username = findViewById(R.id.tv_userProfile_title);
        username.setText(User.currentUser);
        // Assign values to widget variables
        genderSelect = findViewById(R.id.rg_userProfile_gender);
        genderData = findViewById(R.id.tv_userProfile_genderData);
        ageBar = findViewById(R.id.sb_userProfile_age);
        ageData = findViewById(R.id.tv_userProfile_ageData);
        heightBar = findViewById(R.id.sb_userProfile_height);
        heightData = findViewById(R.id.tv_userProfile_heightData);
        fitnessPlanSelect = findViewById(R.id.rg_userProfile_fitnessPlan);
        fitnessPlanData = findViewById(R.id.tv_userProfile_fitnessPlanData);
        Button journal = findViewById(R.id.btn_userProfile_journal);
        Button weight = findViewById(R.id.btn_userProfile_weight);
        Button exercise = findViewById(R.id.btn_userProfile_exercise);
        Button diet = findViewById(R.id.btn_userProfile_diet);
        Button edit = findViewById(R.id.btn_userProfile_edit);


        // Initialize user information variables
        String resultSetGender = null;
        int resultSetAge = 0;
        int resultSetHeight = 0;
        String resultSetFitnessPlan = null;

        // Data retrieval for remote user
        if(User.online) {
            try {
                // Check MS SQL Server for user data to display on screen
                String sqlSelect = "SELECT Gender, Age, Height, FitnessPlan FROM User_Information_Table WHERE Username='" + User.currentUser + "'";
                Statement statement = User.connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlSelect);
                // Result set values
                resultSet.next();
                resultSetGender = resultSet.getString(1);
                resultSetAge = resultSet.getInt(2);
                resultSetHeight = resultSet.getInt(3);
                resultSetFitnessPlan = resultSet.getString(4);
            } catch (Exception exception) {
                Log.e("Error", exception.getMessage());
            }
        }
        // Data retrieval for local user
        else {
            user = fitnessAppDB.getCurrentUserInfo();

            // If data exists, display on screen instead of data entry widgets
            resultSetGender = user.getGender();
            resultSetAge = user.getAge();
            resultSetHeight = user.getHeight();
            resultSetFitnessPlan = user.getFitnessPlan();
        }

        // Display data if it exists, Else display selection widgets
        if(resultSetGender != null) {
            swapGenderWidgets();
            genderData.setText(resultSetGender);
        }
        if(resultSetAge != 0) {
            swapAgeWidgets();
            ageData.setText(Integer.toString(resultSetAge));
        }
        if(resultSetHeight != 0) {
            swapHeightWidgets();
            int ft = resultSetHeight / 12;
            int in = resultSetHeight % 12;
            heightData.setText(ft + " ft " + in + " in.");
        }
        if(resultSetFitnessPlan != null) {
            swapFitnessPlanWidgets();
            fitnessPlanData.setText(resultSetFitnessPlan);
        }


        // Set click listeners

        // Edit button reverts user profile to initial state
        edit.setOnClickListener(v -> swapAllWidgets());

        // Gender radio group click listener that changes widgets when button selected
        genderSelect.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId > -1) {
                genderOptions = findViewById(checkedId);
                String selectedGender = genderOptions.getText().toString();
                if(User.online) {
                    try {
                        String sqlSelect = "UPDATE User_Information_Table SET Gender = '" + selectedGender + "' WHERE Username = '" + User.currentUser + "'";
                        Statement statement = User.connection.createStatement();
                        statement.execute(sqlSelect);
                    } catch (Exception exception) {
                        Log.e("Error", exception.getMessage());
                    }
                } else {
                    user.setGender(selectedGender);
                    fitnessAppDB.updateUserInfo(user);
                }
                updateBMR();
                swapGenderWidgets();
                genderData.setText(selectedGender);
            }
        });

        // Fitness plan radio group click listener that changes widgets when button selected
        fitnessPlanSelect.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId > -1) {
                fitnessPlanOptions = findViewById(checkedId);
                String selectedFitnessPlan = fitnessPlanOptions.getText().toString();
                if(User.online) {
                    try {
                        String sqlSelect = "UPDATE User_Information_Table SET FitnessPlan = '" + selectedFitnessPlan + "' WHERE Username = '" + User.currentUser + "'";
                        Statement statement = User.connection.createStatement();
                        statement.execute(sqlSelect);
                    } catch (Exception exception) {
                        Log.e("Error", exception.getMessage());
                    }
                } else {
                    user.setFitnessPlan(selectedFitnessPlan);
                    fitnessAppDB.updateUserInfo(user);
                }
                updateBMR();
                swapFitnessPlanWidgets();
                fitnessPlanData.setText(selectedFitnessPlan);
            }
        });

        ageBar.setMax(87);
        ageBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            final int min = 12;
            int value = 0;
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value = min + progress;
                ageData.setText(Integer.toString(value));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(User.online) {
                    try {
                        String sqlSelect = "UPDATE User_Information_Table SET Age = " + value + " WHERE Username = '" + User.currentUser + "'";
                        Statement statement = User.connection.createStatement();
                        statement.execute(sqlSelect);
                    } catch (Exception exception) {
                        Log.e("Error", exception.getMessage());
                    }
                } else {
                    user.setAge(value);
                    fitnessAppDB.updateUserInfo(user);
                }
                updateBMR();
                swapAgeWidgets();
            }
        });

        heightBar.setMax(36);
        heightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            final int min = 48;
            int value = 0;
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value = min + progress;
                int ft = value / 12;
                int in = value % 12;
                heightData.setText(ft + " ft " + in + " in.");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(User.online) {
                    try {
                        String sqlSelect = "UPDATE User_Information_Table SET Height = " + value + " WHERE Username = '" + User.currentUser + "'";
                        Statement statement = User.connection.createStatement();
                        statement.execute(sqlSelect);
                    } catch (Exception exception) {
                        Log.e("Error", exception.getMessage());
                    }
                } else {
                    user.setHeight(value);
                    fitnessAppDB.updateUserInfo(user);
                }
                updateBMR();
                swapHeightWidgets();
            }
        });

        // Set recommendation data
        setUserRecommendationStats();

        // Buttons to change activities
        journal.setOnClickListener(v -> openJournalActivity());
        weight.setOnClickListener(v -> openWeightActivity());
        exercise.setOnClickListener(v -> openExerciseActivity());
        diet.setOnClickListener(v -> openDietActivity());
    }

    // Methods for changing current activity layout
    public void swapGenderWidgets() {
        genderSelect.setVisibility(View.INVISIBLE);
        genderData.setVisibility(View.VISIBLE);
    }
    public void swapAgeWidgets() {
        ageBar.setVisibility(View.INVISIBLE);
        ageData.setVisibility(View.VISIBLE);
    }
    public void swapHeightWidgets() {
        heightBar.setVisibility(View.INVISIBLE);
        heightData.setVisibility(View.VISIBLE);
    }
    public void swapFitnessPlanWidgets() {
        fitnessPlanSelect.setVisibility(View.INVISIBLE);
        fitnessPlanData.setVisibility(View.VISIBLE);
    }
    // Swaps all widgets when edit button clicked
    public void swapAllWidgets() {
        genderSelect.clearCheck();
        genderSelect.setVisibility(View.VISIBLE);
        genderData.setVisibility(View.INVISIBLE);
        ageBar.setVisibility(View.VISIBLE);
        ageBar.setProgress(0);
        ageData.setVisibility(View.INVISIBLE);
        heightBar.setVisibility(View.VISIBLE);
        heightBar.setProgress(0);
        heightData.setVisibility(View.INVISIBLE);
        fitnessPlanSelect.clearCheck();
        fitnessPlanSelect.setVisibility(View.VISIBLE);
        fitnessPlanData.setVisibility(View.INVISIBLE);
    }


    // Activity change methods
    // Navigate to weight page
    public void openWeightActivity() {
        Intent intent = new Intent(this, WeightActivity.class);
        startActivity(intent);
    }
    // Navigate to journal page
    public void openJournalActivity() {
        Intent intent = new Intent(this, JournalActivity.class);
        startActivity(intent);
    }
    // Navigate to exercise page
    public void openExerciseActivity() {
        Intent intent = new Intent(this, ExerciseActivity.class);
        startActivity(intent);
    }
    // Navigate to diet page
    public void openDietActivity() {
        Intent intent = new Intent(this, DietActivity.class);
        startActivity(intent);
    }


    // Methods for user recommendations
    // Get activity level and weight change trend
    public void setUserRecommendationStats() {

        // Variable declaration and initialization
        String lastEvaluation = "2022-02-27";
        String currentPlan = null;
        double bmrAdjustment = 0;
        int BMR = 0;

        // Get user data needed for recommendation
        // Get data for remote user
        if(User.online) {
            try {
                // Check MS SQL Server for user data to display on screen
                String sqlSelect = "SELECT LastEvaluation, FitnessPlan, BMR, BMRAdjustment FROM User_Information_Table WHERE Username='" + User.currentUser + "'";
                Statement statement = User.connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlSelect);
                resultSet.next();
                lastEvaluation = resultSet.getString(1);
                currentPlan = resultSet.getString(2);
                BMR = (int) resultSet.getDouble(3);
                bmrAdjustment = resultSet.getDouble(4);
            } catch (Exception exception) {
                Log.e("Error", exception.getMessage());
            }
        }
        // Get data for local user
        else {
            lastEvaluation = user.getLastEvaluationDate();
            currentPlan = user.getFitnessPlan();
            bmrAdjustment = user.getBmrAdjustment();
            BMR = (int) user.getBMR();
        }

        // If it hasn't been a week since last evaluation, exit. Else update last evaluation with today's date
        if(LocalDate.parse(lastEvaluation).until(LocalDate.now(), DAYS) < 7) {
            return;
        } else {
            if(User.online) {
                try {
                    String sqlSelect = "UPDATE User_Information_Table SET LastEvaluation = " + LocalDate.now().toString() + " WHERE Username = '" + User.currentUser + "'";
                    Statement statement = User.connection.createStatement();
                    statement.execute(sqlSelect);
                } catch (Exception exception) {
                    Log.e("Error", exception.getMessage());
                }
            } else {
                user.setLastEvaluationDate(LocalDate.now().toString());
            }
        }

        ArrayList<Exercise> lastWeekExercises = new ArrayList<>();

        if(User.online) {
            try {
                String sqlSelect = "SELECT * FROM Exercise_Table WHERE UserID = " + fitnessAppDB.getUserID(User.currentUser) + " ORDER BY EntryID DESC";
                Statement statement = User.connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlSelect);
                int i = 0;
                while(resultSet.next() && i < 7) {
                    Exercise exercise = new Exercise(resultSet.getString(2), resultSet.getString(4), resultSet.getInt(1));
                    lastWeekExercises.add(exercise);
                    i++;
                }
            } catch (Exception exception) {
                Log.e("Error", exception.getMessage());
            }
        } else {
            lastWeekExercises = fitnessAppDB.getLastWeekExercises();
        }

        int timeExercisedLastWeek = 0;
        for(int i = 0; i < lastWeekExercises.size(); i++) {
            if(LocalDate.parse(lastWeekExercises.get(i).getDate()).until(LocalDate.now(), DAYS) <= 7) {
                timeExercisedLastWeek += lastWeekExercises.get(i).getTime();
            } else {
                break;
            }
        }

        if(User.online) {
            try {
                String sqlSelect = "UPDATE User_Information_Table SET LastWeekExerciseTime = " + timeExercisedLastWeek + " WHERE Username = '" + User.currentUser + "'";
                Statement statement = User.connection.createStatement();
                statement.execute(sqlSelect);
            } catch (Exception exception) {
                Log.e("Error", exception.getMessage());
            }
        } else {
            user.setLastWeekExerciseTime(timeExercisedLastWeek);
            fitnessAppDB.updateUserInfo(user);
        }

        if(timeExercisedLastWeek == 0) {
            user.setActivityLevel(1.2);
        } else if(timeExercisedLastWeek > 0 && timeExercisedLastWeek < 80) {
            user.setActivityLevel(1.375);
        } else if(timeExercisedLastWeek >= 80 && timeExercisedLastWeek < 200) {
            user.setActivityLevel(1.55);
        } else if(timeExercisedLastWeek >= 200 && timeExercisedLastWeek < 280) {
            user.setActivityLevel(1.725);
        } else if(timeExercisedLastWeek >= 280) {
            user.setActivityLevel(1.9);
        }


        // weight change trend
        ArrayList<Weight> lastTwoWeeksWeights = new ArrayList<>();

        if(User.online) {
            try {
                String sqlSelect = "SELECT * FROM Weight_Table WHERE UserID = " + fitnessAppDB.getUserID(User.currentUser) + " ORDER BY EntryID DESC";
                Statement statement = User.connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlSelect);
                int i = 0;
                while(resultSet.next() && i <= 14) {
                    Weight weight = new Weight(resultSet.getDouble(2), resultSet.getString(3));
                    lastTwoWeeksWeights.add(weight);
                    i++;
                }
            } catch (Exception exception) {
                Log.e("Error", exception.getMessage());
            }
        } else {
            lastTwoWeeksWeights = fitnessAppDB.getLastTwoWeeksWeights();
        }

        double lastWeekAvgWeight = 0;
        double twoWeeksAgoAvgWeight = 0;
        int count = 0;
        for(int i = 0; i < lastTwoWeeksWeights.size(); i++) {
            if(LocalDate.parse(lastTwoWeeksWeights.get(i).getDate()).until(LocalDate.now(), DAYS) <= 7) {
                lastWeekAvgWeight += lastTwoWeeksWeights.get(i).getWeight();
                count++;
            } else {
                break;
            }
        }
        if(count == 0) {
            return;
        }
        lastWeekAvgWeight /= count;
        count = 0;
        for(int i = 0; i < lastTwoWeeksWeights.size(); i++) {
            if(LocalDate.parse(lastTwoWeeksWeights.get(i).getDate()).until(LocalDate.now(), DAYS) > 7
            && LocalDate.parse(lastTwoWeeksWeights.get(i).getDate()).until(LocalDate.now(), DAYS) <= 14) {
                twoWeeksAgoAvgWeight += lastTwoWeeksWeights.get(i).getWeight();
                count++;
            } else if(LocalDate.parse(lastTwoWeeksWeights.get(i).getDate()).until(LocalDate.now(), DAYS) > 14) {
                break;
            }
        }
        if(count == 0) {
            return;
        }
        twoWeeksAgoAvgWeight /= count;

        double weightChange = lastWeekAvgWeight - twoWeeksAgoAvgWeight;

        if(User.online) {
            try {
                String sqlSelect = "UPDATE User_Information_Table SET WeightChange = " + weightChange + " WHERE Username = '" + User.currentUser + "'";
                Statement statement = User.connection.createStatement();
                statement.execute(sqlSelect);
            } catch (Exception exception) {
                Log.e("Error", exception.getMessage());
            }
        } else {
            user.setWeightChange(weightChange);
            fitnessAppDB.updateUserInfo(user);
        }


        ArrayList<Diet> lastWeekDiets = new ArrayList<>();
        int daysRecordedCount = 0;
        int totalCalories = 0;
        if(User.online) {
            try {
                String sqlSelect = "SELECT * FROM Diet_Table WHERE UserID = " + fitnessAppDB.getUserID(User.currentUser) + " ORDER BY EntryID DESC";
                Statement statement = User.connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlSelect);
                int i = 0;
                while(resultSet.next() && i <= 7) {
                    Diet diet = new Diet(resultSet.getString(2));
                    diet.setEntryID(resultSet.getInt(0));
                    lastWeekDiets.add(diet);
                    i++;
                }
            } catch (Exception exception) {
                Log.e("Error", exception.getMessage());
            }
        } else {
            lastWeekDiets = fitnessAppDB.getLastWeekDiets();
        }

        for(int i = 0; i < lastWeekDiets.size(); i++) {
            if(LocalDate.parse(lastWeekDiets.get(i).getDate()).until(LocalDate.now(), DAYS) <= 7) {
                if(User.online) {
                    try {
                        String sqlSelect = "SELECT Calories FROM Food_Table WHERE EntryID = " + lastWeekDiets.get(i).getEntryID();
                        Statement statement = User.connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(sqlSelect);
                        while(resultSet.next()) {
                            totalCalories += resultSet.getInt(1);
                        }
                    } catch (Exception exception) {
                        Log.e("Error", exception.getMessage());
                    }
                    daysRecordedCount++;
                } else {
                    totalCalories += fitnessAppDB.getCaloriesFromDate(lastWeekDiets.get(i).getEntryID());
                    daysRecordedCount++;
                }

            } else {
                break;
            }
        }

        int lastWeekAverageCalories = totalCalories / daysRecordedCount;
        int calorieDifferenceFromGoal = lastWeekAverageCalories - BMR;

        if(User.online) {
            try {
                String sqlSelect = "UPDATE User_Information_Table SET CalorieDifferenceFromLastWeek = " + calorieDifferenceFromGoal + " WHERE Username = '" + User.currentUser + "'";
                Statement statement = User.connection.createStatement();
                statement.execute(sqlSelect);
            } catch (Exception exception) {
                Log.e("Error", exception.getMessage());
            }
        } else {
            user.setCalorieDifferenceFromGoal(calorieDifferenceFromGoal);
            fitnessAppDB.updateUserInfo(user);
        }



        // Adjusts BMR based on weight change trend

        // Returns if user is not staying within their calorie goal
        if(calorieDifferenceFromGoal != 200) {
            return;
        }

        // If user is gaining weight rapidly
        else if(weightChange > 1.5) {
            if(currentPlan.equals("Gain Muscle")) {
                bmrAdjustment -= 300;
            } else if(currentPlan.equals("Lose Weight")) {
                bmrAdjustment -= 1300;
            } else {
                bmrAdjustment -= 800;
            }
        }
        // If user is gaining weight slowly
        else if(weightChange > 0.5) {
            if(currentPlan.equals("Gain Muscle")) {
                return;
            } else if(currentPlan.equals("Lose Weight")) {
                bmrAdjustment -= 800;
            } else {
                bmrAdjustment -= 300;
            }
        }
        // If user is maintaining their weight
        else if(weightChange > -0.5) {
            if(currentPlan.equals("Gain Muscle")) {
                bmrAdjustment += 300;
            } else if(currentPlan.equals("Lose Weight")) {
                bmrAdjustment -= 300;
            } else {
                return;
            }
        }
        // If user is losing weight slowly
        else if(weightChange > -1.5) {
            if(currentPlan.equals("Gain Muscle")) {
                bmrAdjustment += 800;
            } else if(currentPlan.equals("Lose Weight")) {
                return;
            } else {
                bmrAdjustment += 300;
            }
        }
        // If user is losing weight rapidly
        else {
            if(currentPlan.equals("Gain Muscle")) {
                bmrAdjustment += 1300;
            } else if(currentPlan.equals("Lose Weight")) {
                bmrAdjustment += 300;
            } else {
                bmrAdjustment += 800;
            }
        }


        if(User.online) {
            try {
                String sqlSelect = "UPDATE User_Information_Table SET BMRAdjustment = " + bmrAdjustment + " WHERE Username = '" + User.currentUser + "'";
                Statement statement = User.connection.createStatement();
                statement.execute(sqlSelect);
            } catch (Exception exception) {
                Log.e("Error", exception.getMessage());
            }
        } else {
            user.setBmrAdjustment(bmrAdjustment);
            fitnessAppDB.updateUserInfo(user);
        }

    }
    // Update BMR
    public void updateBMR() {

        // Get user information needed to calculate BMR
        double bmr = 0;
        String gender = null;
        double height = 0.0;
        int age = 0;
        double weight = 0.0;
        String fitnessPlan = null;
        double activityLevel = 0.0;
        // For Remote User
        if(User.online) {
            try {
                // Check MS SQL Server for user data to display on screen
                String sqlSelect = "SELECT Gender, Age, Height, FitnessPlan, ActivityLevel, CurrentWeight FROM User_Information_Table WHERE Username='" + User.currentUser + "'";
                Statement statement = User.connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlSelect);
                // Result set values
                gender = resultSet.getString(1);
                age = resultSet.getInt(2);
                height = resultSet.getInt(3);
                fitnessPlan = resultSet.getString(4);
                activityLevel = resultSet.getDouble(5);
                weight = resultSet.getDouble(6);
            } catch (Exception exception) {
                Log.e("Error", exception.getMessage());
            }
        }
        // For Local User
        else {
            gender = user.getGender();
            height = user.getHeight();
            age = user.getAge();
            weight = user.getCurrentWeight();
            fitnessPlan = user.getFitnessPlan();
            activityLevel = user.getActivityLevel();
        }


        // Return if any of the required fields are empty
        if(gender == null || age <= 1 || height <= 1 || weight <= 1 || fitnessPlan == null) {
            return;
        }

        // Get BMR (Basal Metabolic Rate) using the Mifflin St. Jeor BMR formula
        if(gender.equals("Male")) {
            bmr = (4.536 * weight) + (15.88 * height) - (5 * age) + 5;
        } else if(gender.equals("Female")) {
            bmr = (4.536 * weight) + (15.88 * height) - (5 * age) - 161;
        }

        if (fitnessPlan.equals("Gain Muscle")) {
            bmr += 300;
        } else if(fitnessPlan.equals("Lose Weight")) {
            bmr -= 500;
        }

        bmr *= activityLevel;

        if(User.online) {
            try {
                // Check MS SQL Server for user data to display on screen
                String sqlSelect = "UPDATE User_Information_Table SET BMR = " + bmr + " WHERE Username = '" + User.currentUser + "'";
                Statement statement = User.connection.createStatement();
                statement.execute(sqlSelect);
            } catch (Exception exception) {
                Log.e("Error", exception.getMessage());
            }
        } else {
            user.setBMR(bmr);
            fitnessAppDB.updateUserInfo(user);
        }
    }
}