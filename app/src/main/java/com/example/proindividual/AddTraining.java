package com.example.proindividual;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.proindividual.models.Training;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTraining extends AppCompatActivity {

    private EditText titleEditText, categoryEditText, dateEditText, detailsEditText;
    private Button addButton;
    private DatabaseReference mDatabase;
    private String playerId, coachUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_training);


        titleEditText = findViewById(R.id.titleEditText);
        categoryEditText = findViewById(R.id.categoryEditText);
        dateEditText = findViewById(R.id.dateEditText);
        detailsEditText = findViewById(R.id.opisEditText);
        addButton = findViewById(R.id.add_button);


        playerId = getIntent().getStringExtra("playerId");

        coachUserId = getIntent().getStringExtra("coachId");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTraining();
            }
        });
    }

    private void addTraining() {
        String title = titleEditText.getText().toString().trim();
        String category = categoryEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String details = detailsEditText.getText().toString().trim();

        String trainingId = mDatabase.child("trainings").push().getKey();

        Training training = new Training(trainingId, coachUserId, playerId, title, category, date, details);

        mDatabase.child("trainings").child(trainingId).setValue(training).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AddTraining.this, "Trening dodany pomyślnie", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AddTraining.this, "Błąd podczas dodawania treningu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
