package com.example.eams;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class InboxActivity extends AppCompatActivity {

    private TextView registrationRequestId;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        registrationRequestId = findViewById(R.id.registrationRequestId);
        databaseReference = FirebaseDatabase.getInstance().getReference("registration_requests");

        fetchRegistrationRequests();
    }

    private void fetchRegistrationRequests() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StringBuilder requests = new StringBuilder();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RegistrationRequest request = snapshot.getValue(RegistrationRequest.class);
                    if (request != null) {
                        requests.append("Name: ").append(request.getFirstName()).append(" ").append(request.getLastName()).append("\n")
                                .append("Email: ").append(request.getEmail()).append("\n")
                                .append("Organization: ").append(request.getOrganizationName()).append("\n\n");
                    }
                }

                if (requests.length() > 0) {
                    registrationRequestId.setText(requests.toString());
                } else {
                    registrationRequestId.setText("No registration requests available.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(InboxActivity.this, "Error fetching requests: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
