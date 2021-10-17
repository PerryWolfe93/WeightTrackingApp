package com.zybooks.perrywolfe_weighttrackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class UserProfileActivity extends AppCompatActivity {

    // Variable declarations
    private TextView age, height, username, genderData, ageData, heightData, goalWeightData, fitnessPlanData, fitnessPlanPrompt;
    private EditText goalWeight;
    private Button enterGoalWeight, weight, exercise, diet, edit;
    private SeekBar ageBar, heightBar;
    private RadioGroup genderSelect, fitnessPlanSelect;
    private RadioButton gender, fitnessPlan;
    DatabaseHelper weightTrackerDB;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize instances of User Info and DatabaseHelper
        weightTrackerDB = new DatabaseHelper(UserProfileActivity.this);

        // Assign values to widget variables
        age = findViewById(R.id.tv_userProfile_age);
        height = findViewById(R.id.tv_userProfile_height);
        username = findViewById(R.id.tv_userProfile_title);
        goalWeight = findViewById(R.id.et_userProfile_goalWeight);
        enterGoalWeight = findViewById(R.id.btn_userProfile_enterGoalWeight);
        weight = findViewById(R.id.btn_userProfile_weight);
        exercise = findViewById(R.id.btn_userProfile_exercise);
        diet = findViewById(R.id.btn_userProfile_diet);
        genderSelect = findViewById(R.id.rg_userProfile_gender);
        fitnessPlanSelect = findViewById(R.id.rg_userProfile_fitnessPlan);
        ageBar = findViewById(R.id.sb_userProfile_age);
        heightBar = findViewById(R.id.sb_userProfile_height);
        edit = findViewById(R.id.btn_userProfile_edit);
        genderData = findViewById(R.id.tv_userProfile_gender);
        ageData = findViewById(R.id.tv_userProfile_ageData);
        heightData = findViewById(R.id.tv_userProfile_heightData);
        goalWeightData = findViewById(R.id.tv_userProfile_goalWeightData);
        fitnessPlanData = findViewById(R.id.tv_userProfile_fitnessPlanData);
        fitnessPlanPrompt = findViewById(R.id.tv_userProfile_exerciseType);


        // Set page title
        username.setText(User.currentUser);


        // If user info is not in database, add to database
        if(!weightTrackerDB.checkUserInfo(weightTrackerDB.getUserID(User.currentUser))) {
            userInfo = new UserInfo(null, 0, 0, 0.0f, null);
            weightTrackerDB.addUserInfo(userInfo);
        } else {
            userInfo = weightTrackerDB.getCurrentUserInfo();
        }


        // If data exists, display on screen instead of data entry widgets
        if(weightTrackerDB.getStringData("GENDER", "USER_DATA_TABLE") != null) {
            swapGenderWidgets();
        }
        if(weightTrackerDB.getIntData("AGE", "USER_DATA_TABLE") != 0) {
            swapAgeWidgets();
        }
        if(weightTrackerDB.getIntData("HEIGHT", "USER_DATA_TABLE") != 0) {
            swapHeightWidgets();
        }
        if(weightTrackerDB.getFloatData("GOAL_WEIGHT", "USER_DATA_TABLE") != 0.0f) {
            swapGoalWeightWidgets();
        }
        if(weightTrackerDB.getStringData("FITNESS_PLAN", "USER_DATA_TABLE") != null) {
            swapFitnessPlanWidgets();
        }

        // Set click listeners for buttons
        enterGoalWeight.setOnClickListener(v -> {

            userInfo.setGoalWeight(Float.valueOf(goalWeight.getText().toString()));
            weightTrackerDB.updateUserInfo(userInfo);
            swapGoalWeightWidgets();
        });

        edit.setOnClickListener(v -> {
            swapAllWidgets();
        });
        // Gender radio group click listener that changes widgets when button selected
        genderSelect.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId > -1) {
                gender = findViewById(checkedId);
                userInfo.setGender(gender.getText().toString());
                weightTrackerDB.updateUserInfo(userInfo);
                swapGenderWidgets();
            }
        });
        // Fitness plan radio group click listener that changes widgets when button selected
        fitnessPlanSelect.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId > -1) {
                fitnessPlan = findViewById(checkedId);
                userInfo.setFitnessPlan(fitnessPlan.getText().toString());
                weightTrackerDB.updateUserInfo(userInfo);
                swapFitnessPlanWidgets();
            }
        });

        ageBar.setMax(87);
        ageBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            final int min = 12;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = min + progress;
                age.setText("Age: " + value);
                userInfo.setAge(value);
                weightTrackerDB.updateUserInfo(userInfo);
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
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = min + progress;
                int ft = value / 12;
                int in = value % 12;
                height.setText("Height: " + ft + " ft " + in + " in.");
                userInfo.setHeight(value);
                weightTrackerDB.updateUserInfo(userInfo);

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                swapHeightWidgets();
            }
        });

        // Buttons to change activities
        weight.setOnClickListener(v -> openWeightActivity());
        exercise.setOnClickListener(v -> openExerciseActivity());
        diet.setOnClickListener(v -> openDietActivity());
    }


    // Change current activity layout methods

    // Swap gender widgets
    public void swapGenderWidgets() {
        genderSelect.setVisibility(View.GONE);
        genderData.setVisibility(View.VISIBLE);
        genderData.setText("Gender: " + weightTrackerDB.getStringData("GENDER", "USER_DATA_TABLE"));
    }
    // Swap age widgets
    public void swapAgeWidgets() {
        ageBar.setVisibility(View.GONE);
        age.setVisibility(View.GONE);
        ageData.setVisibility(View.VISIBLE);
        ageData.setText("Age: " + weightTrackerDB.getIntData("AGE", "USER_DATA_TABLE"));
    }
    // Swap height widgets
    public void swapHeightWidgets() {
        heightBar.setVisibility(View.GONE);
        height.setVisibility(View.GONE);
        heightData.setVisibility(View.VISIBLE);
        int dbHeight = weightTrackerDB.getIntData("HEIGHT", "USER_DATA_TABLE");
        int ft = dbHeight / 12;
        int in = dbHeight % 12;
        heightData.setText("Height: " + ft + " ft " + in + " in.");
    }
    // Swap goal weight widgets
    public void swapGoalWeightWidgets() {
        enterGoalWeight.setVisibility(View.GONE);
        goalWeight.setVisibility(View.GONE);
        goalWeightData.setVisibility(View.VISIBLE);
        goalWeightData.setText("Goal Weight: " + weightTrackerDB.getFloatData("GOAL_WEIGHT", "USER_DATA_TABLE"));
    }
    // Swap fitness plan widgets
    public void swapFitnessPlanWidgets() {
        fitnessPlanSelect.setVisibility(View.GONE);
        fitnessPlanPrompt.setVisibility(View.GONE);
        fitnessPlanData.setVisibility(View.VISIBLE);
        fitnessPlanData.setText("Fitness Plan: " + weightTrackerDB.getStringData("FITNESS_PLAN", "USER_DATA_TABLE"));
    }
    // Swaps all widgets when edit button clicked
    public void swapAllWidgets() {
        genderSelect.clearCheck();
        fitnessPlanSelect.clearCheck();
        genderData.setVisibility(View.GONE);
        genderSelect.setVisibility(View.VISIBLE);
        ageBar.setVisibility(View.VISIBLE);
        age.setVisibility(View.VISIBLE);
        ageData.setVisibility(View.GONE);
        heightBar.setVisibility(View.VISIBLE);
        heightData.setVisibility(View.GONE);
        height.setVisibility(View.VISIBLE);
        enterGoalWeight.setVisibility(View.VISIBLE);
        goalWeight.setVisibility(View.VISIBLE);
        goalWeightData.setVisibility(View.GONE);
        fitnessPlanSelect.setVisibility(View.VISIBLE);
        fitnessPlanPrompt.setVisibility(View.VISIBLE);
        fitnessPlanData.setVisibility(View.GONE);
        goalWeight.setText("");
        heightBar.setProgress(0);
        height.setText("Height: ");
        ageBar.setProgress(0);
        age.setText("Age: ");
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
}