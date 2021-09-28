package com.zybooks.perrywolfe_weighttrackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class UserProfileActivity extends AppCompatActivity {

    // Variable declarations
    private TextView age, height, username;
    private EditText goalWeight;
    private Button enterGoalWeight, weight, exercise, diet;
    private SeekBar ageBar, heightBar;
    private RadioGroup genderSelect, fitnessPlanSelect;
    private RadioButton gender, fitnessPlan;
    DatabaseHelper weightTrackerDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize instances of User Info and DatabaseHelper
        UserInfo userInfo = new UserInfo();
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

        // Set page title
        username.setText(User.currentUser);

        // Check for database data
        if(!weightTrackerDB.checkUserInfo(weightTrackerDB.getUserID(User.currentUser))) {
            weightTrackerDB.addUserInfo(userInfo);
        }

        // Set click listeners for buttons
        enterGoalWeight.setOnClickListener(v -> {
            userInfo.setGoalWeight(Float.valueOf(goalWeight.getText().toString()));
            weightTrackerDB.updateUserInfo(userInfo);
        });

        genderSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                gender = findViewById(checkedId);
                userInfo.setGender(gender.getText().toString());
                weightTrackerDB.updateUserInfo(userInfo);
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
                height.setText(ft + " ft " + in + " in.");
                userInfo.setHeight(value);
                weightTrackerDB.updateUserInfo(userInfo);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Buttons to change activities
        weight.setOnClickListener(v -> openWeightActivity());

        exercise.setOnClickListener(v -> openExerciseActivity());

        diet.setOnClickListener(v -> openDietActivity());
    }

    // Activity change methods
    public void openWeightActivity() {
        Intent intent = new Intent(this, WeightActivity.class);
        startActivity(intent);
    }
    public void openExerciseActivity() {
        Intent intent = new Intent(this, ExerciseActivity.class);
        startActivity(intent);
    }
    public void openDietActivity() {
        Intent intent = new Intent(this, DietActivity.class);
        startActivity(intent);
    }
}