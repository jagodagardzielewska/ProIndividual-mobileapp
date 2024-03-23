package com.example.proindividual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proindividual.adapters.PlayersAdapter;
import com.example.proindividual.models.Player;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CoachMain extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView titleTextView;




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        titleTextView = findViewById(R.id.title);
        ImageButton profileButton = findViewById(R.id.profilebtn);


        profileButton.setOnClickListener(v -> startActivity(new Intent(CoachMain.this, CoachProfile.class)));


        loadUserName();
        loadPlayers();
    }

    private void loadUserName() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    titleTextView.setText("Hej, " + name + "!");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(CoachMain.this, "Nie udało się załadować imienia.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loadPlayers() {
        String coachId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference coachPlayersRef = FirebaseDatabase.getInstance().getReference("CoachPlayers").child(coachId).child("playerUserIds");

        coachPlayersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> playerIds = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String playerId = snapshot.getValue(String.class);
                    playerIds.add(playerId);
                }
                loadPlayerDetails(playerIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CoachMain.this, "Nie udało się załadować zawodników.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPlayerDetails(List<String> playerIds) {
        List<Player> players = new ArrayList<>();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        for (String id : playerIds) {
            usersRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Player player = dataSnapshot.getValue(Player.class);
                    if (player != null) {
                        players.add(player);
                        if (players.size() == playerIds.size()) {
                            updateRecyclerView(players, playerIds);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(CoachMain.this, "Nie udało się załadować danych zawodnika.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void updateRecyclerView(List<Player> players, List<String> playerIds) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        PlayersAdapter adapter = new PlayersAdapter(this, players, playerIds, playerId -> {
            Intent intent = new Intent(CoachMain.this, PlayerDetails.class);
            intent.putExtra("playerId", playerId);
            startActivity(intent);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }



}
