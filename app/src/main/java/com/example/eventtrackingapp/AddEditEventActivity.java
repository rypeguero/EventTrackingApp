package com.example.eventtrackingapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditEventActivity extends AppCompatActivity {
    DBHelper db;
    EditText editTitle, editDate, editLocation, editDescription;
    Button buttonSave, buttonCancel, buttonDeleteEvent;
    long eventId = -1;
    long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_event);

        db = new DBHelper(this);

        editTitle       = findViewById(R.id.editEventTitle);
        editDate        = findViewById(R.id.editEventDate);
        editLocation    = findViewById(R.id.editEventLocation);
        editDescription = findViewById(R.id.editEventDescription);
        buttonSave      = findViewById(R.id.buttonSaveEvent);
        buttonCancel    = findViewById(R.id.buttonCancelEvent);
        buttonDeleteEvent = findViewById(R.id.buttonDeleteEvent);

        userId = getSharedPreferences("app", MODE_PRIVATE).getLong("userId", -1);

        // If editing, load existing values
        eventId = getIntent().getLongExtra("eventId", -1);
        if (eventId > 0) {
            Event existing = db.getEventById(eventId);
            if (existing != null) {
                editTitle.setText(existing.getTitle());
                editDate.setText(existing.getDate());
                editLocation.setText(existing.getLocation());
                editDescription.setText(existing.getDescription());
            }
            buttonDeleteEvent.setVisibility(View.VISIBLE);
        } else {
            buttonDeleteEvent.setVisibility(View.GONE);
        }

        buttonSave.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String date  = editDate.getText().toString().trim();
            String loc   = editLocation.getText().toString().trim();
            String desc  = editDescription.getText().toString().trim();

            if (title.isEmpty()) {
                Toast.makeText(this, "Title required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (eventId > 0) {
                // Update existing
                db.updateEvent(eventId, title, date, loc, desc);
                Toast.makeText(this, "Event updated", Toast.LENGTH_SHORT).show();
            } else {
                // Insert new
                db.insertEvent(userId, title, date, loc, desc);
                Toast.makeText(this, "Event added", Toast.LENGTH_SHORT).show();
            }
            finish();
        });

        buttonCancel.setOnClickListener(v -> finish());

        buttonDeleteEvent.setOnClickListener(v -> {
            if (eventId > 0) {
                new AlertDialog.Builder(this)
                        .setTitle("Delete Event")
                        .setMessage("Are you sure you want to delete this event?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            int rows = db.deleteEvent(eventId);
                            if (rows > 0) {
                                Toast.makeText(this, "Event deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
                            }
                            finish();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
    }
}
