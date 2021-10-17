package com.zybooks.perrywolfe_weighttrackingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.time.LocalDate;

public class ExerciseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Variable Declarations
    private RecyclerView recyclerView;
    private EditText exerciseTime;
    private Button enter, back;
    private DatabaseHelper weightTrackerDB;
    private Spinner chooseExercise;
    private String exerciseChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        // Initialize instance of DatabaseHelper
        weightTrackerDB = new DatabaseHelper(ExerciseActivity.this);

        // Assign values to widget variables
        recyclerView = findViewById(R.id.rv_exercise_list);
        exerciseTime = findViewById(R.id.et_exercise_time);
        enter = findViewById(R.id.btn_exercise_enter);
        back = findViewById(R.id.btn_exercise_back);
        chooseExercise = findViewById(R.id.spn_exercise_exerciseType);

        // Spinner Variables
        ArrayAdapter<CharSequence> exerciseChoiceAdapter = ArrayAdapter.createFromResource(this, R.array.spn_exercises, android.R.layout.simple_spinner_item);
        exerciseChoiceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseExercise.setAdapter(exerciseChoiceAdapter);


        // Set click listeners
        chooseExercise.setOnItemSelectedListener(this);

        enter.setOnClickListener(v -> {
            String date = LocalDate.now().toString();
            int time = Integer.valueOf(exerciseTime.getText().toString());

            if(exerciseTime.equals("")) {
                Toast.makeText(ExerciseActivity.this, "Please enter the amount of time you exercised", Toast.LENGTH_SHORT).show();
            } else if(exerciseChoice.equals("")) {
                Toast.makeText(ExerciseActivity.this, "Please select an exercise.", Toast.LENGTH_SHORT).show();
            } else if(weightTrackerDB.checkForExerciseData()) {
                Toast.makeText(ExerciseActivity.this, "An exercise has already been entered for today", Toast.LENGTH_SHORT).show();
            } else {
                Exercise exercise = new Exercise(date, exerciseChoice, time);
                weightTrackerDB.addOneExercise(exercise);
                setAdapter();
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
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        exerciseChoice = parent.getItemAtPosition(position).toString();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}