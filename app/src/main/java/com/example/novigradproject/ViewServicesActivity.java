package com.example.novigradproject;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewServicesActivity extends AppCompatActivity {

    private DatabaseReference databaseReference; // Firebase database reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_services);

        ListView listViewServices = findViewById(R.id.listViewServices);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("services");

        // Fetch existing services from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ServiceModel> existingServices = new ArrayList<>();
                for (DataSnapshot serviceSnapshot : dataSnapshot.getChildren()) {
                    ServiceModel service = serviceSnapshot.getValue(ServiceModel.class);
                    existingServices.add(service);
                }

                List<String> serviceNames = new ArrayList<>();
                for (ServiceModel service : existingServices) {
                    serviceNames.add(service.getServiceName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewServicesActivity.this, android.R.layout.simple_list_item_1, serviceNames);
                listViewServices.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors here
            }
        });
    }
}
