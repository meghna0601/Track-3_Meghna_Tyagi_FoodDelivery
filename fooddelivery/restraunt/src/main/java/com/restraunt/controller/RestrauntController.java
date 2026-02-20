package com.restraunt.controller;

import com.restraunt.dto.*;
import com.restraunt.service.RestrauntService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/restraunt")
@RequiredArgsConstructor
public class RestrauntController {

    private final RestrauntService restrauntService;

    @PostMapping("/request")
    public ResponseEntity<RestrauntResponse> sendRequestForApproval(@RequestBody RestrauntRequest request) {
        RestrauntResponse response = restrauntService.sendRequest(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/pendingRequest")
    public ResponseEntity<List<RestrauntRequest>> sendRequestForApproval() {
        List<RestrauntRequest> response = restrauntService.findAllPendingRequest();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/requestStatus")
    public ResponseEntity<RestrauntResponse> updateRequestStatus(@RequestParam("restrauntId")String resId,@RequestBody UpdateRestrauntRequest request) {
        RestrauntResponse response = restrauntService.updateRequestStatus(resId,request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/suspend")
    public ResponseEntity<SuspendRestrauntResponse> suspendRestraunt(@RequestBody SuspendRestrauntRequest request) {
        SuspendRestrauntResponse response = restrauntService.suspendRestrauntRequest(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
