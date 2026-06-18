package com.example.eventtrackingapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DBHelper db;
    long userId;
    RecyclerView recyclerViewEvents;
    EventAdapter adapter;
    FloatingActionButton fabAddEvent;

    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);

        // 1) Check login FIRST — if not logged in, go to Login and exit immediately
        userId = getSharedPreferences("app", MODE_PRIVATE).getLong("userId", -1);
        if (userId <= 0){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // 2) Logged in → inflate the event list screen
        setContentView(R.layout.activity_event_list);

        db = new DBHelper(this);

        recyclerViewEvents = findViewById(R.id.recyclerViewEvents);
        recyclerViewEvents.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new EventAdapter(eventId -> {
            Intent i = new Intent(this, AddEditEventActivity.class);
            i.putExtra("eventId", eventId); // keep key consistent with AddEditEventActivity
            startActivity(i);
        });
        recyclerViewEvents.setAdapter(adapter);

        fabAddEvent = findViewById(R.id.fabAddEvent);
        fabAddEvent.setOnClickListener(v -> {
            Intent i = new Intent(this, AddEditEventActivity.class);
            i.putExtra("eventId", -1L);
            startActivity(i);
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (getSharedPreferences("app", MODE_PRIVATE).getLong("userId", -1) <= 0){
            // If user logged out somehow, bounce to login
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        load();
    }

    void load(){
        List<Event> list = new ArrayList<>();
        Cursor c = db.getEvents(userId);
        try {
            while (c.moveToNext()){
                list.add(new Event(
                        c.getLong(c.getColumnIndexOrThrow("id")),
                        c.getString(c.getColumnIndexOrThrow("title")),
                        c.getString(c.getColumnIndexOrThrow("date")),
                        c.getString(c.getColumnIndexOrThrow("time")),
                        c.getString(c.getColumnIndexOrThrow("location")),
                        c.getString(c.getColumnIndexOrThrow("notes")),
                        c.getInt(c.getColumnIndexOrThrow("alert_enabled"))==1,
                        c.getInt(c.getColumnIndexOrThrow("alert_minutes_before"))
                ));
            }
        } finally { c.close(); }
        adapter.submit(list);
    }
}
