package com.example.eams;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.util.Patterns;

public class OrganizerRegistrationActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText phoneNumberEditText;
    private EditText addressEditText;
    private EditText organizationNameEditText;
    private Button registerButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_registration);

        // Link the views from XML
        firstNameEditText = findViewById(R.id.firstName);
        lastNameEditText = findViewById(R.id.lastName);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        phoneNumberEditText = findViewById(R.id.phoneNumber);
        addressEditText = findViewById(R.id.address);
        organizationNameEditText = findViewById(R.id.organizationName);
        registerButton = findViewById(R.id.registerButton);

        databaseReference = FirebaseDatabase.getInstance().getReference("organizers");

        // Set the register button action
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerOrganizer();
            }
        });
    }

    private void registerOrganizer() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String organizationName = organizationNameEditText.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(address) ||
                TextUtils.isEmpty(organizationName)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(address).matches()) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (phoneNumber.length() !=10 || !Patterns.PHONE.matcher(phoneNumber).matches()) {
            Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            // Create Organizer object
            Organizer organizer = new Organizer(firstName, lastName, email, password, phoneNumber, address, organizationName);
            databaseReference.push().setValue(organizer);
            Toast.makeText(this, "Organizer Registered Successfully!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(OrganizerRegistrationActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
