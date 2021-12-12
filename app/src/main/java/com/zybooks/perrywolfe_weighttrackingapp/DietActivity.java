package com.zybooks.perrywolfe_weighttrackingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.time.LocalDate;

public class DietActivity extends AppCompatActivity {

    // Variable Declarations
    private DatabaseHelper fitnessAppDB;
    private RecyclerView recyclerView;
    private EditText enterCalories, enterProtein, enterCarb, enterFat;
    private TextView userRecommendation;
    private Diet diet;
    private UserInfo userInfo;

    public DietActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        // Initialize instance of DatabaseHelper
        fitnessAppDB = new DatabaseHelper(DietActivity.this);

        // Initialize widget variables
        recyclerView = findViewById(R.id.rv_diet_list);
        enterCalories = findViewById(R.id.et_diet_calories);
        enterProtein = findViewById(R.id.et_diet_protein);
        enterCarb = findViewById(R.id.et_diet_carb);
        enterFat = findViewById(R.id.et_diet_fat);
        Button add = findViewById(R.id.btn_diet_add);
        Button subtract = findViewById(R.id.btn_diet_subtract);
        Button back = findViewById(R.id.btn_diet_back);
        userRecommendation = findViewById(R.id.tv_diet_userRecommendation);

        userInfo = fitnessAppDB.getCurrentUserInfo();

        // If today's date is not in the database, create new entry, else get data from database
        if(!fitnessAppDB.checkForCalorieData()) {
            diet = new Diet(LocalDate.now().toString(), 0, 0, 0, 0);
            fitnessAppDB.addOneDiet(diet);
        } else {
            diet = fitnessAppDB.getCurrentDiet();
        }

        // Set click listeners

        // Button for adding to calories/protein/carb/fat
        add.setOnClickListener(v -> {
            diet.setCalories(diet.getCalories() + Integer.parseInt(enterCalories.getText().toString()));
            diet.setProtein(diet.getProtein() + Integer.parseInt(enterProtein.getText().toString()));
            diet.setCarb(diet.getCarb() + Integer.parseInt(enterCarb.getText().toString()));
            diet.setFat(diet.getFat() + Integer.parseInt(enterFat.getText().toString()));
            fitnessAppDB.updateDiet(diet);
            setAdapter();
        });
        // Button for subtracting from calories/protein/carb/fat
        subtract.setOnClickListener(v -> {
            diet.setCalories(diet.getCalories() - Integer.parseInt(enterCalories.getText().toString()));
            diet.setProtein(diet.getProtein() - Integer.parseInt(enterProtein.getText().toString()));
            diet.setCarb(diet.getCarb() - Integer.parseInt(enterCarb.getText().toString()));
            diet.setFat(diet.getFat() - Integer.parseInt(enterFat.getText().toString()));
            fitnessAppDB.updateDiet(diet);
            setAdapter();
        });
        // Button for moving back to the user profile page
        back.setOnClickListener(v -> openUserProfileActivity());

        // Sets an adapter used to create the diet list
        setAdapter();
        // Sets user recommendation text view
        getRecommendation();
    }

    // Method for populating the recyclerview with data from the diet data table
    private void setAdapter() {
        DietRecyclerAdapter adapter = new DietRecyclerAdapter(fitnessAppDB.getDietList());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
    // Method for calculating getting diet recommendations
    @SuppressLint("SetTextI18n")
    private void getRecommendation() {

        String currentPlan = userInfo.getFitnessPlan();
        double finalBmr = userInfo.getBMR() + userInfo.getBmrAdjustment();
        double bmrAdjustment = userInfo.getBmrAdjustment();
        double calorieDifferenceFromGoal = fitnessAppDB.getLastWeekAverageCalories() - userInfo.getBMR();
        double weightChange = fitnessAppDB.getWeeklyWeightAverageDifference();


        if(currentPlan.equals("")) {
            userRecommendation.setText("For diet recommendations, please select a fitness plan " +
                    "from your user profile page.");
        } else if(finalBmr < -99) {
            userRecommendation.setText("For diet recommendations, please fill in the weight, " +
                    "height, age, and gender fields on your user profile page.");
        } else if(calorieDifferenceFromGoal > 200 || calorieDifferenceFromGoal < -200) {
            userRecommendation.setText("For an accurate recommendation, you must meet your calorie " +
                    "goal as close as possible.\nAlso be sure to record everything you eat or" +
                    "your bmr will not be adjusted correctly. You can do it!");
        } else if(currentPlan.equals("Gain Muscle")) {
            if(weightChange > 1.5) {
                userRecommendation.setText("Great job meeting your calorie goal!\nYou are " +
                        "gaining weight a little too quickly so your calorie goal will be " +
                        "adjusted by " + bmrAdjustment + ".\nYour new calorie goal is " + finalBmr + ".");
            } else if(weightChange > 0.5) {
                userRecommendation.setText("Great job meeting your calorie goal!\nNo adjustments" +
                        "need to be made to your current diet.\nKeep up the good work!");
            } else if(weightChange > -0.5) {
                userRecommendation.setText("Great job meeting your calorie goal!\nYou are right on" +
                        "track to meet your fitness goal.\nKeep up the good work!");
            } else {
                userRecommendation.setText("Great job meeting your calorie goal!\nSince you are" +
                        " losing weight and your goal is to gain muscle, your calorie goal will" +
                        " be increased by " + bmrAdjustment + ".\nYour new calorie goal is " + finalBmr + ".");
            }
        } else if(currentPlan.equals("Lose Weight")) {
            if(weightChange > 0.5) {
                userRecommendation.setText("Great job meeting your calorie goal!\nYou are " +
                        "gaining weight while your goal is to lose weight so your calorie goal will be " +
                        "adjusted by " + bmrAdjustment + ".\nYour new calorie goal is " + finalBmr + ".");
            } else if(weightChange > -0.5) {
                userRecommendation.setText("Great job meeting your calorie goal!\nYou are not losing weight" +
                        " so your calorie goal will be adjusted by " + bmrAdjustment + "\nYour new calorie goal is " + finalBmr);
            } else if(weightChange > -1.5) {
                userRecommendation.setText("Great job meeting your calorie goal!\nYou are right on" +
                        " track to meet your fitness goal\nKeep up the good work!");
            } else {
                userRecommendation.setText("Great job meeting your calorie goal!\nYou are losing " +
                        "weight a little too quickly so your calorie goal will be adjusted by " +
                        bmrAdjustment + ".\nYour new calorie goal is " + finalBmr + ".");
            }
        } else {
            if(weightChange > 0.5) {
                userRecommendation.setText("Great job meeting your calorie goal!\nYou are gaining " +
                        "weight when your goal is to maintain your weight so your calorie goal " +
                        "will be adjusted by " + bmrAdjustment + ".\nYour new calorie goal is " + finalBmr + ".");
            } else if(weightChange > -0.5) {
                userRecommendation.setText("Great job meeting your calorie goal!\nYou are right on" +
                        "track to meet your fitness goal.\nKeep up the good work!");
            } else {
                userRecommendation.setText("Great job meeting your calorie goal!\nYou are losing " +
                        "weight when your goal is to maintain your weight so your calorie goal " +
                        "will be adjusted by " + bmrAdjustment + ".\nYour new calorie goal is " + finalBmr + ".");
            }
        }
    }

    // Method for opening the user profile
    public void openUserProfileActivity() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }
}