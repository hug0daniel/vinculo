package com.vinculo.application.request;

import com.vinculo.api.request.dto.CreateRequestRequest;
import com.vinculo.api.request.dto.RequestListItem;
import com.vinculo.api.request.dto.RequestResponse;

import java.util.List;
import java.util.UUID;

public interface RequestService {
    RequestResponse createRequest(CreateRequestRequest request);
    RequestResponse approveRequest(UUID requestId, UUID warehouseId);
    RequestResponse rejectRequest(UUID requestId);
    RequestResponse fulfillRequest(UUID requestId);
    RequestResponse getRequest(UUID id);
    List<RequestListItem> getRequests();
}
