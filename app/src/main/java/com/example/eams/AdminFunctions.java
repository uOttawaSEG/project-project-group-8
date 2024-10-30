package com.example.eams;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminFunctions extends AppCompatActivity {
    private DatabaseReference attendeeReference;
    private DatabaseReference organizerReference;
    private String currentRequestId;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_functions);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent receive = getIntent();
        String r = receive.getStringExtra("UserInfo");
        currentRequestId = extractRequestIdFromUserInfo(r);

        attendeeReference = FirebaseDatabase.getInstance().getReference("attendees");
        organizerReference = FirebaseDatabase.getInstance().getReference("organizers");

        userType = receive.getStringExtra("UserType");

        TextView userInfo = findViewById(R.id.welcomeTextView);
        userInfo.setText(r);
    }

    private String extractRequestIdFromUserInfo(String userInfo){
        return userInfo;
    }

    public void approve(View view) {
        approveRequest(userType);
    }

    public void reject(View view) {
        rejectRequest(userType);
    }

    private void approveRequest(String userType) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(userType).child(currentRequestId);

        userRef.child("status").setValue("Approved").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Request approved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Approval failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
            finish();
        });
    }

    private void rejectRequest(String userType) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(userType).child(currentRequestId);

        userRef.child("status").setValue("Rejected").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Request rejected", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Rejection failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
            finish();
        });
    }


    private void removeFromPendingRequests(String userType) {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference(userType).child(currentRequestId);
        userReference.removeValue().addOnCompleteListener(removeTask -> {
            if (!removeTask.isSuccessful()) {
                Toast.makeText(this, "Error removing from pending requests", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
