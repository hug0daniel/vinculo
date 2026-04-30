package com.vinculo.api.request.mapper;

import com.vinculo.api.request.dto.BeneficiaryDto;
import com.vinculo.api.request.dto.ItemDto;
import com.vinculo.api.request.dto.RequestListItem;
import com.vinculo.api.request.dto.RequestResponse;
import com.vinculo.domain.request.model.Request;
import com.vinculo.domain.request.model.RequestItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestMapper {

    public RequestResponse toResponse(Request request) {
        BeneficiaryDto beneficiaryDto = BeneficiaryDto.fromDomain(request.getBeneficiary());

        List<ItemDto> itemsDto = request.getItems().stream()
                .map(item -> new ItemDto(
                        item.getProductName(),
                        item.getQuantity(),
                        item.getUnit()
                ))
                .toList();

        return new RequestResponse(
                request.getId(),
                request.getCreatedAt(),
                request.getStatus(),
                request.getDisasterId(),
                beneficiaryDto,
                itemsDto
        );
    }

    public RequestListItem toListItem(Request request) {
        return new RequestListItem(
                request.getId(),
                request.getCreatedAt(),
                request.getStatus(),
                request.getDisasterId(),
                request.getBeneficiary().getName(),
                request.getItems().size()
        );
    }
}
