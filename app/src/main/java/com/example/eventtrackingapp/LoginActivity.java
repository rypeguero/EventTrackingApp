package com.example.eventtrackingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    private static final int REQ_SMS = 1001;

    DBHelper db;
    TextInputEditText editTextUsername, editTextPassword;
    Button buttonLogin, buttonCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DBHelper(this);

        editTextUsername    = findViewById(R.id.editTextUsername);
        editTextPassword    = findViewById(R.id.editTextPassword);
        buttonLogin         = findViewById(R.id.buttonLogin);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        buttonLogin.setOnClickListener(v -> {
            String u = editTextUsername.getText() == null ? "" : editTextUsername.getText().toString().trim();
            String p = editTextPassword.getText() == null ? "" : editTextPassword.getText().toString().trim();

            long userId = db.loginUser(u, p);
            if (userId > 0) {
                getSharedPreferences("app", MODE_PRIVATE).edit()
                        .putLong("userId", userId)
                        .putString("username", u)
                        .apply();

                maybeAskForSmsPermission(); // <--- prompt right after successful login

                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });

        buttonCreateAccount.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void maybeAskForSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, REQ_SMS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_SMS) {
            boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            Toast.makeText(this, granted ? "SMS permission granted" : "SMS permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}
