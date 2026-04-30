package com.vinculo.application.request;

import com.vinculo.api.request.dto.RequestDto;

import java.util.List;
import java.util.UUID;

public interface RequestService {
    RequestDto.RequestResponse createRequest(RequestDto.CreateRequestRequest request);
    RequestDto.RequestResponse approveRequest(UUID requestId, UUID warehouseId);
    RequestDto.RequestResponse rejectRequest(UUID requestId);
    RequestDto.RequestResponse fulfillRequest(UUID requestId);
    RequestDto.RequestResponse getRequest(UUID id);
    List<RequestDto.RequestListItem> getRequests();
}
