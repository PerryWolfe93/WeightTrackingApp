package com.zybooks.perrywolfe_weighttrackingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.time.LocalDate;

public class ExerciseActivity extends AppCompatActivity {

    // Variable Declarations
    private RecyclerView recyclerView;
    private RadioGroup exerciseSelect;
    private RadioButton exercise;
    private EditText exerciseTime;
    private Button enter, back;
    private DatabaseHelper weightTrackerDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        // Initialize instance of DatabaseHelper
        weightTrackerDB = new DatabaseHelper(ExerciseActivity.this);

        // Assign values to widget variables
        recyclerView = findViewById(R.id.rv_exercise_list);
        //TODO Add exercise selection radio group

        // exerciseSelect = findViewById(R.id.rg_exercise_exercise_type);
        exerciseTime = findViewById(R.id.et_exercise_time);
        enter = findViewById(R.id.btn_exercise_enter);
        back = findViewById(R.id.btn_exercise_back);

        // Set click listeners
        exerciseSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                exercise = findViewById(checkedId);
            }
        });

        enter.setOnClickListener(v -> {
            String date = LocalDate.now().toString();
            String exerciseType = exercise.getText().toString();
            int time = Integer.valueOf(exerciseTime.getText().toString());

            if(exerciseTime.equals("")) {
                Toast.makeText(ExerciseActivity.this, "Please enter the amount of time you exercised", Toast.LENGTH_SHORT).show();
            } else if(exerciseType.equals("")) {
                Toast.makeText(ExerciseActivity.this, "Please select an exercise type", Toast.LENGTH_SHORT).show();
            } else if(weightTrackerDB.checkForExercise()) {
                Exercise exercise = new Exercise(date, exerciseType, time);
                weightTrackerDB.updateExercise(exercise);
            } else {
                Exercise exercise = new Exercise(date, exerciseType, time);
                weightTrackerDB.addOneExercise(exercise);
            }
        });

        back.setOnClickListener(v -> openUserProfileActivity());

        setAdapter();
    }

    private void setAdapter() {
        ExerciseRecyclerAdapter adapter = new ExerciseRecyclerAdapter(weightTrackerDB.getExerciseList());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void openUserProfileActivity() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }
}