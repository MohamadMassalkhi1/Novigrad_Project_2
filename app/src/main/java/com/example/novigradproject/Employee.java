package com.example.novigradproject;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Employee extends AppCompatActivity {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String role;

    public Employee() {
        // Default constructor required for Firebase
    }

    public Employee(String username, String email, String firstName, String lastName, String role) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    // Getter and setter methods for the above attributes
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_dashboard);

        String username = getIntent().getStringExtra("username");

        // Display the user's first and last name on the dashboard
        TextView nameTextView = findViewById(R.id.NameTextView);
        nameTextView.setText("Bienvenue, " + username);
    }
}
