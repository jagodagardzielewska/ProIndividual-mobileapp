package com.example.proindividual.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proindividual.R;
import com.example.proindividual.models.Player;

import java.util.List;

public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.ViewHolder> {

    private List<Player> playersList;
    private List<String> playerIds;
    private Context context;
    private OnItemClickListener listener;


    public interface OnItemClickListener {
        void onItemClick(String playerId);
    }


    public PlayersAdapter(Context context, List<Player> playersList, List<String> playerIds, OnItemClickListener listener) {
        this.context = context;
        this.playersList = playersList;
        this.playerIds = playerIds;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Player player = playersList.get(position);
        holder.nameTextView.setText(player.getName() + " " + player.getSurname());

        if (player.getProfileImageUrl() != null && !player.getProfileImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(player.getProfileImageUrl())
                    .placeholder(R.drawable.profile_image)
                    .into(holder.profileImageView);
        } else {
            holder.profileImageView.setImageResource(R.drawable.profile_image);
        }


        holder.itemView.setOnClickListener(v -> listener.onItemClick(playerIds.get(position)));
    }

    @Override
    public int getItemCount() {
        return playersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public ImageView profileImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.player_name);
            profileImageView = itemView.findViewById(R.id.player_profile_image);
        }
    }
}
