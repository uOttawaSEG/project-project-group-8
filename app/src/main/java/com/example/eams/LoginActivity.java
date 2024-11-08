package com.example.eams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private DatabaseReference attendeeReference;
    private DatabaseReference organizerReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        attendeeReference = FirebaseDatabase.getInstance().getReference("attendees");
        organizerReference = FirebaseDatabase.getInstance().getReference("organizers");

        Button submitButton = findViewById(R.id.submitId);
        submitButton.setOnClickListener(this::loginAttempt);
    }

    public void loginAttempt(View view) {
        EditText usernameEditText = findViewById(R.id.usernameId);
        EditText passwordEditText = findViewById(R.id.passwordId);

        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Admin login
        Administrator admin = new Administrator();
        if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
            Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, AdministratorActivity.class);
            intent.putExtra("role", "Administrator");
            startActivity(intent);
            return;
        }

        // Attendee login
        attendeeReference.orderByChild("email").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot attendeeSnapshot : dataSnapshot.getChildren()) {
                                Attendee attendee = attendeeSnapshot.getValue(Attendee.class);
                                if (attendee != null && attendee.getPassword().equals(password)) {
                                    // Check the status before allowing login
                                    handleUserStatus(attendee.getStatus(), "Attendee");
                                    return;
                                    //Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                    //Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                                    //intent.putExtra("role", "Attendee");
                                    //startActivity(intent);

                                }
                            }
                            Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                        } else {

                            checkOrganizers(username, password);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(LoginActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkOrganizers(String username, String password) {

        // Organizer login
        organizerReference.orderByChild("email").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot organizerSnapshot : dataSnapshot.getChildren()) {
                                Organizer organizer = organizerSnapshot.getValue(Organizer.class);
                                if (organizer != null && organizer.getPassword().equals(password)) {
                                    // Check the status before allowing login
                                    handleUserStatus(organizer.getStatus(), "Organizer");
                                    return;
                                    //Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                    //Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                                    //intent.putExtra("role", "Organizer");
                                    //startActivity(intent);

                                }
                            }
                            Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "No user found with this email", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(LoginActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void handleUserStatus(String status, String role) {
        switch (status) {
            case "approved":
                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                if(role.equals("Organizer")) {
                    Intent intent = new Intent(LoginActivity.this, OrganizerActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                    intent.putExtra("role", role);
                    startActivity(intent);
                }
                break;
            case "pending":
                Toast.makeText(LoginActivity.this, "Your registration is still pending approval.", Toast.LENGTH_SHORT).show();
                break;
            case "rejected":
                Toast.makeText(LoginActivity.this, "Your registration has been rejected. Contact the Admin.(Phone:0123456789)", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(LoginActivity.this, "Error:Invalid status", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
