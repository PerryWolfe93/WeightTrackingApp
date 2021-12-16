package com.zybooks.perrywolfe_weighttrackingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodRecyclerAdapter extends RecyclerView.Adapter<FoodRecyclerAdapter.MyViewHolder>{
    private ArrayList<Food> foodList;

    public FoodRecyclerAdapter(ArrayList<Food> foodList) {
        this.foodList = foodList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView foodText;
        private TextView caloriesText;
        private TextView proteinText;
        private TextView carbText;
        private TextView fatText;

        public MyViewHolder(final View view) {
            super(view);
            foodText = view.findViewById(R.id.tv_foodList_name);
            caloriesText = view.findViewById(R.id.tv_foodList_calories);
            proteinText = view.findViewById(R.id.tv_foodList_protein);
            carbText = view.findViewById(R.id.tv_foodList_carb);
            fatText = view.findViewById(R.id.tv_foodList_fat);
        }
    }

    @NonNull
    @Override
    public FoodRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list, parent, false);
        return new FoodRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodRecyclerAdapter.MyViewHolder holder, int position) {

        String name = foodList.get(position).getName();
        int calories = foodList.get(position).getCalories();
        int protein = foodList.get(position).getProtein();;
        int carb = foodList.get(position).getCarbs();;
        int fat = foodList.get(position).getFat();;

        holder.foodText.setText(name);
        holder.caloriesText.setText(String.valueOf(calories));
        holder.proteinText.setText(String.valueOf(protein));
        holder.carbText.setText(String.valueOf(carb));
        holder.fatText.setText(String.valueOf(fat));
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
}
