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
    private ArrayList<Diet> dietList;
    private OnDietListener mOnDietListener;

    public DietRecyclerAdapter(ArrayList<Diet> dietList, OnDietListener onDietListener) {
        this.dietList = dietList;
        this.mOnDietListener = onDietListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView dateText;
        private TextView caloriesText;
        private TextView proteinText;
        private TextView carbText;
        private TextView fatText;

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

        ArrayList<Food> foodArrayList = dietList.get(position).getFoodList();

        String date = dietList.get(position).getDate();
        int calories = 0;
        int protein = 0;
        int carb = 0;
        int fat = 0;

        for(int i = 0; i < foodArrayList.size(); i++) {
            calories += foodArrayList.get(i).getCalories();
            protein += foodArrayList.get(i).getProtein();
            carb += foodArrayList.get(i).getCarbs();
            fat += foodArrayList.get(i).getFat();
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
