package com.example.proindividual;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proindividual.models.Training;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

public class TrainingsSummary extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private ListView summaryListView;
    private Map<String, Long> categoryDurations = new HashMap<>();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainings_summary);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("trainings");
        summaryListView = findViewById(R.id.summaryListView);

        ImageButton profileButton = findViewById(R.id.profilebtn);
        profileButton.setOnClickListener(v -> navigateToProfile());

        ImageButton backButton = findViewById(R.id.backbtn);
        backButton.setOnClickListener(v -> navigateToProfile());

        loadSummaryData();
    }

    private void loadSummaryData() {
        String currentUserId = mAuth.getCurrentUser().getUid();
        mDatabase.orderByChild("playerUserId").equalTo(currentUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Training training = snapshot.getValue(Training.class);
                            if (training != null && training.isCompleted()) {
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

    private void navigateToProfile() {
        Intent intent = new Intent(this, PlayerProfile.class);
        startActivity(intent);
    }
}

