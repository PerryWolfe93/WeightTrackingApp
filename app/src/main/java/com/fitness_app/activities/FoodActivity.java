package com.fitness_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.fitness_app.DatabaseHelper;
import com.fitness_app.object_classes.Diet;
import com.fitness_app.recycler_adapters.FoodRecyclerAdapter;
import com.fitness_app.object_classes.Food;
import com.fitness_app.R;

import java.util.ArrayList;

public class FoodActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    DatabaseHelper fitnessAppDB;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        TextView title = findViewById(R.id.tv_food_title);
        recyclerView = findViewById(R.id.rv_food_list);

        fitnessAppDB = new DatabaseHelper(this);

        // Set title to current date
        title.setText("List of foods recorded on " + Food.dateClicked);

        setAdapter();
    }

    // Method for populating the recyclerview with data from the diet data table
    private void setAdapter() {
        ArrayList<Food> foodArrayList = new ArrayList<>();
        ArrayList<String> foodList = fitnessAppDB.getFoodList();

        for(String food : foodList) {
            foodArrayList.add(Diet.checkCatalog(food));
        }

        FoodRecyclerAdapter adapter = new FoodRecyclerAdapter(foodArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}