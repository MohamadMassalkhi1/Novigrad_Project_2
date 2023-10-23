package com.example.novigradproject;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Client extends AppCompatActivity {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String address;

    public Client() {
        // Default constructor required for Firebase
    }

    public Client(String username, String email, String firstName, String lastName, String address) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_dashboard);

        String username = getIntent().getStringExtra("username");

        // Display the user's first and last name on the dashboard
        TextView NameTextView = findViewById(R.id.NameTextView);
        NameTextView.setText("Bienvenue, " + username);
    }

}
