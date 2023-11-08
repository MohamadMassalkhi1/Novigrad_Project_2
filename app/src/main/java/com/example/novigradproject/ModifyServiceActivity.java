package com.example.novigradproject;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ModifyServiceActivity extends AppCompatActivity {
    private Spinner serviceSpinner;
    private EditText serviceNameEditText;
    private EditText formInformationEditText;
    private EditText documentInformationEditText;
    private Button saveServiceButton;
    private List<ServiceModel> services; // List of ServiceModel objects

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference servicesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_services);

        serviceSpinner = findViewById(R.id.spinnerServices);
        serviceNameEditText = findViewById(R.id.editTextServiceName);
        formInformationEditText = findViewById(R.id.editTextFormInformation);
        documentInformationEditText = findViewById(R.id.editTextDocumentInformation);
        saveServiceButton = findViewById(R.id.btnSaveService);

        services = new ArrayList<>(); // Initialize the list

        firebaseDatabase = FirebaseDatabase.getInstance();
        servicesRef = firebaseDatabase.getReference("services");

        // Fetch existing services from Firebase
        fetchDataFromFirebase();

        // Setup an AdapterView.OnItemSelectedListener for the spinner
        serviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle when a service is selected from the spinner
                populateFieldsWithSelectedService(services.get(position));
                serviceNameEditText.setText(services.get(position).getServiceName()); // Ensure the name is displayed in the correct case
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle when nothing is selected
                clearFields();
            }
        });

        saveServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected service from the spinner
                int selectedPosition = serviceSpinner.getSelectedItemPosition();
                if (selectedPosition != AdapterView.INVALID_POSITION) {
                    ServiceModel selectedService = services.get(selectedPosition);
                    String originalServiceName = selectedService.getServiceName(); // Get the original service name

                    // Update the selected service with new information
                    selectedService.setFormInformation(formInformationEditText.getText().toString());
                    selectedService.setDocumentInformation(documentInformationEditText.getText().toString());

                    // Save the updated service in Firebase with the original service name
                    updateServiceInFirebase(selectedService, originalServiceName); // Use the correct case
                }
            }
        });

    }

    private void fetchDataFromFirebase() {
        servicesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                services.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ServiceModel service = snapshot.getValue(ServiceModel.class);
                    if (service != null) {
                        services.add(service);
                    }
                }
                updateServiceSpinner();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ModifyServiceActivity.this, "Failed to fetch services from Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateServiceSpinner() {
        String[] serviceNames = new String[services.size()];
        for (int i = 0; i < services.size(); i++) {
            serviceNames[i] = services.get(i).getServiceName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, serviceNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceSpinner.setAdapter(adapter);
    }

    private void populateFieldsWithSelectedService(ServiceModel selectedService) {
        serviceNameEditText.setText(selectedService.getServiceName()); // Display the name in the correct case
        formInformationEditText.setText(selectedService.getFormInformation());
        documentInformationEditText.setText(selectedService.getDocumentInformation());
    }

    private void clearFields() {
        serviceNameEditText.setText(""); // Clear the name
        formInformationEditText.setText("");
        documentInformationEditText.setText("");
    }

    private void updateServiceInFirebase(ServiceModel service, String originalServiceName) {
        DatabaseReference serviceReference = servicesRef.child(originalServiceName);
        serviceReference.setValue(service, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    // Successfully updated the service
                    Toast.makeText(ModifyServiceActivity.this, "Service updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle the error case
                    Toast.makeText(ModifyServiceActivity.this, "Failed to update service", Toast.LENGTH_SHORT).show();
                }
            }
        });


}
}
