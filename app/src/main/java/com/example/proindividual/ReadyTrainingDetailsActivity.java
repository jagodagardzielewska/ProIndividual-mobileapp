package com.example.proindividual;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proindividual.models.ReadyTraining;
import com.example.proindividual.models.Training;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReadyTrainingDetailsActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private String readyTrainingId;
    private TextView titleTextView, categoryTextView, detailsTextView;
    private EditText dateEditText;
    private Button addTrainingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_training_details);

        titleTextView = findViewById(R.id.training_title);
        categoryTextView = findViewById(R.id.training_category);
        detailsTextView = findViewById(R.id.training_details);
        dateEditText = findViewById(R.id.training_date);
        addTrainingButton = findViewById(R.id.add_training_button);

        Intent intent = getIntent();
        readyTrainingId = intent.getStringExtra("readyTrainingId");
        String playerId = intent.getStringExtra("playerId");
        String coachId = intent.getStringExtra("coachId");
        String title = intent.getStringExtra("title");
        String category = intent.getStringExtra("category");
        String details = intent.getStringExtra("details");

        titleTextView.setText(title);
        categoryTextView.setText(category);
        detailsTextView.setText(details);


        if (readyTrainingId == null || playerId == null || coachId == null) {
            Toast.makeText(this, "Wystąpił błąd podczas przekazywania danych.", Toast.LENGTH_LONG).show();
            finish();
        }


        mDatabase = FirebaseDatabase.getInstance().getReference("readyTrainings").child(readyTrainingId);

        loadTrainingDetails();

        addTrainingButton.setOnClickListener(v -> addTrainingToPlayer(playerId, coachId));
    }


    private void loadTrainingDetails() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ReadyTraining readyTraining = dataSnapshot.getValue(ReadyTraining.class);
                if (readyTraining != null) {
                    titleTextView.setText(readyTraining.getTitle());
                    categoryTextView.setText(readyTraining.getCategory());
                    detailsTextView.setText(readyTraining.getDetails());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReadyTrainingDetailsActivity.this, "Nie udało się załadować szczegółów treningu.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTrainingToPlayer(String playerId, String coachId) {
        String date = dateEditText.getText().toString().trim();
        if (date.isEmpty()) {
            Toast.makeText(this, "Proszę wprowadzić datę.", Toast.LENGTH_SHORT).show();
            return;
        }

        String title = titleTextView.getText().toString();
        String category = categoryTextView.getText().toString();
        String details = detailsTextView.getText().toString();

        DatabaseReference trainingsRef = FirebaseDatabase.getInstance().getReference("trainings");
        String trainingId = trainingsRef.push().getKey();
        Training newTraining = new Training(trainingId, coachId, playerId, title, category, date, details, false);

        trainingsRef.child(trainingId).setValue(newTraining).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Trening dodany pomyślnie.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Błąd podczas dodawania treningu.", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
