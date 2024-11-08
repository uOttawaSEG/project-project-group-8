package com.example.eams;

import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateEventActivity extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText, addressEditText;
    private TextView dateTextView, startTimeTextView, endTimeTextView;
    private CheckBox autoApproveCheckBox;
    private Button createEventButton;
    private Calendar selectedDate, currentDate, startTime, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        addressEditText = findViewById(R.id.addressEditText);
        dateTextView = findViewById(R.id.dateTextView);
        startTimeTextView = findViewById(R.id.startTimeTextView);
        endTimeTextView = findViewById(R.id.endTimeTextView);
        autoApproveCheckBox = findViewById(R.id.autoApproveCheckBox);
        createEventButton = findViewById(R.id.createEventButton);
        currentDate = Calendar.getInstance();
        dateTextView.setOnClickListener(v -> showDatePicker());
        startTimeTextView.setOnClickListener(v -> showTimePicker(startTimeTextView, true));
        endTimeTextView.setOnClickListener(v -> showTimePicker(endTimeTextView, false));
        createEventButton.setOnClickListener(v -> createEvent());
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);

                    if (selectedDate.before(currentDate)) {
                        Toast.makeText(this, "Please select a future date", Toast.LENGTH_SHORT).show();
                    } else {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        dateTextView.setText(sdf.format(selectedDate.getTime()));
                    }
                },
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
        datePickerDialog.show();
    }

    private void showTimePicker(TextView timeTextView, boolean isStartTime) {
        int hour = currentDate.get(Calendar.HOUR_OF_DAY);
        int minute = currentDate.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, selectedHour, selectedMinute) -> {
                    selectedMinute = (selectedMinute < 30) ? 0 : 30;

                    Calendar time = Calendar.getInstance();
                    time.set(Calendar.HOUR_OF_DAY, selectedHour);
                    time.set(Calendar.MINUTE, selectedMinute);

                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    timeTextView.setText(sdf.format(time.getTime()));

                    if (isStartTime) {
                        startTime = time;
                    } else {
                        endTime = time;
                    }
                },
                hour,
                minute,
                true
        );
        timePickerDialog.show();
    }

    private void createEvent() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        boolean autoApprove = autoApproveCheckBox.isChecked();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) ||
                TextUtils.isEmpty(address) || selectedDate == null ||
                startTime == null || endTime == null) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (startTime.after(endTime)) {
            Toast.makeText(this, "Start time must be before end time", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Event Created Successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}