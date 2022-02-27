package com.fitness_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fitness_app.BackgroundAnimator;
import com.fitness_app.DatabaseHelper;
import com.fitness_app.object_classes.User;
import com.fitness_app.recycler_adapters.WeightRecyclerAdapter;
import com.fitness_app.object_classes.Weight;
import com.fitness_app.R;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import static java.time.temporal.ChronoUnit.DAYS;

public class WeightActivity extends AppCompatActivity {

    // Variable declarations
    private EditText currentWeight;
    private RecyclerView recyclerView;
    private User user;
    private TextView userRecommendation;
    DatabaseHelper fitnessAppDB;
    BackgroundAnimator backgroundAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        // Initialize instance of DatabaseHelper
        fitnessAppDB = new DatabaseHelper(WeightActivity.this);

        // Call class method for background animation
        backgroundAnimator = new BackgroundAnimator();
        backgroundAnimator.animateBackground(findViewById(R.id.weight_layout));

        // Assign values to widget variables
        recyclerView = findViewById(R.id.rv_weight_list);
        currentWeight = findViewById(R.id.et_weight_editCurrentWeight);
        Button enterWeight = findViewById(R.id.btn_weight_changeWeight);
        Button back = findViewById(R.id.btn_weight_back);
        userRecommendation = findViewById(R.id.tv_weight_recommendation);

        // Retrieve current user information
        user = fitnessAppDB.getCurrentUserInfo();

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
                user.setCurrentWeight(weight);
                fitnessAppDB.updateUserInfo(user);

            } else {
                Toast.makeText(WeightActivity.this, "Please Enter Your Weight", Toast.LENGTH_SHORT).show();
            }
        });

        back.setOnClickListener(v -> openUserProfileActivity());

        setAdapter();
        getRecommendation();
    }

    private void getRecommendation() {

        ArrayList<Weight> lastTwoWeeksWeights = new ArrayList<>();

        if(User.online) {
            try {
                String sqlSelect = "SELECT * FROM Weight_Table WHERE UserID = " + fitnessAppDB.getUserID(User.currentUser) + " ORDER BY EntryID DESC";
                Statement statement = User.connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlSelect);
                int i = 0;
                while(resultSet.next() && i <= 14) {
                    Weight weight = new Weight(resultSet.getDouble(2), resultSet.getString(3));
                    lastTwoWeeksWeights.add(weight);
                    i++;
                }
            } catch (Exception exception) {
                Log.e("Error", exception.getMessage());
            }
        } else {
            lastTwoWeeksWeights = fitnessAppDB.getLastTwoWeeksWeights();
        }

        if(lastTwoWeeksWeights == null) {
            // Returns if user does not have at least one data entry per each of the past two weeks
            userRecommendation.setText("Keep recording your weights everyday for the most accurate feedback.");
            return;
        }

        double lastWeekAvgWeight = 0;
        double twoWeeksAgoAvgWeight = 0;
        int count = 0;
        for(int i = 0; i < lastTwoWeeksWeights.size(); i++) {
            if(LocalDate.parse(lastTwoWeeksWeights.get(i).getDate()).until(LocalDate.now(), DAYS) <= 7) {
                lastWeekAvgWeight += lastTwoWeeksWeights.get(i).getWeight();
                count++;
            } else {
                break;
            }
        }
        if(count == 0) {
            return;
        }
        lastWeekAvgWeight /= count;
        count = 0;
        for(int i = 0; i < lastTwoWeeksWeights.size(); i++) {
            if(LocalDate.parse(lastTwoWeeksWeights.get(i).getDate()).until(LocalDate.now(), DAYS) > 7
                    && LocalDate.parse(lastTwoWeeksWeights.get(i).getDate()).until(LocalDate.now(), DAYS) <= 14) {
                twoWeeksAgoAvgWeight += lastTwoWeeksWeights.get(i).getWeight();
                count++;
            } else if(LocalDate.parse(lastTwoWeeksWeights.get(i).getDate()).until(LocalDate.now(), DAYS) > 14) {
                break;
            }
        }
        if(count == 0) {
            return;
        }
        twoWeeksAgoAvgWeight /= count;

        double weightChange = lastWeekAvgWeight - twoWeeksAgoAvgWeight;

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