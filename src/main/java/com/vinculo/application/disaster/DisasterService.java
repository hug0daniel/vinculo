package com.vinculo.application.disaster;

import com.vinculo.api.disaster.dto.CreateDisasterRequest;
import com.vinculo.api.disaster.dto.DisasterItem;
import com.vinculo.api.disaster.dto.DisasterResponse;
import com.vinculo.api.disaster.dto.UpdateDisasterRequest;

import java.util.List;
import java.util.UUID;

public interface DisasterService {
    DisasterResponse createDisaster(CreateDisasterRequest request);
    DisasterResponse updateDisaster(UUID disasterId, UpdateDisasterRequest request);
    DisasterResponse deactivateDisaster(UUID disasterId);
    DisasterResponse reactivateDisaster(UUID disasterId);
    DisasterResponse getDisaster(UUID id);
    List<DisasterItem> getDisasters();
}
