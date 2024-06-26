package com.example.proindividual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.proindividual.adapters.TrainingAdapter;
import com.example.proindividual.models.Player;
import com.example.proindividual.models.Training;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PlayerMain extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        loadUserTrainings();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_main);

        ImageButton profileButton = findViewById(R.id.profilebtn);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerMain.this, PlayerProfile.class);
                startActivity(intent);
            }
        });
        loadUserTrainings();
    }
    private void loadUserTrainings() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("trainings");
        ref.orderByChild("playerUserId").equalTo(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Training> trainingList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Training training = snapshot.getValue(Training.class);
                    if (training != null && !training.isCompleted()) {
                        trainingList.add(training);
                    }
                }
                updateRecyclerView(trainingList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PlayerMain.this, "Nie udało się załadować treningów.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateRecyclerView(List<Training> trainingList) {
        TextView noTrainingsText = findViewById(R.id.no_trainings_text);
        Button addCoachButton = findViewById(R.id.add_coach_button);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        if (trainingList.isEmpty()) {
            noTrainingsText.setVisibility(View.VISIBLE);
            addCoachButton.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noTrainingsText.setVisibility(View.GONE);
            addCoachButton.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            TrainingAdapter adapter = new TrainingAdapter(this, trainingList, false);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }
    }


    public void goToAddCoachCode(View view) {
        Intent intent = new Intent(this, AddingCoachCode.class);
        startActivity(intent);
    }


}