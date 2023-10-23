package com.example.novigradproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton, registerButton;
    private Spinner roleSpinner;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        roleSpinner = findViewById(R.id.roleSpinner);

        // Handle the login button click event
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user input data
                final String email = usernameEditText.getText().toString().trim();
                final String password = passwordEditText.getText().toString().trim();
                final String selectedRole = roleSpinner.getSelectedItem().toString();

                // Trim the email address to get the username
                String username;

                if (email.contains("@")) {
                    username = email.split("@")[0];
                } else {
                    username = email;
                }

                // Validate user input
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    // Handle validation error
                    Toast.makeText(LoginActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                } else {
                    if (isAdminUser(username, password) && selectedRole.equals("Admin (cannot create a new Admin)")) {
                        // Directly navigate to the Admin activity
                        Intent intent = new Intent(LoginActivity.this, Admin.class);
                        startActivity(intent);
                    } else {
                        // Authenticate with Firebase
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();

                                            if (selectedRole.equals("Client")) {
                                                Intent intent = new Intent(LoginActivity.this, Client.class);
                                                intent.putExtra("username", username); // Pass the username
                                                startActivity(intent);
                                            } else if (selectedRole.equals("Employee")) {
                                                Intent intent = new Intent(LoginActivity.this, Employee.class);
                                                intent.putExtra("username", username); // Pass the username
                                                startActivity(intent);
                                            } else {
                                                // Handle other roles or show an error message
                                                Toast.makeText(LoginActivity.this, "Login failed. Invalid role selected.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            // Login failed
                                            Toast.makeText(LoginActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }
        });

        // Handle the register button click event
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isAdminUser(String username, String password) {
        // Check if the user is an admin based on username and password
        // Replace these with your actual admin credentials
        String adminUsername = "admin";
        String adminPassword = "admin123";

        return username.equals(adminUsername) && password.equals(adminPassword);
    }
}
