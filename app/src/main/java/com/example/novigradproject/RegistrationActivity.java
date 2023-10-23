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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, confirmPasswordEditText, firstNameEditText, lastNameEditText, emailEditText;
    private Button registerButton;
    private Spinner roleSpinner;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        registerButton = findViewById(R.id.registerButton);
        roleSpinner = findViewById(R.id.roleSpinner);
        Button backToLoginButton = findViewById(R.id.backToLoginButton);

        // Handle the back to login button click event
        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to go back to the LoginActivity
                Intent loginIntent = new Intent(RegistrationActivity.this, LoginActivity.class);
                // Clear the backstack, so the user can't navigate back to the RegistrationActivity
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get user input data
                final String username = usernameEditText.getText().toString().trim();
                final String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();
                final String firstName = firstNameEditText.getText().toString().trim();
                final String lastName = lastNameEditText.getText().toString().trim();
                final String email = emailEditText.getText().toString().trim();

                // Validate user input
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    // Handle validation error
                    Toast.makeText(RegistrationActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    // Passwords don't match
                    Toast.makeText(RegistrationActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                } else {
                    // Registration logic
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Registration successful
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        // Get the selected role from the Spinner
                                        String selectedRole = roleSpinner.getSelectedItem().toString();

                                        // Save user data to the Firebase Realtime Database
                                        saveUserDataToDatabase(user.getUid(), username, firstName, lastName, email, selectedRole);

                                        // Redirect to the appropriate dashboard based on the role
                                        if (selectedRole.equals("Employee")) {
                                            Intent employeeIntent = new Intent(RegistrationActivity.this, Employee.class);
                                            startActivity(employeeIntent);
                                            finish(); // Finish the current activity to prevent the user from going back to the registration page
                                        } else if (selectedRole.equals("Client")) {
                                            Intent clientIntent = new Intent(RegistrationActivity.this, Client.class);
                                            startActivity(clientIntent);
                                            finish(); // Finish the current activity
                                        } else {
                                            Toast.makeText(RegistrationActivity.this, "Registration failed. Cannot create an Admin account.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        // Registration failed
                                        Toast.makeText(RegistrationActivity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void saveUserDataToDatabase(String userId, String username, String firstName, String lastName, String email, String role) {
        // Reference to the "users" node in the database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        if (role.equals("Employee")) {
            // Create a new Employee object
            Employee employee = new Employee(username, email, firstName, lastName, role);
            // Save user data under the user's ID
            usersRef.child(userId).setValue(employee);
        } else if (role.equals("Client")) {
            // Create a new Client object
            Client client = new Client(username, email, firstName, lastName, "Client Address");
            // Save user data under the user's ID
            usersRef.child(userId).setValue(client);
        }
    }
}
