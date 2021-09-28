package com.zybooks.perrywolfe_weighttrackingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.time.LocalDate;

public class WeightActivity extends AppCompatActivity {

    // Variable declarations
    private EditText currentWeight;
    private Button enterWeight, back;
    private RecyclerView recyclerView;
    private DatabaseHelper weightTrackerDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        // Initialize instance of DatabaseHelper
        weightTrackerDB = new DatabaseHelper(WeightActivity.this);

        // Assign values to widget variables
        recyclerView = findViewById(R.id.rv_weight_list);
        currentWeight = findViewById(R.id.et_weight_editCurrentWeight);
        enterWeight = findViewById(R.id.btn_weight_changeWeight);
        back = findViewById(R.id.btn_weight_back);

        // Set click listeners for buttons
        enterWeight.setOnClickListener(v -> {
            String date = LocalDate.now().toString();
            float weight = Float.valueOf(currentWeight.getText().toString());

            if(currentWeight.equals("")) {
                Toast.makeText(WeightActivity.this, "Please Enter Your Weight", Toast.LENGTH_SHORT).show();
            } else if(weightTrackerDB.checkForWeight()) {
                Weight weightEntry = new Weight(weight, date);
                weightTrackerDB.updateWeight(weightEntry);
            } else {
                Weight weightEntry = new Weight(weight, date);
                weightTrackerDB.addOneWeight(weightEntry);
            }
        });

        back.setOnClickListener(v -> openUserProfileActivity());

        setAdapter();
    }

    private void setAdapter() {
        WeightRecyclerAdapter adapter = new WeightRecyclerAdapter(weightTrackerDB.getWeightList());
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