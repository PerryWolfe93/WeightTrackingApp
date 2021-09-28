package com.zybooks.perrywolfe_weighttrackingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExerciseRecyclerAdapter extends RecyclerView.Adapter<ExerciseRecyclerAdapter.MyViewHolder> {
    private ArrayList<Exercise> exerciseList;

    public ExerciseRecyclerAdapter(ArrayList<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView dateText;
        private TextView exerciseTypeText;
        private TextView exerciseTimeText;

        public MyViewHolder(final View view) {
            super(view);
            dateText = view.findViewById(R.id.tv_exerciseList_date);
            exerciseTypeText = view.findViewById(R.id.tv_exerciseList_exerciseType);
            exerciseTimeText = view.findViewById(R.id.tv_exerciseList_exerciseTime);
        }
    }

    @NonNull
    @Override
    public ExerciseRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_list, parent, false);
        return new ExerciseRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseRecyclerAdapter.MyViewHolder holder, int position) {
        String date = exerciseList.get(position).getDate();
        String exerciseType = exerciseList.get(position).getExerciseType();
        int exerciseTime = exerciseList.get(position).getTime();

        holder.dateText.setText(date);
        holder.exerciseTypeText.setText(exerciseType);
        holder.exerciseTimeText.setText(String.valueOf(exerciseTime));
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }
}
