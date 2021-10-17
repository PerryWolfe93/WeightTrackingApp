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
    private DatabaseHelper weightTrackerDB;
    private RecyclerView recyclerView;
    private EditText enterCalories, enterProtein, enterCarb, enterFat;
    private Button add, subtract, back;
    private Diet diet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        // Initialize instance of DatabaseHelper
        weightTrackerDB = new DatabaseHelper(DietActivity.this);

        // Initialize widget variables
        recyclerView = findViewById(R.id.rv_diet_list);
        enterCalories = findViewById(R.id.et_diet_calories);
        enterProtein = findViewById(R.id.et_diet_protein);
        enterCarb = findViewById(R.id.et_diet_carb);
        enterFat = findViewById(R.id.et_diet_fat);
        add = findViewById(R.id.btn_diet_add);
        subtract = findViewById(R.id.btn_diet_subtract);
        back = findViewById(R.id.btn_diet_back);

        // If today's date is not in the database, create new entry, else get data from database
        if(!weightTrackerDB.checkForCalorieData()) {
            diet = new Diet(LocalDate.now().toString(), 0, 0, 0, 0);
            weightTrackerDB.addOneDiet(diet);
        } else {
            diet = weightTrackerDB.getCurrentDiet();
        }


        // Set click listeners

        // Button for adding to calories/protein/carb/fat
        add.setOnClickListener(v -> {
            diet.setCalories(diet.getCalories() + Integer.valueOf(enterCalories.getText().toString()));
            diet.setProtein(diet.getProtein() + Integer.valueOf(enterProtein.getText().toString()));
            diet.setCarb(diet.getCarb() + Integer.valueOf(enterCarb.getText().toString()));
            diet.setFat(diet.getFat() + Integer.valueOf(enterFat.getText().toString()));
            weightTrackerDB.updateDiet(diet);
            setAdapter();
        });
        // Button for subtracting from calories/protein/carb/fat
        subtract.setOnClickListener(v -> {
            diet.setCalories(diet.getCalories() - Integer.valueOf(enterCalories.getText().toString()));
            diet.setProtein(diet.getProtein() - Integer.valueOf(enterProtein.getText().toString()));
            diet.setCarb(diet.getCarb() - Integer.valueOf(enterCarb.getText().toString()));
            diet.setFat(diet.getFat() - Integer.valueOf(enterFat.getText().toString()));
            weightTrackerDB.updateDiet(diet);
            setAdapter();
        });
        // Button for moving back to the user profile page
        back.setOnClickListener(v -> openUserProfileActivity());

        // Sets an adapter used to create the diet list
        setAdapter();
    }

    // Method for populating the recyclerview with data from the diet data table
    private void setAdapter() {
        DietRecyclerAdapter adapter = new DietRecyclerAdapter(weightTrackerDB.getDietList());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
    // Method for opening the user profile
    public void openUserProfileActivity() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }
}