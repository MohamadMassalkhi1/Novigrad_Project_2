package com.example.novigradproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class AppUnitTests {
    @Mock
    private AddServiceActivity addServiceActivity;

    @Mock
    private DeleteServiceActivity deleteServiceActivity;

    @Mock
    private ModifyServiceActivity modifyServiceActivity;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        // You can mock specific behaviors or setup here if needed
    }

    @Test
    public void testAddServiceFunctionality() {
        // Test the functionality of AddServiceActivity
        // For example, you can simulate user input, call methods, and verify results
        // Sample:
        when(addServiceActivity.getServiceNameEditText().getText()).thenReturn("New Service");
        when(addServiceActivity.getFormInformationEditText().getText()).thenReturn("Form Info");
        when(addServiceActivity.getDocumentInformationEditText().getText()).thenReturn("Doc Info");

        addServiceActivity.onClick(addServiceActivity.getAddServiceButton());

        verify(addServiceActivity.getDatabaseReference()).child("New Service");
        verify(addServiceActivity.getDatabaseReference().child("New Service")).child("serviceName").setValue("New Service");
        verify(addServiceActivity.getDatabaseReference().child("New Service")).child("formInformation").setValue("Form Info");
        verify(addServiceActivity.getDatabaseReference().child("New Service")).child("documentInformation").setValue("Doc Info");
    }

    @Test
    public void testDeleteServiceFunctionality() {
        // Test the functionality of DeleteServiceActivity
        // For example, you can simulate user input, call methods, and verify results
        // Sample:
        when(deleteServiceActivity.getServiceSpinner().getSelectedItem()).thenReturn("ServiceNameToDelete");
        when(deleteServiceActivity.deleteService("ServiceNameToDelete")).thenReturn(true);

        deleteServiceActivity.onClick(deleteServiceActivity.getConfirmDeleteButton());

        verify(deleteServiceActivity.getDatabaseReference().child("ServiceNameToDelete")).removeValue();
    }

    @Test
    public void testModifyServiceFunctionality() {
        // Test the functionality of ModifyServiceActivity
        // You can simulate user interactions, call methods, and verify the results
        // Example:
        when(modifyServiceActivity.getServiceSpinner().getSelectedItem()).thenReturn("ServiceNameToModify");
        when(modifyServiceActivity.getServiceNameEditText().getText()).thenReturn("Modified Service Name");
        when(modifyServiceActivity.getFormInformationEditText().getText()).thenReturn("Modified Form Info");
        when(modifyServiceActivity.getDocumentInformationEditText().getText()).thenReturn("Modified Doc Info");

        modifyServiceActivity.onClick(modifyServiceActivity.getSaveServiceButton());

        // Verify that the service is updated in Firebase with the modified values
        verify(modifyServiceActivity.getServicesRef().child("ServiceNameToModify")).setValue(any(ServiceModel.class));
    }

    @Test
    public void testViewServicesFunctionality() {
        // Test the functionality of ViewServicesActivity
        // You can simulate the data from Firebase and verify the display
        // Example:
        when(viewServicesActivity.getDatabaseReference().addValueEventListener(any(ValueEventListener.class)))
                .thenAnswer((Answer<DatabaseReference>) invocation -> {
                    ValueEventListener valueEventListener = (ValueEventListener) invocation.getArguments()[0];
                    DataSnapshot dataSnapshot = Mockito.mock(DataSnapshot.class);
                    when(dataSnapshot.getChildren()).thenReturn(Arrays.asList(
                            createServiceDataSnapshot("Service1", "Form1", "Doc1"),
                            createServiceDataSnapshot("Service2", "Form2", "Doc2")
                    ));
                    valueEventListener.onDataChange(dataSnapshot);
                    return viewServicesActivity.getDatabaseReference();
                });

        // Verify that the adapter of the ListView is updated with the service names
        verify(viewServicesActivity.getListViewServices().getAdapter()).add("Service1");
        verify(viewServicesActivity.getListViewServices().getAdapter()).add("Service2");
    }

    @Test
    public void testServiceModelFunctionality() {
        // Test the functionality of the ServiceModel class
        // You can create ServiceModel objects, set their properties, and verify them
        // Example:
        ServiceModel service = new ServiceModel();
        service.setServiceName("Service1");
        service.setFormInformation("Form1");
        service.setDocumentInformation("Doc1");

        assertEquals("Service1", service.getServiceName());
        assertEquals("Form1", service.getFormInformation());
        assertEquals("Doc1", service.getDocumentInformation());
    }

    // Helper method to create a mock DataSnapshot for services
    private DataSnapshot createServiceDataSnapshot(String serviceName, String formInfo, String docInfo) {
        DataSnapshot dataSnapshot = Mockito.mock(DataSnapshot.class);
        when(dataSnapshot.getKey()).thenReturn(serviceName);
        when(dataSnapshot.child("serviceName").getValue(String.class)).thenReturn(serviceName);
        when(dataSnapshot.child("formInformation").getValue(String.class)).thenReturn(formInfo);
        when(dataSnapshot.child("documentInformation").getValue(String.class)).thenReturn(docInfo);
        return dataSnapshot;
    }
}

