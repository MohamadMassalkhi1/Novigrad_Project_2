package com.example.novigradproject;

import java.util.List;


public class ServiceModel {
    private String serviceName;
    private String formInformation;
    private String documentInformation;

    public ServiceModel() {
        // No-argument constructor required by Firebase
    }

    public ServiceModel(String serviceName, String formInformation, String documentInformation) {
        this.serviceName = serviceName;
        this.formInformation = formInformation;
        this.documentInformation = documentInformation;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getFormInformation() {
        return formInformation;
    }

    public void setFormInformation(String formInformation) {
        this.formInformation = formInformation;
    }

    public String getDocumentInformation() {
        return documentInformation;
    }

    public void setDocumentInformation(String documentInformation) {
        this.documentInformation = documentInformation;
    }
}
