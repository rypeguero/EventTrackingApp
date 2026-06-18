package com.example.eventtrackingapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SettingsActivity extends AppCompatActivity {
    private static final int REQ_SMS = 2001;

    Switch switchEventReminders;
    TextView tvSmsPermissionStatus;
    Button btnRequestSmsPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchEventReminders    = findViewById(R.id.switchEventReminders);
        tvSmsPermissionStatus   = findViewById(R.id.tvSmsPermissionStatus);
        btnRequestSmsPermission = findViewById(R.id.btnRequestSmsPermission);

        btnRequestSmsPermission.setOnClickListener(v -> requestSmsPermission());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSmsStatusLabel();
    }

    private void updateSmsStatusLabel() {
        boolean granted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
        tvSmsPermissionStatus.setText(granted ? "SMS Permission: Granted" : "SMS Permission: Not granted");
        switchEventReminders.setEnabled(granted);
        if (!granted) switchEventReminders.setChecked(false);
    }

    private void requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, REQ_SMS);
        } else {
            Toast.makeText(this, "Already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] perms, int[] results) {
        super.onRequestPermissionsResult(requestCode, perms, results);
        if (requestCode == REQ_SMS) {
            boolean granted = results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED;
            Toast.makeText(this, granted ? "SMS permission granted" : "SMS permission denied", Toast.LENGTH_SHORT).show();
            updateSmsStatusLabel();
        }
    }
}
