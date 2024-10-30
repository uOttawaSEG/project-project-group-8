package com.example.eams;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;


public class AdminFunctions extends AppCompatActivity {
    private DatabaseReference attendeeReference;
    private DatabaseReference organizerReference;
    String UInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_functions);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent receive = getIntent();
        String r = receive.getStringExtra("UserInfo");

        attendeeReference = FirebaseDatabase.getInstance().getReference("attendees");;
        organizerReference = FirebaseDatabase.getInstance().getReference("organizers");



        TextView UserInfo = findViewById(R.id.welcomeTextView);
        UserInfo.setText(r);
    }
    public void approve(View view) {

        Intent intent = new Intent(AdminFunctions.this, InboxActivity.class);
        startActivity(intent);
    }

    public void reject(View view) {

        Intent intent = new Intent(AdminFunctions.this, InboxActivity.class);
        startActivity(intent);
    }
}