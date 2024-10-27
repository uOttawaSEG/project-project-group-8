package com.example.eams;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdministratorActivity extends AppCompatActivity {

    private RecyclerView requestsRecyclerView;
    private DatabaseReference  databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administrator);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize the RecyclerView and Firebase reference
        requestsRecyclerView = findViewById(R.id.requestsRecyclerView);
        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference("organizers");

        // Fetch registration requests and set up the RecyclerView adapter
        fetchRegistrationRequests();

    }

    // Method to fetch registration requests
    private void fetchRegistrationRequests() {
        // TODO
    }
    // Method to approve a request
    public void approveRequest(String userId) {
        databaseReference.child(userId).child("status").setValue("approved");
        Toast.makeText(this, "Request approved for user: " + userId, Toast.LENGTH_SHORT).show();
    }

    // Method to reject a request
    public void rejectRequest(String userId) {
        databaseReference.child(userId).child("status").setValue("rejected");
        Toast.makeText(this, "Request rejected for user: " + userId, Toast.LENGTH_SHORT).show();
    }
}