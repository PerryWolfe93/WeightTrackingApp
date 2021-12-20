package com.zybooks.perrywolfe_weighttrackingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class WeightActivity extends AppCompatActivity {

    // Variable declarations
    private EditText currentWeight;
    private RecyclerView recyclerView;
    private DatabaseHelper fitnessAppDB;
    private UserInfo userInfo;
    private TextView userRecommendation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        // Initialize instance of DatabaseHelper
        fitnessAppDB = new DatabaseHelper(WeightActivity.this);

        // Assign values to widget variables
        recyclerView = findViewById(R.id.rv_weight_list);
        currentWeight = findViewById(R.id.et_weight_editCurrentWeight);
        Button enterWeight = findViewById(R.id.btn_weight_changeWeight);
        Button back = findViewById(R.id.btn_weight_back);
        userRecommendation = findViewById(R.id.tv_weight_recommendation);

        // Retrieve current user information
        userInfo = fitnessAppDB.getCurrentUserInfo();

        // Set click listeners for buttons
        enterWeight.setOnClickListener(v -> {
            String date = LocalDate.now().toString();
            boolean isDouble = true;
            try {
                Double.parseDouble(currentWeight.getText().toString());
            } catch (NumberFormatException | NullPointerException nfe) {
                isDouble = false;
            }

            if(isDouble) {
                double weight = Double.parseDouble(currentWeight.getText().toString());

                if(fitnessAppDB.checkForWeightData()) {
                    Weight weightEntry = new Weight(weight, date);
                    fitnessAppDB.updateWeight(weightEntry);
                    setAdapter();
                } else {
                    Weight weightEntry = new Weight(weight, date);
                    fitnessAppDB.addOneWeight(weightEntry);
                    setAdapter();
                }
                userInfo.setCurrentWeight(weight);
                fitnessAppDB.updateUserInfo(userInfo);

            } else {
                Toast.makeText(WeightActivity.this, "Please Enter Your Weight", Toast.LENGTH_SHORT).show();
            }
        });

        back.setOnClickListener(v -> openUserProfileActivity());

        setAdapter();
        getRecommendation();
    }

    private void getRecommendation() {

        double weightChange = fitnessAppDB.getWeeklyWeightAverageDifference();

        // Returns if user does not have at least one data entry per each of the past two weeks
        if(weightChange <= -99) {
            userRecommendation.setText("Keep recording your weights everyday for the most accurate feedback.");
            return;
        }

        // If user is gaining weight rapidly
        if(weightChange > 2.5) {
            userRecommendation.setText("You are gaining weight rapidly.");
        }
        // If user is gaining weight slowly
        else if(weightChange > 1.0) {
            userRecommendation.setText("You are gradually gaining weight.");
        }
        // If user is maintaining their weight
        else if(weightChange > -1.0) {
            userRecommendation.setText("You are maintaining your weight.");
        }
        // If user is losing weight slowly
        else if(weightChange > -2.5) {
            userRecommendation.setText("You are gradually losing weight.");
        }
        // If user is losing weight rapidly
        else {
            userRecommendation.setText("You are losing weight rapidly." + weightChange);
        }
    }

    private void setAdapter() {
        WeightRecyclerAdapter adapter = new WeightRecyclerAdapter(fitnessAppDB.getWeightList());
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