package com.example.proindividual;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proindividual.models.Training;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class TrainingsSummaryCoach extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private ListView summaryListView;
    private Map<String, Long> categoryDurations = new HashMap<>();
    private String playerId;
    private String coachId;
    private TextView playerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainings_summary_coach);

        summaryListView = findViewById(R.id.summaryListView);
        playerTextView = findViewById(R.id.playername);
        playerId = getIntent().getStringExtra("playerId");
        coachId = getIntent().getStringExtra("coachId");

        ImageButton profileButton = findViewById(R.id.profilebtn);
        profileButton.setOnClickListener(v -> navigateToCoachProfile());

        ImageButton backButton = findViewById(R.id.backbtn);
        backButton.setOnClickListener(v -> finish());

        mDatabase = FirebaseDatabase.getInstance().getReference();
        loadPlayerDetails();
        loadSummaryData();
    }

    private void loadPlayerDetails() {
        mDatabase.child("users").child(playerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String firstName = dataSnapshot.child("name").getValue(String.class);
                String lastName = dataSnapshot.child("surname").getValue(String.class);
                if (firstName != null && lastName != null) {
                    playerTextView.setText(firstName + " " + lastName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(TrainingsSummaryCoach.this, "Nie udało się załadować danych zawodnika.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSummaryData() {
        mDatabase.child("trainings").orderByChild("playerUserId").equalTo(playerId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Training training = snapshot.getValue(Training.class);
                            if (training != null && training.isCompleted() && training.getCoachUserId().equals(coachId)) {
                                String category = training.getCategory();
                                Long duration = training.getDuration();
                                categoryDurations.put(category, categoryDurations.getOrDefault(category, 0L) + duration);
                            }
                        }
                        updateListView();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.err.println("Listener was cancelled");
                    }
                });
    }

    private void updateListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        for (Map.Entry<String, Long> entry : categoryDurations.entrySet()) {
            adapter.add(entry.getKey() + ": " + formatDuration(entry.getValue()));
        }
        summaryListView.setAdapter(adapter);
    }

    private String formatDuration(Long durationMs) {
        long seconds = (durationMs / 1000) % 60;
        long minutes = (durationMs / (1000 * 60)) % 60;
        long hours = (durationMs / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void navigateToCoachProfile() {
        Intent intent = new Intent(this, CoachProfile.class);
        startActivity(intent);
    }
}
