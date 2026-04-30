package com.vinculo.application.disaster;

import com.vinculo.api.disaster.dto.DisasterDto;
import com.vinculo.api.disaster.mapper.DisasterMapper;
import com.vinculo.application.exception.ResourceNotFoundException;
import com.vinculo.domain.disaster.model.Disaster;
import com.vinculo.domain.disaster.repository.DisasterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DisasterServiceImpl implements DisasterService {

    private final DisasterRepository disasterRepository;
    private final DisasterMapper mapper;

    public DisasterServiceImpl(DisasterRepository disasterRepository, DisasterMapper mapper) {
        this.disasterRepository = disasterRepository;
        this.mapper = mapper;
    }

    @Override
    public DisasterDto.DisasterResponse createDisaster(DisasterDto.CreateDisasterRequest request) {
        Disaster disaster = mapper.toEntity(request);
        Disaster saved = disasterRepository.save(disaster);
        return mapper.toResponse(saved);
    }

    @Override
    public DisasterDto.DisasterResponse updateDisaster(UUID disasterId, DisasterDto.UpdateDisasterRequest request) {
        Disaster disaster = disasterRepository.findById(disasterId)
                .orElseThrow(() -> new ResourceNotFoundException("Disaster not found"));
        disaster.updateDetails(request.name(), request.type(), request.location());
        return mapper.toResponse(disaster);
    }

    @Override
    public DisasterDto.DisasterResponse deactivateDisaster(UUID disasterId) {
        Disaster disaster = disasterRepository.findById(disasterId)
                .orElseThrow(() -> new ResourceNotFoundException("Disaster not found"));
        disaster.deactivate();
        return mapper.toResponse(disaster);
    }

    @Override
    public DisasterDto.DisasterResponse reactivateDisaster(UUID disasterId) {
        Disaster disaster = disasterRepository.findById(disasterId)
                .orElseThrow(() -> new ResourceNotFoundException("Disaster not found"));
        disaster.reactivate();
        return mapper.toResponse(disaster);
    }

    @Override
    public DisasterDto.DisasterResponse getDisaster(UUID id) {
        Disaster disaster = disasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disaster not found"));
        return mapper.toResponse(disaster);
    }

    @Override
    public List<DisasterDto.DisasterListItem> getDisasters() {
        return disasterRepository.findAll().stream()
                .map(mapper::toListItem)
                .toList();
    }
}
