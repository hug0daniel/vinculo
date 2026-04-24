package com.vinculo.application.donation;

import com.vinculo.api.donation.DonationRequest;
import com.vinculo.api.donation.DonationResponse;
import com.vinculo.domain.donation.model.DonationOffer;
import com.vinculo.domain.donation.model.Donor;
import com.vinculo.domain.donation.model.DonationItem;
import org.springframework.stereotype.Service;

@Service
public class DonationMapper {

    public DonationOffer toEntity(DonationRequest request) {
        var donor = new Donor(
            request.donor().name(),
            request.donor().contact(),
            request.donor().type()
        );

        var donation = new DonationOffer(donor);

        for (var itemReq : request.items()) {
            var item = new DonationItem(
                itemReq.productId(),
                itemReq.quantity(),
                itemReq.unit(),
                itemReq.expiryDate()
            );
            donation.addItem(item);
        }

        return donation;
    }

    public DonationResponse toResponse(DonationOffer donation) {
        var donorDto = new DonationResponse.DonorDto(
            donation.getDonor().name(),
            donation.getDonor().contact(),
            donation.getDonor().type().name()
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