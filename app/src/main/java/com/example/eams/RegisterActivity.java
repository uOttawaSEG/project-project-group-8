package com.example.eams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseReference = FirebaseDatabase.getInstance().getReference("registration_requests");

        Button attendeeButton = findViewById(R.id.attendeeId);
        Button organizerButton = findViewById(R.id.OrganizerId);

        attendeeButton.setOnClickListener(v -> startActivity(new Intent(this, AttendeeActivity.class)));
        organizerButton.setOnClickListener(v -> startActivity(new Intent(this, OrganizerRegistrationActivity.class)));
    }

    public void submitAttendee(View view) {

        Toast.makeText(this, "Submitting attendee registration", Toast.LENGTH_SHORT).show();
    }

    public void submitOrganizer(View view) {
        EditText firstName = findViewById(R.id.firstName);
        EditText lastName = findViewById(R.id.lastName);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        EditText phoneNumber = findViewById(R.id.phoneNumber);
        EditText address = findViewById(R.id.address);
        EditText organizationName = findViewById(R.id.organizationName);

        String firstNameValue = firstName.getText().toString().trim();
        String lastNameValue = lastName.getText().toString().trim();
        String emailValue = email.getText().toString().trim();
        String passwordValue = password.getText().toString().trim();
        String phoneNumberValue = phoneNumber.getText().toString().trim();
        String addressValue = address.getText().toString().trim();
        String organizationNameValue = organizationName.getText().toString().trim();

        if (firstNameValue.isEmpty() || lastNameValue.isEmpty() || emailValue.isEmpty() || passwordValue.isEmpty() ||
                phoneNumberValue.isEmpty() || addressValue.isEmpty() || organizationNameValue.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String requestId = databaseReference.push().getKey();
        RegistrationRequest request = new RegistrationRequest(firstNameValue, lastNameValue, emailValue, passwordValue,
                phoneNumberValue, addressValue, organizationNameValue);

        databaseReference.child(requestId).setValue(request).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(RegisterActivity.this, "Registration request submitted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterActivity.this, "Failed to submit request", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
