package com.example.novigradproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Admin extends AppCompatActivity {

    private Button btnAddServices;
    private Button btnModifyServices;
    private Button btnViewServices;
    private Button btnDeleteServices; // Button to delete services

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        btnAddServices = findViewById(R.id.btnAddServices);
        btnModifyServices = findViewById(R.id.btnModifyServices);
        btnViewServices = findViewById(R.id.btnViewServices);
        btnDeleteServices = findViewById(R.id.btnDeleteServices); // Initialize the "Delete Services" button

        btnAddServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Add Services activity
                Intent intent = new Intent(Admin.this, AddServiceActivity.class);
                startActivity(intent);
            }
        });

        btnModifyServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the ModifyServiceActivity
                Intent intent = new Intent(Admin.this, ModifyServiceActivity.class);
                startActivity(intent);
            }
        });

        btnViewServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the ViewServicesActivity to view existing services
                Intent intent = new Intent(Admin.this, ViewServicesActivity.class);
                startActivity(intent);
            }
        });

        btnDeleteServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, DeleteServiceActivity.class);
                startActivity(intent);
            }
        });
    }
}
