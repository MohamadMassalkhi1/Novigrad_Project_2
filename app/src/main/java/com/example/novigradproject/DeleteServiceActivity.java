package com.example.novigradproject;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class DeleteServiceActivity extends AppCompatActivity {
    private Spinner serviceSpinner;
    private Button confirmDeleteButton;
    private List<ServiceModel> services;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference servicesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_services);

        serviceSpinner = findViewById(R.id.serviceSpinner);
        confirmDeleteButton = findViewById(R.id.btnConfirmDelete);

        firebaseDatabase = FirebaseDatabase.getInstance();
        servicesRef = firebaseDatabase.getReference("services");

        // Fetch existing services from Firebase
        services = new ArrayList<>();
        fetchDataFromFirebase();

        confirmDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected service name from the Spinner
                String selectedServiceName = serviceSpinner.getSelectedItem().toString();

                // Implement logic to delete the service with the selected name
                if (deleteService(selectedServiceName)) {
                    Toast.makeText(DeleteServiceActivity.this, "Service deleted successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Finish the activity and return to the previous screen
                } else {
                    Toast.makeText(DeleteServiceActivity.this, "Failed to delete service", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Fetch existing services from Firebase
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

                // Update the Spinner with the service names
                updateServiceSpinner();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DeleteServiceActivity.this, "Failed to fetch services from Firebase", Toast.LENGTH_SHORT).show();
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

    private boolean deleteService(String serviceName) {
        // Implement the logic to delete a service from Firebase
        // Return true if the deletion is successful, false otherwise
        DatabaseReference serviceReference = servicesRef.child(serviceName);
        serviceReference.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if (error == null) {
                    // Successfully removed from Firebase, now remove from the local list
                    ServiceModel serviceToRemove = null;
                    for (ServiceModel service : services) {
                        if (service.getServiceName().equals(serviceName)) {
                            serviceToRemove = service;
                            break;
                        }
                    }
                    if (serviceToRemove != null) {
                        services.remove(serviceToRemove);
                        updateServiceSpinner(); // Update the spinner list
                    }
                }
            }
        });
        return true; // Assuming it's successful (you may want to add error handling)
    }
}
