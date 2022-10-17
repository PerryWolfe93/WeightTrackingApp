package com.fitness_app.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fitness_app.DatabaseHelper;
import com.fitness_app.object_classes.Diet;
import com.fitness_app.object_classes.Food;
import com.fitness_app.object_classes.User;
import com.fitness_app.recycler_adapters.DietRecyclerAdapter;
import com.fitness_app.R;
import com.fitness_app.recycler_adapters.FoodRecyclerAdapter;

import java.util.ArrayList;

public class DietActivity extends AppCompatActivity implements DietRecyclerAdapter.OnDietListener {

    // Variable Declarations
    private DatabaseHelper fitnessAppDB;
    private RecyclerView dietRecyclerView, foodRecyclerView;
    private TextView userRecommendation;
    private User user;
    private EditText foodName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        // Initialize instance of DatabaseHelper
        fitnessAppDB = new DatabaseHelper(DietActivity.this);

        // Get Food Catalog
        Diet.foodCatalog = fitnessAppDB.getFoodCatalog();

        // Call class method for background animation
//        BackgroundAnimator backgroundAnimator = new BackgroundAnimator();
//        backgroundAnimator.animateBackground(findViewById(R.id.user_profile_layout));

        // Initialize widget variables
        dietRecyclerView = findViewById(R.id.rv_diet_list);
        foodRecyclerView = findViewById(R.id.rv_diet_foods);
        foodName = findViewById(R.id.et_diet_foodName);
        Button add = findViewById(R.id.btn_diet_add);
        Button remove = findViewById(R.id.btn_diet_remove);
        Button catalog = findViewById(R.id.btn_diet_catalog);
        Button back = findViewById(R.id.btn_diet_back);
        userRecommendation = findViewById(R.id.tv_diet_userRecommendation);

        user = fitnessAppDB.getCurrentUserInfo();

        // If today's date is not in the database, create new entry, else get data from database
        if(!fitnessAppDB.checkForDietData()) {
            fitnessAppDB.addDiet();
        }

        // Set click listeners

        // Button for adding a food to today's diet
        add.setOnClickListener(view -> {

            String foodEntry = foodName.getText().toString();

            if(foodEntry.equals("")) {
                Toast.makeText(DietActivity.this, "Enter a food saved in your food catalog", Toast.LENGTH_SHORT).show();
            } else if(Diet.checkCatalog(foodEntry) == null) {
                Toast.makeText(DietActivity.this, "That food has not been added to your food catalog yet", Toast.LENGTH_SHORT).show();
            } else {
                fitnessAppDB.addFood(foodEntry);
                setDietAdapter();
                setFoodAdapter();
            }
        });

        // Click listener for button to navigate to Food Catalog activity
        catalog.setOnClickListener(view -> openFoodCatalog());

        // Button for moving back to the user profile page
        back.setOnClickListener(v -> openUserProfileActivity());

        // Sets an adapter used to create the current day's food list
        if(fitnessAppDB.getFoodList() != null) {
            setFoodAdapter();
        }

        // Sets an adapter used to create the diet list
        setDietAdapter();

        // Sets user recommendation text view
        getRecommendation();
    }

    // Method for populating the recyclerview with data from the diet data table
    private void setDietAdapter() {
        DietRecyclerAdapter adapter = new DietRecyclerAdapter(fitnessAppDB.getDietList(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        dietRecyclerView.setLayoutManager(layoutManager);
        dietRecyclerView.setItemAnimator(new DefaultItemAnimator());
        dietRecyclerView.setAdapter(adapter);
    }

    private void setFoodAdapter() {

        ArrayList<Food> foodArrayList = new ArrayList<>();
        ArrayList<String> foodList = fitnessAppDB.getFoodList();

        for(String food : foodList) {
            foodArrayList.add(Diet.checkCatalog(food));
        }

        Toast.makeText(DietActivity.this, foodList.get(0), Toast.LENGTH_SHORT).show();

        FoodRecyclerAdapter adapter = new FoodRecyclerAdapter(foodArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        foodRecyclerView.setLayoutManager(layoutManager);
        foodRecyclerView.setItemAnimator(new DefaultItemAnimator());
        foodRecyclerView.setAdapter(adapter);
    }

    // Method for calculating getting diet recommendations
    @SuppressLint("SetTextI18n")
    private void getRecommendation() {

        String currentPlan = user.getFitnessPlan();
        double finalBmr = user.getBMR() + user.getBmrAdjustment();
        double bmrAdjustment = user.getBmrAdjustment();
        double calorieDifferenceFromGoal = user.getCalorieDifferenceFromGoal();
        double weightChange = user.getWeightChange();


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
        } else if(weightChange < -99) {
            userRecommendation.setText("Enter your information daily to get the most accurate feedback." +
                    "\nCurrently your calorie goal is " + finalBmr);
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
    // Method for opening the food catalog activity
    public void openFoodCatalog() {
        Intent intent = new Intent(this, FoodCatalogActivity.class);
        startActivity(intent);
    }

    @Override
    public void OnDietClick(int position) {
        Intent intent = new Intent(this, FoodActivity.class);
        Food.dateClicked = fitnessAppDB.getDietList().get(position).getDate();
        startActivity(intent);
    }
}