package com.vinculo.application.disaster;

import com.vinculo.api.disaster.dto.DisasterDto;

import java.util.List;
import java.util.UUID;

public interface DisasterService {
    DisasterDto.DisasterResponse createDisaster(DisasterDto.CreateDisasterRequest request);
    DisasterDto.DisasterResponse updateDisaster(UUID disasterId, DisasterDto.UpdateDisasterRequest request);
    DisasterDto.DisasterResponse deactivateDisaster(UUID disasterId);
    DisasterDto.DisasterResponse reactivateDisaster(UUID disasterId);
    DisasterDto.DisasterResponse getDisaster(UUID id);
    List<DisasterDto.DisasterListItem> getDisasters();
}
