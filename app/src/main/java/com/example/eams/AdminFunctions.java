package com.example.eams;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminFunctions extends AppCompatActivity {
    private DatabaseReference attendeeReference;
    private DatabaseReference organizerReference;
    private String userType;
    private String r;
    private String email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_functions);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent receive = getIntent();

        attendeeReference = FirebaseDatabase.getInstance().getReference("attendees");
        organizerReference = FirebaseDatabase.getInstance().getReference("organizers");

        userType = receive.getStringExtra("UserType");

        r = receive.getStringExtra("UserInfo");

        TextView userInfo = findViewById(R.id.welcomeTextView);
        userInfo.setText(r);

        String emailRegex = "([\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6})";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(r);

        if(matcher.find()) {

            email = matcher.group().trim();

        }
    }

    public void approve(View view) {

        if(userType.equals("attendees")) {

            Query query = attendeeReference.orderByChild("email").equalTo(email);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        for (DataSnapshot attendeeSnapshot : dataSnapshot.getChildren()) {

                            String attendeeId = attendeeSnapshot.getKey();

                            attendeeSnapshot.getRef().child("status").setValue("approved")
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(AdminFunctions.this, "Request approved", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(AdminFunctions.this, "Request failed to approve", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(AdminFunctions.this, "No attendee found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(AdminFunctions.this, "Query cancelled", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{

            Query query = organizerReference.orderByChild("email").equalTo(email);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        for (DataSnapshot attendeeSnapshot : dataSnapshot.getChildren()) {

                            String attendeeId = attendeeSnapshot.getKey();

                            attendeeSnapshot.getRef().child("status").setValue("approved")
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(AdminFunctions.this, "Request approved", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(AdminFunctions.this, "Request failed to approve", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(AdminFunctions.this, "No attendee found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(AdminFunctions.this, "Query cancelled", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    public void reject(View view) {

        if(userType.equals("attendees")) {

            Query query = attendeeReference.orderByChild("email").equalTo(email);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        for (DataSnapshot attendeeSnapshot : dataSnapshot.getChildren()) {

                            String attendeeId = attendeeSnapshot.getKey();

                            attendeeSnapshot.getRef().child("status").setValue("rejected")
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(AdminFunctions.this, "Request rejected", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(AdminFunctions.this, "Request failed to reject", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(AdminFunctions.this, "No attendee found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(AdminFunctions.this, "Query cancelled", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{

            Query query = organizerReference.orderByChild("email").equalTo(email);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        for (DataSnapshot attendeeSnapshot : dataSnapshot.getChildren()) {

                            String attendeeId = attendeeSnapshot.getKey();

                            attendeeSnapshot.getRef().child("status").setValue("rejected")
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(AdminFunctions.this, "Request rejected", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(AdminFunctions.this, "Request failed to reject", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(AdminFunctions.this, "No attendee found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(AdminFunctions.this, "Query cancelled", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

}
