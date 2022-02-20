package com.fitness_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fitness_app.BackgroundAnimator;
import com.fitness_app.DatabaseHelper;
import com.fitness_app.recycler_adapters.ExerciseRecyclerAdapter;
import com.fitness_app.object_classes.UserInfo;
import com.fitness_app.object_classes.Exercise;
import com.fitness_app.R;

import java.time.LocalDate;

public class ExerciseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Variable Declarations
    private RecyclerView recyclerView;
    private TextView userRecommendation;
    private EditText exerciseTime;
    private DatabaseHelper fitnessAppDB;
    private String exerciseChoice;
    private UserInfo userInfo;
    BackgroundAnimator backgroundAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        // Initialize instance of DatabaseHelper
        fitnessAppDB = new DatabaseHelper(ExerciseActivity.this);

        // Call class method for background animation
        backgroundAnimator = new BackgroundAnimator();
        backgroundAnimator.animateBackground(findViewById(R.id.exercise_layout));

        userInfo = fitnessAppDB.getCurrentUserInfo();

        // Assign values to widget variables
        recyclerView = findViewById(R.id.rv_exercise_list);
        exerciseTime = findViewById(R.id.et_exercise_time);
        Button enter = findViewById(R.id.btn_exercise_enter);
        Button back = findViewById(R.id.btn_exercise_back);
        Spinner chooseExercise = findViewById(R.id.spn_exercise_exerciseType);
        userRecommendation = findViewById(R.id.tv_exercise_recommendation);

        // Spinner Variables
        ArrayAdapter<CharSequence> exerciseChoiceAdapter = ArrayAdapter.createFromResource(this, R.array.spn_exercises, android.R.layout.simple_spinner_item);
        //TODO use this method to fix spinner dropdown background
        // exerciseChoiceAdapter.setDropDownViewTheme();
        exerciseChoiceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseExercise.setAdapter(exerciseChoiceAdapter);


        // Set click listeners
        chooseExercise.setOnItemSelectedListener(this);

        enter.setOnClickListener(v -> {
            String date = LocalDate.now().toString();
            int time = Integer.parseInt(exerciseTime.getText().toString());

            if(exerciseTime.getText().toString().equals("")) {
                Toast.makeText(ExerciseActivity.this, "Please enter the amount of time you exercised", Toast.LENGTH_SHORT).show();
            } else if(exerciseChoice.equals("")) {
                Toast.makeText(ExerciseActivity.this, "Please select an exercise.", Toast.LENGTH_SHORT).show();
            } else if(fitnessAppDB.checkForExerciseData()) {
                Toast.makeText(ExerciseActivity.this, "An exercise has already been entered for today", Toast.LENGTH_SHORT).show();
            } else {
                Exercise exercise = new Exercise(date, exerciseChoice, time);
                fitnessAppDB.addOneExercise(exercise);
                setAdapter();
            }
        });
        back.setOnClickListener(v -> openUserProfileActivity());

        // Generates list of previous exercises from database
        setAdapter();
        // Sets user recommendation text view
        getRecommendation();
    }

    private void setAdapter() {
        ExerciseRecyclerAdapter adapter = new ExerciseRecyclerAdapter(fitnessAppDB.getExerciseList());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
    public void openUserProfileActivity() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        exerciseChoice = parent.getItemAtPosition(position).toString();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @SuppressLint("SetTextI18n")
    private void getRecommendation() {

        int timeExercisedLastWeek = userInfo.getLastWeekExerciseTime();

        if(timeExercisedLastWeek <= 0) {
            userRecommendation.setText("You did not record any exercises last week." +
                    "\nYou should aim to get at least 150 minutes of exercise per week.");
        } else if(timeExercisedLastWeek < 30) {
            userRecommendation.setText("Good job getting some exercise last week." +
                    "\nYou got " + timeExercisedLastWeek + " minutes of exercise." +
                    "\nNext week try to exercise even more.");
        } else if(timeExercisedLastWeek <= 60) {
            userRecommendation.setText("Great effort setting aside time for fitness!" +
                    "\nYou exercised for " + timeExercisedLastWeek + " minutes.");
        } else if(timeExercisedLastWeek <= 120) {
            userRecommendation.setText("You nearly reached the weekly recommendation last week." +
                    "\nYou got " + timeExercisedLastWeek + " minutes of exercise.");
        }else if(timeExercisedLastWeek <= 150) {
            userRecommendation.setText("Amazing! You reached the weekly recommendation." +
                    "\nYou exercised for " + timeExercisedLastWeek + " minutes." +
                    "\nKeep up the good work!");
        } else if(timeExercisedLastWeek <= 210) {
            userRecommendation.setText("Your commitment to fitness is inspiring." +
                    "\nYou exercised for " + timeExercisedLastWeek + " minutes this week");
        } else {
            userRecommendation.setText("Congratulations! You have achieved a perfect week!" +
                    "\nYou exercised for " + timeExercisedLastWeek + " minutes." +
                    "\nKeep up the good work and don't forget to let yourself rest");
        }
    }
}