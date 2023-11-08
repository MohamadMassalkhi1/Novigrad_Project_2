package com.example.novigradproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class AddServiceActivity extends AppCompatActivity {
    private EditText serviceNameEditText;
    private EditText formInformationEditText;
    private EditText documentInformationEditText;
    private Button addServiceButton;

    // Firebase database reference
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_services);

        serviceNameEditText = findViewById(R.id.editTextServiceName);
        formInformationEditText = findViewById(R.id.editTextFormInformation);
        documentInformationEditText = findViewById(R.id.editTextDocumentInformation);
        addServiceButton = findViewById(R.id.btnAddAService);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("services");

        addServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String serviceName = serviceNameEditText.getText().toString();

                // Check if the service name already exists
                databaseReference.child(serviceName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Service with the same name already exists
                            Toast.makeText(AddServiceActivity.this, "Service with the same name already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            // Service name is unique, add the new service
                            String formInformation = formInformationEditText.getText().toString();
                            String documentInformation = documentInformationEditText.getText().toString();

                            // Perform input validation
                            if (serviceName.isEmpty()) {
                                Toast.makeText(AddServiceActivity.this, "Service name is required", Toast.LENGTH_SHORT).show();
                            } else {
                                // Generate a unique key for the service using push()
                                DatabaseReference serviceRef = databaseReference.child(serviceName);

                                // Set the values for the service
                                serviceRef.child("serviceName").setValue(serviceName);
                                serviceRef.child("formInformation").setValue(formInformation);
                                serviceRef.child("documentInformation").setValue(documentInformation);

                                // Show a success message and navigate back
                                Toast.makeText(AddServiceActivity.this, "Service added successfully", Toast.LENGTH_SHORT).show();
                                finish(); // Finish the activity and return to the previous screen
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle the error case
                        Toast.makeText(AddServiceActivity.this, "Error checking for service name", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
