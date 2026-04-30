package com.vinculo.api.request.dto;

import com.vinculo.domain.request.model.Beneficiary;

public record BeneficiaryDto(
        String name,
        String contact,
        String documentId
) {
    public static BeneficiaryDto fromDomain(Beneficiary beneficiary) {
        return new BeneficiaryDto(
                beneficiary.getName(),
                beneficiary.getContact(),
                beneficiary.getDocumentId()
        );
    }

    public Beneficiary toDomain() {
        return new Beneficiary(name, contact, documentId);
    }
}
