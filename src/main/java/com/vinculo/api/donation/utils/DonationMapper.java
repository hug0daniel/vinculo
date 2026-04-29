package com.vinculo.api.donation.utils;

import com.vinculo.api.donation.dto.DonationRequest;
import com.vinculo.api.donation.dto.DonationResponse;
import com.vinculo.domain.donation.model.DonationOffer;
import com.vinculo.domain.donation.model.Donor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DonationMapper {

    public DonationOffer toEntity(DonationRequest request) {
        Donor donor = new Donor(
            request.donor().name(),
            request.donor().contact(),
            request.donor().type()
        );

        DonationOffer donation = DonationOffer.builder()
            .donor(donor)
            .build();

        for (DonationRequest.DonationItemDto itemReq : request.items()) {
            donation.addItem(
                itemReq.productName(),
                itemReq.quantity(),
                itemReq.unit(),
                itemReq.expiryDate()
            );
        }

        return donation;
    }

    public DonationResponse toResponse(DonationOffer donation) {
        Donor donor = donation.getDonor();
        DonationResponse.DonorDto donorDto = new DonationResponse.DonorDto(
            donor.getName(),
            donor.getContact(),
            donor.getType().name()
        );

        List<DonationResponse.ItemDto> itemsDto = donation.getItems().stream()
            .map(item -> new DonationResponse.ItemDto(
                item.getProductName(),
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