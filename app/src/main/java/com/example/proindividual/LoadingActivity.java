package com.example.proindividual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_activity);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isNetworkAvailable()) {
                    startActivity(new Intent(LoadingActivity.this, NoInternetActivity.class));
                    finish();
                } else {
                    checkUserAndNavigate();
                }
            }
        },3000);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void checkUserAndNavigate() {
        getCurrentUserRole().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    String role = task.getResult();
                    Intent intent;
                    if (role != null) {
                        if (role.equals("coach")) {
                            intent = new Intent(LoadingActivity.this, CoachMain.class);
                        } else {
                            intent = new Intent(LoadingActivity.this, PlayerMain.class);
                        }
                    } else {
                        intent = new Intent(LoadingActivity.this, LoginActivity.class);
                    }
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private Task<String> getCurrentUserRole() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        TaskCompletionSource<String> taskCompletionSource = new TaskCompletionSource<>();

        if (currentUser == null) {
            taskCompletionSource.setResult(null); // Użytkownik niezalogowany
        } else {
            String userId = currentUser.getUid();
            DatabaseReference roleRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("role");

            roleRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        taskCompletionSource.setResult(dataSnapshot.getValue(String.class));
                    } else {
                        FirebaseAuth.getInstance().signOut(); // Użytkownik nie ma już rekordu w bazie
                        taskCompletionSource.setResult(null);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    taskCompletionSource.setException(databaseError.toException());
                }
            });
        }
        return taskCompletionSource.getTask();
    }


}
