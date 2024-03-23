package com.example.proindividual.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proindividual.R;
import com.example.proindividual.models.Training;

import java.util.List;

public class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.ViewHolder> {

    private List<Training> trainingList;
    private Context context;

    public TrainingAdapter(Context context, List<Training> trainingList) {
        this.context = context;
        this.trainingList = trainingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_training, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Training training = trainingList.get(position);
        holder.titleTextView.setText(training.getTitle());
        holder.categoryTextView.setText(training.getCategory());
        holder.dateTextView.setText(training.getDate());
    }

    @Override
    public int getItemCount() {
        return trainingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView, categoryTextView, dateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.trainingTitleTextView);
            categoryTextView = itemView.findViewById(R.id.trainingCategoryTextView);
            dateTextView = itemView.findViewById(R.id.trainingDateTextView);
        }
    }
}
