package com.example.eventtrackingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "eventapp.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Users table
        db.execSQL("CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE," +
                "password TEXT," +
                "phone TEXT)");

        // Events table
        // NOTE: we use 'description' as a column name to match your AddEditEventActivity,
        // but you can map it to your Event.notes field if desired.
        db.execSQL("CREATE TABLE IF NOT EXISTS events (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "title TEXT NOT NULL," +
                "date TEXT," +
                "time TEXT," +
                "location TEXT," +
                "description TEXT," +
                "alert_enabled INTEGER DEFAULT 0," +
                "alert_minutes_before INTEGER DEFAULT 30," +
                "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS events");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    // -------------------------
    // User auth helpers
    // -------------------------
    public long registerUser(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("username", username);
        v.put("password", password);
        return db.insert("users", null, v);
    }

    public long loginUser(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT id FROM users WHERE username=? AND password=?",
                new String[]{username, password});
        try {
            if (c.moveToFirst()) return c.getLong(0);
            return -1;
        } finally { c.close(); }
    }

    // -------------------------
    // Events CRUD used by Activities
    // -------------------------

    // CREATE
    public long insertEvent(long userId, String title, String date, String location, String description) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("user_id", userId);
        v.put("title", title);
        v.put("date", date);
        v.put("time", (String) null); // not provided in AddEditEventActivity UI yet
        v.put("location", location);
        v.put("description", description);
        v.put("alert_enabled", 0);
        v.put("alert_minutes_before", 30);
        return db.insert("events", null, v);
    }

    // READ (single)
    public Event getEventById(long eventId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT id, title, date, time, location, description, alert_enabled, alert_minutes_before " +
                        "FROM events WHERE id=?",
                new String[]{String.valueOf(eventId)});
        try {
            if (c.moveToFirst()) {
                long id            = c.getLong(c.getColumnIndexOrThrow("id"));
                String title       = c.getString(c.getColumnIndexOrThrow("title"));
                String date        = c.getString(c.getColumnIndexOrThrow("date"));
                String time        = c.getString(c.getColumnIndexOrThrow("time"));
                String location    = c.getString(c.getColumnIndexOrThrow("location"));
                String description = c.getString(c.getColumnIndexOrThrow("description"));
                boolean alert      = c.getInt(c.getColumnIndexOrThrow("alert_enabled")) == 1;
                int minutesBefore  = c.getInt(c.getColumnIndexOrThrow("alert_minutes_before"));
                // Map 'description' column to Event.notes field in your Event class
                return new Event(id, title, date, time, location, description, alert, minutesBefore);
            }
            return null;
        } finally { c.close(); }
    }

    // READ (list for a user) – used by MainActivity to populate the grid
    public Cursor getEvents(long userId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
                "SELECT id, title, date, time, location, description AS notes, alert_enabled, alert_minutes_before " +
                        "FROM events WHERE user_id=? ORDER BY date, time",
                new String[]{String.valueOf(userId)});

    }

    // UPDATE
    public int updateEvent(long eventId, String title, String date, String location, String description) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("title", title);
        v.put("date", date);
        v.put("location", location);
        v.put("description", description);
        // time/alerts not edited in current UI; keep existing values
        return db.update("events", v, "id=?", new String[]{String.valueOf(eventId)});
    }

    // DELETE (if you add a delete button later)
    public int deleteEvent(long eventId) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("events", "id=?", new String[]{String.valueOf(eventId)});
    }
}
