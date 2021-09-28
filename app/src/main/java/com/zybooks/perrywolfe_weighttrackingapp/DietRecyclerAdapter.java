package com.zybooks.perrywolfe_weighttrackingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DietRecyclerAdapter extends RecyclerView.Adapter<DietRecyclerAdapter.MyViewHolder> {
    private ArrayList<Diet> dietList;

    public DietRecyclerAdapter(ArrayList<Diet> dietList) {
        this.dietList = dietList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView dateText;
        private TextView caloriesText;

        public MyViewHolder(final View view) {
            super(view);
            dateText = view.findViewById(R.id.tv_dietList_date);
            caloriesText = view.findViewById(R.id.tv_dietList_calories);
        }
    }

    @NonNull
    @Override
    public DietRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.diet_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DietRecyclerAdapter.MyViewHolder holder, int position) {
        String date = dietList.get(position).getDate();
        int calories = dietList.get(position).getCalories();

        holder.dateText.setText(date);
        holder.caloriesText.setText(String.valueOf(calories));
    }

    @Override
    public int getItemCount() {
        return dietList.size();
    }
}
