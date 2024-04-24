package com.example.proindividual;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proindividual.models.Training;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrainingSessionActivity extends AppCompatActivity {
    private Chronometer chronometer;
    private boolean running = false;
    private long pauseOffset;
    private DatabaseReference mDatabase;
    private String trainingId;
    private TextView txtTrainingTitle, txtTrainingDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_session);

        trainingId = getIntent().getStringExtra("trainingId");
        mDatabase = FirebaseDatabase.getInstance().getReference("trainings");

        txtTrainingTitle = findViewById(R.id.textView16);
        txtTrainingDescription = findViewById(R.id.opistexview);
        chronometer = findViewById(R.id.chronometer);
        Button startButton = findViewById(R.id.startButton);
        Button stopButton = findViewById(R.id.stopButton);
        Button finishButton = findViewById(R.id.finishButton);
        ImageButton backButton = findViewById(R.id.backbtn);
        ImageButton profileButton = findViewById(R.id.profilebtn);

        loadTrainingDetails();

        startButton.setOnClickListener(v -> {
            if (!running) {
                chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                chronometer.start();
                running = true;
                stopButton.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.GONE);
            }
        });

        stopButton.setOnClickListener(v -> {
            if (running) {
                chronometer.stop();
                pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                running = false;
                finishButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.GONE);
            }
        });

        finishButton.setOnClickListener(v -> {
            long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
            saveTrainingDuration(elapsedMillis);
            Intent intent = new Intent(TrainingSessionActivity.this, TrainingEnding.class);
            intent.putExtra("trainingId", trainingId);
            startActivity(intent);
            finish();
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(TrainingSessionActivity.this, TrainingPlayerView.class);
            intent.putExtra("trainingId", trainingId);
            startActivity(intent);
            finish();
        });

        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(TrainingSessionActivity.this, PlayerProfile.class);
            startActivity(intent);
        });
    }

    private void loadTrainingDetails() {
        mDatabase.child(trainingId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Training training = dataSnapshot.getValue(Training.class);
                if (training != null) {
                    txtTrainingTitle.setText(training.getTitle());
                    txtTrainingDescription.setText(training.getDetails());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void saveTrainingDuration(long duration) {
        mDatabase.child(trainingId).child("duration").setValue(duration);
    }
}
