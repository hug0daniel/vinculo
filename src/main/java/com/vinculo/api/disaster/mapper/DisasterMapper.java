package com.vinculo.api.disaster.mapper;

import com.vinculo.api.disaster.dto.CreateDisasterRequest;
import com.vinculo.api.disaster.dto.DisasterItem;
import com.vinculo.api.disaster.dto.DisasterResponse;
import com.vinculo.domain.disaster.model.Disaster;
import org.springframework.stereotype.Service;

@Service
public class DisasterMapper {

    public Disaster toEntity(CreateDisasterRequest request) {
        return Disaster.create(request.name(), request.type(), request.location());
    }

    public DisasterResponse toResponse(Disaster disaster) {
        return new DisasterResponse(
                disaster.getId(),
                disaster.getName(),
                disaster.getType(),
                disaster.getStatus(),
                disaster.getLocation(),
                disaster.getStartDate(),
                disaster.getEndDate()
        );
    }

    public DisasterItem toListItem(Disaster disaster) {
        return new DisasterItem(
                disaster.getId(),
                disaster.getName(),
                disaster.getType(),
                disaster.getStatus(),
                disaster.getLocation()
        );
    }
}
