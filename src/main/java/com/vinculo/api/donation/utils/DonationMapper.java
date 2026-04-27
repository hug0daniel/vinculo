package com.vinculo.api.donation.utils;

import com.vinculo.api.donation.dto.DonationRequest;
import com.vinculo.api.donation.dto.DonationResponse;
import com.vinculo.domain.donation.model.DonationOffer;
import com.vinculo.domain.donation.model.Donor;
import com.vinculo.domain.donation.model.QuantityUnit;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DonationMapper {

    public DonationOffer toEntity(DonationRequest request) {
        var donor = new Donor(
            request.donor().name(),
            request.donor().contact(),
            request.donor().type()
        );

        var donation = DonationOffer.builder()
            .donor(donor)
            .build();

        for (var itemReq : request.items()) {
            donation.addItem(
                itemReq.productId(),
                itemReq.quantity(),
                itemReq.unit(),
                itemReq.expiryDate()
            );
        }

        return donation;
    }

    public DonationResponse toResponse(DonationOffer donation) {
        var donor = donation.getDonor();
        var donorDto = new DonationResponse.DonorDto(
            donor.getName(),
            donor.getContact(),
            donor.getType().name()
        );

        var itemsDto = donation.getItems().stream()
            .map(item -> new DonationResponse.ItemDto(
                item.getProductId(),
                item.getQuantity(),
                item.getUnit(),
                item.getExpiryDate()
            ))
            .toList();

        return new DonationResponse(
            donation.getId(),
            donation.getStatus(),
            donation.getCreatedAt(),
            donorDto,
            itemsDto
        );
    }
}