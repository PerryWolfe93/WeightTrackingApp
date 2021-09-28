package com.zybooks.perrywolfe_weighttrackingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.time.LocalDate;

public class DietActivity extends AppCompatActivity {

    // Variable Declarations
    DatabaseHelper weightTrackerDB;
    RecyclerView recyclerView;
    EditText enterCalories;
    Button add, subtract, back;
    Diet diet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        // Initialize instance of DatabaseHelper
        weightTrackerDB = new DatabaseHelper(DietActivity.this);

        // Assign values to widget variables
        recyclerView = findViewById(R.id.rv_diet_list);
        enterCalories = findViewById(R.id.et_diet_calories);
        add = findViewById(R.id.btn_diet_add);
        subtract = findViewById(R.id.btn_diet_subtract);
        back = findViewById(R.id.btn_diet_back);

        // Check for current day data and instantiate
        if(!weightTrackerDB.checkForCalories()) {
            diet = new Diet(LocalDate.now().toString(), 0);
            weightTrackerDB.addOneDiet(diet);
        } else {
            diet = weightTrackerDB.getCurrentDiet();
        }

        // Set click listeners
        add.setOnClickListener(v -> {
            int calories = diet.getCalories();
            int addedCalories = Integer.valueOf(enterCalories.getText().toString());
            diet.setCalories(calories + addedCalories);
            weightTrackerDB.updateDiet(diet);
        });

        subtract.setOnClickListener(v -> {
            int calories = diet.getCalories();
            int subtractedCalories = Integer.valueOf(enterCalories.getText().toString());
            diet.setCalories(calories - subtractedCalories);
            weightTrackerDB.updateDiet(diet);
        });

        back.setOnClickListener(v -> openUserProfileActivity());

        setAdapter();
    }

    private void setAdapter() {
        DietRecyclerAdapter adapter = new DietRecyclerAdapter(weightTrackerDB.getDietList());
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