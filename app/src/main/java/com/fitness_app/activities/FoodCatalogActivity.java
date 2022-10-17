package com.fitness_app.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fitness_app.DatabaseHelper;
import com.fitness_app.R;
import com.fitness_app.object_classes.Food;
import com.fitness_app.recycler_adapters.FoodRecyclerAdapter;

public class FoodCatalogActivity extends AppCompatActivity {

    private EditText foodEntry, caloriesEntry, proteinEntry, carbEntry, fatEntry;
    private DatabaseHelper fitnessAppDB;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_catalog);

        foodEntry = findViewById(R.id.et_foodCatalog_food);
        caloriesEntry = findViewById(R.id.et_foodCatalog_calories);
        proteinEntry = findViewById(R.id.et_foodCatalog_protein);
        carbEntry = findViewById(R.id.et_foodCatalog_carb);
        fatEntry = findViewById(R.id.et_foodCatalog_fat);
        Button addToCatalog = findViewById(R.id.btn_foodCatalog_add);
        recyclerView = findViewById(R.id.rv_food_list);

        fitnessAppDB = new DatabaseHelper(FoodCatalogActivity.this);

        addToCatalog.setOnClickListener(v -> {

            String foodName = foodEntry.getText().toString().trim();
            String calories = caloriesEntry.getText().toString().trim();
            String proteins = proteinEntry.getText().toString().trim();
            String carbs = carbEntry.getText().toString().trim();
            String fat = fatEntry.getText().toString().trim();

            if(foodName.equals("") || calories.equals("") || proteins.equals("") || carbs.equals("") || fat.equals("")) {
                Toast.makeText(FoodCatalogActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                Food food = new Food(foodName, Integer.parseInt(calories), Integer.parseInt(proteins), Integer.parseInt(carbs), Integer.parseInt(fat));
                boolean success = fitnessAppDB.addToCatalog(food);

                if(success) {
                    Toast.makeText(FoodCatalogActivity.this, foodName + " added to food catalog", Toast.LENGTH_SHORT).show();
                    setAdapter();
                } else {
                    Toast.makeText(FoodCatalogActivity.this, "Failed to add " + foodName + " to food catalog", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(fitnessAppDB.getFoodCatalog() != null) {
            setAdapter();
        }

    }

    private void setAdapter() {
        FoodRecyclerAdapter adapter = new FoodRecyclerAdapter(fitnessAppDB.getFoodCatalog());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}
