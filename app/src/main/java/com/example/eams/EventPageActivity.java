package com.example.eams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class EventPageActivity extends AppCompatActivity {

    private String str, title, find;
    private DatabaseReference eventReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        Intent receive = getIntent();
        str = receive.getStringExtra("EventInfo");
        find = "Date:";
        int i = str.indexOf(find);
        if(i>0) {
            title = str.substring(7, i);
            title = title.trim();
        }

    }
    public void delete(View view) {
        eventReference = FirebaseDatabase.getInstance().getReference("events");
        eventReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d : snapshot.getChildren()){
                    EventInfo event = d.getValue(EventInfo.class);
                    if(event.getTitle().equals(title)){
                        d.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(EventPageActivity.this, "delete successful", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(EventPageActivity.this, "delete unsuccessful", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
