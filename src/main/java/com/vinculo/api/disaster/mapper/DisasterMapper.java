package com.vinculo.api.disaster.mapper;

import com.vinculo.api.disaster.dto.DisasterDto;
import com.vinculo.domain.disaster.model.Disaster;
import org.springframework.stereotype.Service;

@Service
public class DisasterMapper {

    public Disaster toEntity(DisasterDto.CreateDisasterRequest request) {
        return Disaster.create(request.name(), request.type(), request.location());
    }

    public DisasterDto.DisasterResponse toResponse(Disaster disaster) {
        return new DisasterDto.DisasterResponse(
                disaster.getId(),
                disaster.getName(),
                disaster.getType(),
                disaster.getStatus(),
                disaster.getLocation(),
                disaster.getStartDate(),
                disaster.getEndDate()
        );
    }

    public DisasterDto.DisasterListItem toListItem(Disaster disaster) {
        return new DisasterDto.DisasterListItem(
                disaster.getId(),
                disaster.getName(),
                disaster.getType(),
                disaster.getStatus(),
                disaster.getLocation()
        );
    }
}
