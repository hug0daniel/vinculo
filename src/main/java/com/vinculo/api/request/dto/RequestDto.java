package com.vinculo.api.request.dto;

import com.vinculo.domain.request.model.Beneficiary;
import com.vinculo.domain.request.model.QuantityUnit;
import com.vinculo.domain.request.model.RequestStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class RequestDto {

    public record RequestResponse(
        UUID id,
        LocalDateTime createdAt,
        RequestStatus status,
        UUID disasterId,
        BeneficiaryDto beneficiary,
        List<ItemDto> items
    ) {}

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

    public record ItemDto(
        String productName,
        BigDecimal quantity,
        QuantityUnit unit
    ) {}

    public record RequestItemDto(
        String productName,
        BigDecimal quantity,
        QuantityUnit unit
    ) {}

    public record CreateRequestRequest(
        BeneficiaryDto beneficiary,
        UUID disasterId,
        List<RequestItemDto> items
    ) {}

    public record RequestListItem(
        UUID id,
        LocalDateTime createdAt,
        RequestStatus status,
        UUID disasterId,
        String beneficiaryName,
        int itemsCount
    ) {}
}
