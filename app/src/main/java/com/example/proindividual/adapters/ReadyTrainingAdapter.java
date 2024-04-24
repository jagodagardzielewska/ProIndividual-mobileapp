package com.example.proindividual.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proindividual.R;
import com.example.proindividual.ReadyTrainingDetailsActivity;
import com.example.proindividual.models.ReadyTraining;

import java.util.List;

public class ReadyTrainingAdapter extends RecyclerView.Adapter<ReadyTrainingAdapter.ViewHolder> {

    private List<ReadyTraining> readyTrainings;
    private Context context;
    private String playerId;
    private String coachId;


    public ReadyTrainingAdapter(List<ReadyTraining> readyTrainings, Context context, String playerId, String coachId) {
        this.readyTrainings = readyTrainings;
        this.context = context;
        this.playerId = playerId;
        this.coachId = coachId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ready_training, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReadyTraining readyTraining = readyTrainings.get(position);
        holder.titleTextView.setText(readyTraining.getTitle());
        holder.categoryTextView.setText(readyTraining.getCategory());
        holder.detailsTextView.setText(readyTraining.getDetails());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReadyTrainingDetailsActivity.class);
            intent.putExtra("readyTrainingId", readyTraining.getReadyTrainingId());
            intent.putExtra("title", readyTraining.getTitle());
            intent.putExtra("category", readyTraining.getCategory());
            intent.putExtra("details", readyTraining.getDetails());
            intent.putExtra("playerId", playerId);
            intent.putExtra("coachId", coachId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return readyTrainings.size();
    }

    public void updateData(List<ReadyTraining> newReadyTrainings) {
        this.readyTrainings.clear();
        this.readyTrainings.addAll(newReadyTrainings);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView categoryTextView;
        public TextView detailsTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.trainingTitleTextView);
            categoryTextView = itemView.findViewById(R.id.trainingCategoryTextView);
            detailsTextView = itemView.findViewById(R.id.trainingDetailsTextView);
        }
    }
}

