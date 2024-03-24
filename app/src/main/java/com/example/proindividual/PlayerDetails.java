package com.example.proindividual;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.proindividual.adapters.TrainingAdapter;
import com.example.proindividual.models.CoachPlayer;
import com.example.proindividual.models.Player;
import com.example.proindividual.models.Training;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PlayerDetails extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ImageView profileImageView;
    private TextView nameTextView, surnameTextView;
    private String playerId;
    private Button endCooperationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_details);

        profileImageView = findViewById(R.id.profile_image);
        nameTextView = findViewById(R.id.nameTextView);
        surnameTextView = findViewById(R.id.surnameTextView);
        endCooperationButton = findViewById(R.id.end_button);

        playerId = getIntent().getStringExtra("playerId");

        Button addTrainingButton = findViewById(R.id.addtrening_button);
        addTrainingButton.setOnClickListener(v -> {
            Intent intent = new Intent(PlayerDetails.this, AddTraining.class);
            intent.putExtra("playerId", playerId);
            intent.putExtra("coachId", FirebaseAuth.getInstance().getCurrentUser().getUid());
            startActivity(intent);
        });

        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        loadPlayerDetails(playerId);
        loadTrainings(playerId);

        endCooperationButton.setOnClickListener(v -> showEndCooperationDialog());
    }

    private void showEndCooperationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Zakończenie współpracy")
                .setMessage("Czy na pewno chcesz zakończyć współpracę z tym zawodnikiem?")
                .setPositiveButton("Tak", (dialogInterface, i) -> {
                    endCooperation(FirebaseAuth.getInstance().getCurrentUser().getUid(), playerId);
                })
                .setNegativeButton("Nie", null)
                .show();
    }

    private void endCooperation(String coachUserId, String playerId) {
        DatabaseReference coachRef = FirebaseDatabase.getInstance().getReference("CoachPlayers").child(coachUserId);
        coachRef.child("playerUserIds").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, String> playersMap = (Map<String, String>) dataSnapshot.getValue();
                    String keyOfPlayerToRemove = null;
                    for (Map.Entry<String, String> entry : playersMap.entrySet()) {
                        if (entry.getValue().equals(playerId)) {
                            keyOfPlayerToRemove = entry.getKey();
                            break;
                        }
                    }
                    if (keyOfPlayerToRemove != null) {
                        coachRef.child("playerUserIds").child(keyOfPlayerToRemove).removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(PlayerDetails.this, "Zakończono współpracę z zawodnikiem.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PlayerDetails.this, CoachMain.class);
                                    startActivity(intent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(PlayerDetails.this, "Nie udało się zakończyć współpracy.", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(PlayerDetails.this, "Zawodnik nie znaleziony w bazie danych trenera.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PlayerDetails.this, "Trener nie ma przypisanych zawodników.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PlayerDetails.this, "Nie udało się załadować danych.", Toast.LENGTH_SHORT).show();
            }
        });
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

                String currentCoachId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                calculateAverageRatingsForCoach(playerId, currentCoachId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PlayerDetails.this, "Nie udało się załadować treningów.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateRecyclerView(List<Training> trainingList) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        TrainingAdapter adapter = new TrainingAdapter(this, trainingList, true);
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

    private void calculateAverageRatingsForCoach(String playerId, String currentCoachId) {
        DatabaseReference trainingsRef = FirebaseDatabase.getInstance().getReference("trainings");
        Query query = trainingsRef.orderByChild("playerUserId").equalTo(playerId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalSatisfaction = 0;
                double totalEffort = 0;
                long count = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Training training = snapshot.getValue(Training.class);
                    if (training != null && training.getCoachUserId().equals(currentCoachId) && training.getRatings() != null) {
                        totalSatisfaction += training.getRatings().getSatisfaction();
                        totalEffort += training.getRatings().getEffort();
                        count++;
                    }
                }

                if (count > 0) {
                    double averageSatisfaction = totalSatisfaction / count;
                    double averageEffort = totalEffort / count;


                    TextView averageSatisfactionView = findViewById(R.id.average_satisfaction);
                    TextView averageEffortView = findViewById(R.id.average_effort);
                    averageSatisfactionView.setText(String.format(Locale.getDefault(), "%.2f", averageSatisfaction));
                    averageEffortView.setText(String.format(Locale.getDefault(), "%.2f", averageEffort));
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PlayerDetails.this, "Błąd podczas obliczania średnich ocen.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
