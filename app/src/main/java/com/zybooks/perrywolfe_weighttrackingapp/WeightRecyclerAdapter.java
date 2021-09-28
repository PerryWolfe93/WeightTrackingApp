package com.zybooks.perrywolfe_weighttrackingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class WeightRecyclerAdapter extends RecyclerView.Adapter<WeightRecyclerAdapter.MyViewHolder> {
    private ArrayList<Weight> weightList;

    public WeightRecyclerAdapter(ArrayList<Weight> weightList) {
        this.weightList = weightList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView dateText;
        private TextView weightText;

        public MyViewHolder(final View view) {
            super(view);
            dateText = view.findViewById(R.id.tv_weightList_date);
            weightText = view.findViewById(R.id.tv_weightList_weight);
        }
    }

    @NonNull
    @Override
    public WeightRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.weight_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeightRecyclerAdapter.MyViewHolder holder, int position) {
        String date = weightList.get(position).getDate();
        float weight = weightList.get(position).getWeight();

        holder.dateText.setText(date);
        holder.weightText.setText(String.valueOf(weight));
    }

    @Override
    public int getItemCount() {
        return weightList.size();
    }
}
