package com.example.proindividual;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class EndedTrainings extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private TextView playerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ended_trainings);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        playerTextView = findViewById(R.id.playertexview);

        String currentCoachId = getIntent().getStringExtra("coachId");
        String playerId = getIntent().getStringExtra("playerId");

        ImageButton backButton = findViewById(R.id.backbtn);
        backButton.setOnClickListener(v -> finish());

        ImageButton profileButton = findViewById(R.id.profilebtn);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(EndedTrainings.this, CoachProfile.class);
            startActivity(intent);
        });

        loadPlayerDetails(playerId);
        loadCompletedTrainings(currentCoachId, playerId);
    }

    private void loadPlayerDetails(String playerId) {
        DatabaseReference playerRef = FirebaseDatabase.getInstance().getReference("users").child(playerId);
        playerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Player player = dataSnapshot.getValue(Player.class);
                if (player != null) {
                    String fullName = player.getName() + " " + player.getSurname();
                    playerTextView.setText(fullName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.err.println("Listener was cancelled: " + databaseError.getMessage());
            }
        });
    }

    private void loadCompletedTrainings(String coachId, String playerId) {
        mDatabase = FirebaseDatabase.getInstance().getReference("trainings");
        mDatabase.orderByChild("playerUserId").equalTo(playerId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Training> trainings = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Training training = snapshot.getValue(Training.class);
                            if (training != null && training.isCompleted() && training.getCoachUserId().equals(coachId)) {
                                trainings.add(training);
                            }
                        }
                        TrainingAdapter adapter = new TrainingAdapter(EndedTrainings.this, trainings, true);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        System.err.println("Listener was cancelled");
                    }
                });
    }
}
