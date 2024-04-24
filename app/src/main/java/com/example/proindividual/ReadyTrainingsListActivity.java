package com.example.proindividual;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proindividual.adapters.ReadyTrainingAdapter;
import com.example.proindividual.models.ReadyTraining;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReadyTrainingsListActivity extends AppCompatActivity {

    private RecyclerView readyTrainingsRecycler;
    private ReadyTrainingAdapter adapter;
    private DatabaseReference mDatabase;
    private String playerId, coachId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_trainings_list);

        playerId = getIntent().getStringExtra("playerId");
        coachId = getIntent().getStringExtra("coachId");

        mDatabase = FirebaseDatabase.getInstance().getReference("readyTrainings");
        readyTrainingsRecycler = findViewById(R.id.ready_trainings_recycler);
        readyTrainingsRecycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ReadyTrainingAdapter(new ArrayList<>(), this, playerId, coachId);
        readyTrainingsRecycler.setAdapter(adapter);

        loadReadyTrainings();

        ImageButton backButton = findViewById(R.id.backbtn);
        backButton.setOnClickListener(v -> finish());

        ImageButton profileButton = findViewById(R.id.profilebtn);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(ReadyTrainingsListActivity.this, CoachProfile.class);
            startActivity(intent);
        });
    }

    private void loadReadyTrainings() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ReadyTraining> readyTrainings = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ReadyTraining readyTraining = snapshot.getValue(ReadyTraining.class);
                    if (readyTraining != null) {
                        readyTrainings.add(readyTraining);
                    }
                }
                adapter.updateData(readyTrainings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReadyTrainingsListActivity.this, "Błąd podczas ładowania gotowych treningów.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
