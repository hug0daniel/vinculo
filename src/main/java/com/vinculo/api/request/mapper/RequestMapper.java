package com.vinculo.api.request.mapper;

import com.vinculo.api.request.dto.RequestDto;
import com.vinculo.domain.request.model.Request;
import com.vinculo.domain.request.model.RequestItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestMapper {

    public RequestDto.RequestResponse toResponse(Request request) {
        RequestDto.BeneficiaryDto beneficiaryDto = RequestDto.BeneficiaryDto.fromDomain(request.getBeneficiary());

        List<RequestDto.ItemDto> itemsDto = request.getItems().stream()
                .map(item -> new RequestDto.ItemDto(
                        item.getProductName(),
                        item.getQuantity(),
                        item.getUnit()
                ))
                .toList();

        return new RequestDto.RequestResponse(
                request.getId(),
                request.getCreatedAt(),
                request.getStatus(),
                request.getDisasterId(),
                beneficiaryDto,
                itemsDto
        );
    }

    public RequestDto.RequestListItem toListItem(Request request) {
        return new RequestDto.RequestListItem(
                request.getId(),
                request.getCreatedAt(),
                request.getStatus(),
                request.getDisasterId(),
                request.getBeneficiary().getName(),
                request.getItems().size()
        );
    }
}
