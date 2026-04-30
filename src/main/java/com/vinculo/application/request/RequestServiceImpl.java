package com.vinculo.application.request;

import com.vinculo.api.request.dto.CreateRequestRequest;
import com.vinculo.api.request.dto.RequestItemDto;
import com.vinculo.api.request.dto.RequestListItem;
import com.vinculo.api.request.dto.RequestResponse;
import com.vinculo.api.request.mapper.RequestMapper;
import com.vinculo.application.exception.ResourceNotFoundException;
import com.vinculo.application.inventory.StockManagementPort;
import com.vinculo.domain.request.model.Beneficiary;
import com.vinculo.domain.request.model.Request;
import com.vinculo.domain.request.model.RequestItem;
import com.vinculo.domain.request.repository.RequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper mapper;
    private final StockManagementPort stockManagementPort;

    public RequestServiceImpl(RequestRepository requestRepository,
                           RequestMapper mapper,
                           StockManagementPort stockManagementPort) {
        this.requestRepository = requestRepository;
        this.mapper = mapper;
        this.stockManagementPort = stockManagementPort;
    }

    @Override
    public RequestResponse createRequest(CreateRequestRequest request) {
        Beneficiary beneficiary = request.beneficiary().toDomain();
        Request newRequest = Request.create(beneficiary, request.disasterId());

        for (RequestItemDto item : request.items()) {
            newRequest.addItem(item.productName(), item.quantity(), item.unit());
        }

        Request saved = requestRepository.save(newRequest);
        return mapper.toResponse(saved);
    }

    @Override
    public RequestResponse approveRequest(UUID requestId, UUID warehouseId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        for (RequestItem item : request.getItems()) {
            stockManagementPort.allocateStock(warehouseId, item.getProductName(), item.getQuantity().intValue());
        }

        request.approve(warehouseId);
        return mapper.toResponse(request);
    }

    @Override
    public RequestResponse rejectRequest(UUID requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        request.reject();
        return mapper.toResponse(request);
    }

    @Override
    public RequestResponse fulfillRequest(UUID requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        request.fulfill();
        return mapper.toResponse(request);
    }

    @Override
    public RequestResponse getRequest(UUID id) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        return mapper.toResponse(request);
    }

    @Override
    public List<RequestListItem> getRequests() {
        return requestRepository.findAll().stream()
                .map(mapper::toListItem)
                .toList();
    }
}
