package com.restraunt.service;

import com.restraunt.dto.*;

import java.util.List;

public interface RestrauntService {
    RestrauntResponse sendRequest(RestrauntRequest request);

    List<RestrauntRequest> findAllPendingRequest();

    RestrauntResponse updateRequestStatus(String resId,UpdateRestrauntRequest request);

    SuspendRestrauntResponse suspendRestrauntRequest(SuspendRestrauntRequest request);
}
