package com.example.proindividual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.proindividual.adapters.TrainingAdapter;
import com.example.proindividual.models.Player;
import com.example.proindividual.models.Training;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PlayerDetails extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ImageView profileImageView;
    private TextView nameTextView, surnameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_details);


        profileImageView = findViewById(R.id.profile_image);
        nameTextView = findViewById(R.id.nameTextView);
        surnameTextView = findViewById(R.id.surnameTextView);


        String playerId = getIntent().getStringExtra("playerId");

        Button addTrainingButton = findViewById(R.id.addtrening_button);
        addTrainingButton.setOnClickListener(v -> {
            Intent intent = new Intent(PlayerDetails.this, AddTraining.class);
            intent.putExtra("playerId", playerId);
            startActivity(intent);
        });


        mDatabase = FirebaseDatabase.getInstance().getReference("users");


        loadPlayerDetails(playerId);
        loadTrainings(playerId);

    }

    private void loadTrainings(String playerId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("trainings");
        ref.orderByChild("playerUserId").equalTo(playerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Training> trainingList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Training training = snapshot.getValue(Training.class);
                    trainingList.add(training);
                }
                updateRecyclerView(trainingList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PlayerDetails.this, "Nie udało się załadować treningów.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateRecyclerView(List<Training> trainingList) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        TrainingAdapter adapter = new TrainingAdapter(this, trainingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    private void loadPlayerDetails(String playerId) {
        mDatabase.child(playerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Player player = dataSnapshot.getValue(Player.class);
                if (player != null) {
                    nameTextView.setText(player.getName());
                    surnameTextView.setText(player.getSurname());
                    Glide.with(PlayerDetails.this)
                            .load(player.getProfileImageUrl())
                            .placeholder(R.drawable.profile_image)
                            .into(profileImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PlayerDetails.this, "Nie udało się załadować danych gracza.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
