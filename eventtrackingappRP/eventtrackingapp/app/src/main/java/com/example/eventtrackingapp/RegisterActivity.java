package com.example.eventtrackingapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    private static final int REQ_SMS_AFTER_REGISTER = 2010;

    DBHelper db;
    TextInputEditText editTextUsername, editTextPassword;
    Button buttonLogin, buttonCreateAccount; // reusing IDs from activity_login.xml

    // When true, we’ll finish() after the permission dialog result
    private boolean finishAfterPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Reuse the login layout (has username/password + two buttons)
        setContentView(R.layout.activity_login);

        db = new DBHelper(this);

        editTextUsername    = findViewById(R.id.editTextUsername);
        editTextPassword    = findViewById(R.id.editTextPassword);
        buttonLogin         = findViewById(R.id.buttonLogin);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        // Repurpose buttons for register flow
        buttonLogin.setText("Create Account");
        buttonCreateAccount.setText("Cancel");

        buttonLogin.setOnClickListener(v -> {
            String u = editTextUsername.getText() == null ? "" : editTextUsername.getText().toString().trim();
            String p = editTextPassword.getText() == null ? "" : editTextPassword.getText().toString().trim();

            if (u.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            long id = db.registerUser(u, p);
            if (id > 0) {
                Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show();

                // Ask for SMS permission RIGHT NOW after creating the account
                maybeAskForSmsPermissionAfterRegister();
            } else {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            }
        });

        buttonCreateAccount.setOnClickListener(v -> finish());
    }

    private void maybeAskForSmsPermissionAfterRegister() {
        // If already granted, we’re done — return to Login
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "SMS permission already granted", Toast.LENGTH_SHORT).show();
            finish(); // back to Login
            return;
        }

        // Request permission, and finish the screen after user responds
        finishAfterPermission = true;
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.SEND_SMS},
                REQ_SMS_AFTER_REGISTER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_SMS_AFTER_REGISTER) {
            boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            Toast.makeText(this, granted ? "SMS permission granted" : "SMS permission denied", Toast.LENGTH_SHORT).show();

            if (finishAfterPermission) {
                // Return to Login after user’s decision
                finish();
            }
        }
    }
}
