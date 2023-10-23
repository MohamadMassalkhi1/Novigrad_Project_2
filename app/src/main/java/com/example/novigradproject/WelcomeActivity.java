package com.example.novigradproject;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Get the first name and role information passed from the previous activity
        String firstName = getIntent().getStringExtra("firstName");
        String role = getIntent().getStringExtra("role");

        // Display a welcome message with the user's first name and role
        TextView welcomeMessage = findViewById(R.id.welcomeTextView);
        welcomeMessage.setText("Welcome, " + firstName + "! You are logged in as a " + role + ".");
    }
}
