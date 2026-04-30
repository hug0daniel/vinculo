package com.vinculo.domain.request.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Beneficiary {

    private String name;
    private String contact;
    private String documentId;

    protected Beneficiary() {
    }

    public Beneficiary(String name, String contact, String documentId) {
        this.name = name;
        this.contact = contact;
        this.documentId = documentId;
    }

    public String getName() { return name; }
    public String getContact() { return contact; }
    public String getDocumentId() { return documentId; }
}
