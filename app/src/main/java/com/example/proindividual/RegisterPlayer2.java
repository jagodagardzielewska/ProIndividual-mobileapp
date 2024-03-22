package com.example.proindividual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.proindividual.models.Player;

public class RegisterPlayer2 extends AppCompatActivity {

    EditText nameEditText, surnameEditText;
    Button next_button2;
    TextView namesError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_player2);

        nameEditText = findViewById(R.id.nameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        next_button2 = findViewById(R.id.next_button2);
        namesError = findViewById(R.id.namesError);

        namesError.setVisibility(View.INVISIBLE);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                namesError.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        nameEditText.addTextChangedListener(textWatcher);
        surnameEditText.addTextChangedListener(textWatcher);


        next_button2.setOnClickListener(v -> {
            boolean isNameFilled = !nameEditText.getText().toString().trim().isEmpty();
            boolean isSurnameFilled = !surnameEditText.getText().toString().trim().isEmpty();

            if (isNameFilled && isSurnameFilled) {
                Player player = (Player) getIntent().getSerializableExtra("player");
                player.setName(nameEditText.getText().toString().trim());
                player.setSurname(surnameEditText.getText().toString().trim());

                Intent intent = new Intent(RegisterPlayer2.this, RegisterPlayer3.class);
                intent.putExtra("player", player);
                startActivity(intent);
            } else {
                namesError.setVisibility(View.VISIBLE);
            }
        });
    }
}
