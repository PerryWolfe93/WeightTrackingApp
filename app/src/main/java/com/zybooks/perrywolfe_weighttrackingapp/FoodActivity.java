package com.zybooks.perrywolfe_weighttrackingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

public class FoodActivity extends AppCompatActivity {

    private TextView title;
    private RecyclerView recyclerView;

    DatabaseHelper fitnessAppDB;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        title = findViewById(R.id.tv_food_title);
        recyclerView = findViewById(R.id.rv_food_list);

        fitnessAppDB = new DatabaseHelper(this);

        // Set title to current date
        title.setText("List of foods recorded on " + Food.dateClicked);

        setAdapter();
    }

    // Method for populating the recyclerview with data from the diet data table
    private void setAdapter() {
        FoodRecyclerAdapter adapter = new FoodRecyclerAdapter(fitnessAppDB.getFoodList());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}