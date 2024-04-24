package com.example.proindividual;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proindividual.models.Training;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrainingPlayerView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_player_view);


        String trainingId = getIntent().getStringExtra("trainingId");

        Button endButton = findViewById(R.id.end_button);
        endButton.setOnClickListener(v -> {
            Intent intent = new Intent(TrainingPlayerView.this, TrainingSessionActivity.class);
            intent.putExtra("trainingId", trainingId);
            startActivity(intent);
        });

        ImageButton backButton = findViewById(R.id.backbtn);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(TrainingPlayerView.this, PlayerMain.class);
            startActivity(intent);
        });
        ImageButton profileButton = findViewById(R.id.profilebtn);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(TrainingPlayerView.this, PlayerProfile.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("trainings");


        mDatabase.child(trainingId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Training training = dataSnapshot.getValue(Training.class);
                if (training != null) {

                    TextView titleTextView = findViewById(R.id.textView7);
                    TextView dateTextView = findViewById(R.id.datetextView);
                    TextView categoryTextView = findViewById(R.id.categorytextView);
                    TextView descriptionTextView = findViewById(R.id.opistextView);
                    TextView coachTextView = findViewById(R.id.coachtexview);

                    titleTextView.setText(training.getTitle());
                    dateTextView.setText(training.getDate());
                    categoryTextView.setText(training.getCategory());
                    descriptionTextView.setText(training.getDetails());

                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                    usersRef.child(training.getCoachUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String name = dataSnapshot.child("name").getValue(String.class);
                            String surname = dataSnapshot.child("surname").getValue(String.class);
                            if (name != null && surname != null) {
                                coachTextView.setText("Trener: " + name + " " + surname);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(TrainingPlayerView.this, "Nie udało się pobrać danych trenera.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TrainingPlayerView.this, "Nie udało się pobrać danych treningu.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
