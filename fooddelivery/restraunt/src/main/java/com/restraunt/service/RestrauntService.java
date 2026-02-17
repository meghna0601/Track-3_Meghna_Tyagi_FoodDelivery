package com.restraunt.service;

import com.restraunt.dto.RestrauntRequest;
import com.restraunt.dto.RestrauntResponse;
import com.restraunt.dto.UpdateRestrauntRequest;

import java.util.List;

public interface RestrauntService {
    RestrauntResponse sendRequest(RestrauntRequest request);

    List<RestrauntRequest> findAllPendingRequest();

    RestrauntResponse updateRequestStatus(String resId,UpdateRestrauntRequest request);
}
