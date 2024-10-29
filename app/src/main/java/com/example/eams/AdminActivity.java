package com.example.eams;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    private DatabaseReference dbRef;
    private ListView pendingRequestsListView;
    private ArrayAdapter<String> requestsAdapter;
    private ArrayList<String> requestList;
    private ArrayList<RegistrationRequest> pendingRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        // db reference
        dbRef = FirebaseDatabase.getInstance().getReference("registrationRequests");

        //ListView and adapter for displaying requests
        pendingRequestsListView = findViewById(R.id.pendingRequestsListView);
        requestList = new ArrayList<>();
        pendingRequests = new ArrayList<>();
        requestsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requestList);
        pendingRequestsListView.setAdapter(requestsAdapter);

        loadPendingRequests();

        pendingRequestsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Show options to approve or reject
                RegistrationRequest selectedRequest = pendingRequests.get(position);
                showApprovalOptions(selectedRequest);
            }
        });
    }

    private void loadPendingRequests() {
        dbRef.orderByChild("status").equalTo("pending").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requestList.clear();
                pendingRequests.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RegistrationRequest request = snapshot.getValue(RegistrationRequest.class);
                    if (request != null) {
                        pendingRequests.add(request);
                        requestList.add(request.getFirstName() + " " + request.getLastName() + " (" + request.getEmail() + ")");
                    }
                }
                requestsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminActivity.this, "Failed to load requests", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showApprovalOptions(RegistrationRequest request) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Approve or Reject Request")
                .setMessage("Approve or reject registration for " + request.getFirstName() + " " + request.getLastName() + "?")
                .setPositiveButton("Approve", (dialog, which) -> updateRequestStatus(request.getEmail(), "approved"))
                .setNegativeButton("Reject", (dialog, which) -> updateRequestStatus(request.getEmail(), "rejected"))
                .show();
    }

    private void updateRequestStatus(String email, String status) {
        dbRef.child(email.replace(".", ",")).child("status").setValue(status).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Request " + status, Toast.LENGTH_SHORT).show();
                loadPendingRequests(); // Refresh the list after approval/rejection
            } else {
                Toast.makeText(this, "Failed to update request", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

