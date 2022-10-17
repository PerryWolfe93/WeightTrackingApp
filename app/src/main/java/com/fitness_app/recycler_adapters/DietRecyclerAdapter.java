package com.fitness_app.recycler_adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fitness_app.object_classes.Diet;
import com.fitness_app.object_classes.Food;
import com.fitness_app.R;

import java.util.ArrayList;

public class DietRecyclerAdapter extends RecyclerView.Adapter<DietRecyclerAdapter.MyViewHolder> {
    private final ArrayList<Diet> dietList;
    private final OnDietListener mOnDietListener;

    public DietRecyclerAdapter(ArrayList<Diet> dietList, OnDietListener onDietListener) {
        this.dietList = dietList;
        this.mOnDietListener = onDietListener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView dateText;
        private final TextView caloriesText;
        private final TextView proteinText;
        private final TextView carbText;
        private final TextView fatText;

        OnDietListener onDietListener;

        public MyViewHolder(final View view, OnDietListener onDietListener) {
            super(view);
            dateText = view.findViewById(R.id.tv_dietList_date);
            caloriesText = view.findViewById(R.id.tv_dietList_calories);
            proteinText = view.findViewById(R.id.tv_dietList_protein);
            carbText = view.findViewById(R.id.tv_dietList_carb);
            fatText = view.findViewById(R.id.tv_dietList_fat);
            this.onDietListener = onDietListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onDietListener.OnDietClick(getAdapterPosition());
        }
    }

    public interface OnDietListener {
        void OnDietClick(int position);
    }

    @NonNull
    @Override
    public DietRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.diet_list, parent, false);
        return new MyViewHolder(itemView, mOnDietListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DietRecyclerAdapter.MyViewHolder holder, int position) {

        ArrayList<String> foodArrayList = dietList.get(position).getFoodList();

        String date = dietList.get(position).getDate();
        int calories = 0;
        int protein = 0;
        int carb = 0;
        int fat = 0;

        for(int i = 0; i < foodArrayList.size(); i++) {

            Food food = Diet.checkCatalog(foodArrayList.get(0));

            calories += food.getCalories();
            protein += food.getProtein();
            carb += food.getCarbs();
            fat += food.getFat();
        }

        holder.dateText.setText(date);
        holder.caloriesText.setText(String.valueOf(calories));
        holder.proteinText.setText(String.valueOf(protein));
        holder.carbText.setText(String.valueOf(carb));
        holder.fatText.setText(String.valueOf(fat));
    }

    @Override
    public int getItemCount() {
        return dietList.size();
    }
}
