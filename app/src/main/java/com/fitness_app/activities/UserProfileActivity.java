package com.fitness_app.activities;

//TODO Fix seek bars to show value while scrolling

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fitness_app.BackgroundAnimator;
import com.fitness_app.DatabaseHelper;
import com.fitness_app.object_classes.User;
import com.fitness_app.object_classes.UserInfo;
import com.fitness_app.R;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class UserProfileActivity extends AppCompatActivity {

    // Variable declaration for database
    private DatabaseHelper fitnessAppDB;

    // Variable declaration for User
    private UserInfo userInfo;

    // Variable declarations for widgets
    private RadioGroup genderSelect;
    private RadioButton genderOptions;
    private TextView genderData;
    private SeekBar ageBar;
    private TextView ageData;
    private SeekBar heightBar;
    private TextView heightData;
    private RadioGroup fitnessPlanSelect;
    private RadioButton fitnessPlan;
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
        Button weight = findViewById(R.id.btn_userProfile_weight);
        Button exercise = findViewById(R.id.btn_userProfile_exercise);
        Button diet = findViewById(R.id.btn_userProfile_diet);
        Button edit = findViewById(R.id.btn_userProfile_edit);

        // If user info is not in database, add to database, else retrieve current user information
        if(!fitnessAppDB.checkUserInfo(fitnessAppDB.getUserID(User.currentUser))) {
            userInfo = new UserInfo(null, 0, 0, null, 0, 1.375, LocalDate.now().toString() , 0, 0, 0, 0, -100);
            fitnessAppDB.addUserInfo(userInfo);
        } else {
            userInfo = fitnessAppDB.getCurrentUserInfo();
        }


        // If data exists, display on screen instead of data entry widgets
        if(fitnessAppDB.getStringData("GENDER", "USER_DATA_TABLE") != null) {
            swapGenderWidgets();
        }
        if(fitnessAppDB.getIntData("AGE", "USER_DATA_TABLE") != 0) {
            swapAgeWidgets();
        }
        if(fitnessAppDB.getIntData("HEIGHT", "USER_DATA_TABLE") != 0) {
            swapHeightWidgets();
        }
        if(fitnessAppDB.getStringData("FITNESS_PLAN", "USER_DATA_TABLE") != null) {
            swapFitnessPlanWidgets();
        }

        // Set click listeners for edit button (reverts user profile to initial state)
        edit.setOnClickListener(v -> swapAllWidgets());

        // Gender radio group click listener that changes widgets when button selected
        genderSelect.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId > -1) {
                genderOptions = findViewById(checkedId);
                userInfo.setGender(genderOptions.getText().toString());
                fitnessAppDB.updateUserInfo(userInfo);
                updateBMR();
                swapGenderWidgets();
            }
        });
        // Fitness plan radio group click listener that changes widgets when button selected
        fitnessPlanSelect.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId > -1) {
                fitnessPlan = findViewById(checkedId);
                userInfo.setFitnessPlan(fitnessPlan.getText().toString());
                fitnessAppDB.updateUserInfo(userInfo);
                updateBMR();
                swapFitnessPlanWidgets();
            }
        });

        ageBar.setMax(87);
        ageBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            final int min = 12;
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = min + progress;
                ageData.setText(Integer.toString(value));
                userInfo.setAge(value);
                fitnessAppDB.updateUserInfo(userInfo);
                updateBMR();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                swapAgeWidgets();
            }
        });

        heightBar.setMax(36);
        heightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            final int min = 48;
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = min + progress;
                int ft = value / 12;
                int in = value % 12;
                heightData.setText(ft + " ft " + in + " in.");
                userInfo.setHeight(value);
                fitnessAppDB.updateUserInfo(userInfo);
                updateBMR();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                swapHeightWidgets();
            }
        });

        // Set recommendation data
        setUserRecommendationStats();

        // Buttons to change activities
        weight.setOnClickListener(v -> openWeightActivity());
        exercise.setOnClickListener(v -> openExerciseActivity());
        diet.setOnClickListener(v -> openDietActivity());
    }

    // Change current activity layout methods

    // Swap gender widgets
    @SuppressLint("SetTextI18n")
    public void swapGenderWidgets() {
        genderSelect.setVisibility(View.INVISIBLE);
        genderData.setVisibility(View.VISIBLE);
        genderData.setText(fitnessAppDB.getStringData("GENDER", "USER_DATA_TABLE"));
    }
    // Swap age widgets
    @SuppressLint("SetTextI18n")
    public void swapAgeWidgets() {
        ageBar.setVisibility(View.INVISIBLE);
        ageData.setVisibility(View.VISIBLE);
        ageData.setText(Integer.toString(fitnessAppDB.getIntData("AGE", "USER_DATA_TABLE")));
    }
    // Swap height widgets
    @SuppressLint("SetTextI18n")
    public void swapHeightWidgets() {
        heightBar.setVisibility(View.INVISIBLE);
        heightData.setVisibility(View.VISIBLE);
        int dbHeight = fitnessAppDB.getIntData("HEIGHT", "USER_DATA_TABLE");
        int ft = dbHeight / 12;
        int in = dbHeight % 12;
        heightData.setText(ft + " ft " + in + " in.");
    }
    // Swap fitness plan widgets
    @SuppressLint("SetTextI18n")
    public void swapFitnessPlanWidgets() {
        fitnessPlanSelect.setVisibility(View.INVISIBLE);
        fitnessPlanData.setVisibility(View.VISIBLE);
        fitnessPlanData.setText(fitnessAppDB.getStringData("FITNESS_PLAN", "USER_DATA_TABLE"));
    }
    // Swaps all widgets when edit button clicked
    @SuppressLint("SetTextI18n")
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

        // Returns if the current date is not at least 7 days from the last evaluation
        String lastEvaluation = userInfo.getLastEvaluationDate();
        if(LocalDate.parse(lastEvaluation).until(LocalDate.now(), DAYS) < 7) {
            return;
        } else {
            userInfo.setLastEvaluationDate(LocalDate.now().toString());
        }

        int timeExercisedLastWeek = fitnessAppDB.getLastWeekExerciseTime();
        userInfo.setLastWeekExerciseTime(timeExercisedLastWeek);

        if(timeExercisedLastWeek == 0) {
            userInfo.setActivityLevel(1.2);
        } else if(timeExercisedLastWeek > 0 && timeExercisedLastWeek < 80) {
            userInfo.setActivityLevel(1.375);
        } else if(timeExercisedLastWeek >= 80 && timeExercisedLastWeek < 200) {
            userInfo.setActivityLevel(1.55);
        } else if(timeExercisedLastWeek >= 200 && timeExercisedLastWeek < 280) {
            userInfo.setActivityLevel(1.725);
        } else if(timeExercisedLastWeek >= 280) {
            userInfo.setActivityLevel(1.9);
        }

        // weight change trend

        double weightChange = fitnessAppDB.getWeeklyWeightAverageDifference();
        userInfo.setWeightChange(weightChange);

        String currentPlan = userInfo.getFitnessPlan();
        double bmrAdjustment = userInfo.getBmrAdjustment();
        double calorieDifferenceFromGoal = fitnessAppDB.getLastWeekAverageCalories() - userInfo.getBMR();
        userInfo.setCalorieDifferenceFromGoal(calorieDifferenceFromGoal);

        // Adjusts BMR based on weight change trend

        // Returns if user does not have at least one weight entry in each of the past two weeks
        if(weightChange <= -99) {
            return;
        }
        // Returns if user is not staying within their calorie goal
        if(calorieDifferenceFromGoal > 200 || calorieDifferenceFromGoal < 200) {
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

        userInfo.setBmrAdjustment(bmrAdjustment);

        fitnessAppDB.updateUserInfo(userInfo);
    }

    // Update BMR
    public void updateBMR() {

        // Get user information needed to calculate BMR
        double bmr = 0;
        String gender = userInfo.getGender();
        double height = userInfo.getHeight();
        int age = userInfo.getAge();
        double weight = userInfo.getCurrentWeight();
        String userGoal = userInfo.getFitnessPlan();
        double activityLevel = userInfo.getActivityLevel();

        // Return if any of the required fields are empty
        if(gender == null || age <= 1 || height <= 1 || weight <= 1 || userGoal == null) {
            return;
        }

        // Get BMR (Basal Metabolic Rate) using the Mifflin St. Jeor BMR formula
        if(gender.equals("Male")) {
            bmr = (4.536 * weight) + (15.88 * height) - (5 * age) + 5;
        } else if(gender.equals("Female")) {
            bmr = (4.536 * weight) + (15.88 * height) - (5 * age) - 161;
        }

        if (userGoal.equals("Gain Muscle")) {
            bmr += 300;
        } else if(userGoal.equals("Lose Weight")) {
            bmr -= 500;
        }

        bmr *= activityLevel;

        userInfo.setBMR(bmr);
    }
}