package com.restraunt.service.impl;

import com.restraunt.dto.*;
import com.restraunt.enums.RestrauntStatus;
import com.restraunt.exception.RequestSentFailedException;
import com.restraunt.exception.RestrauntNotFoundException;
import com.restraunt.model.Restraunt;
import com.restraunt.model.SuspendRestraunt;
import com.restraunt.repository.RestrauntRepository;
import com.restraunt.repository.SuspendRestrauntRepository;
import com.restraunt.service.RestrauntService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestrauntServiceImpl  implements RestrauntService {

    private final  RestrauntRepository restrauntRepository;
    private  final SuspendRestrauntRepository suspendRestrauntRepository;

    @Override
    public RestrauntResponse sendRequest(RestrauntRequest request) {
       Optional<Restraunt> restrauntDetailsOptional = restrauntRepository.findByRestrauntNameAndLatitudeAndLongitude(request.restrauntName(),request.latitude(),request.longitude());

       if(!restrauntDetailsOptional.isPresent()) {
           String resId = UUID.randomUUID().toString();
           restrauntRepository.save(Restraunt.builder()
                   .restrauntId(resId)
                   .restrauntName(request.restrauntName())
                   .restrauntAddress(request.restrauntAddress())
                   .latitude(request.latitude())
                   .longitude(request.longitude())
                   .hours(request.hours())
                   .ownerUserId(request.ownerUserId())
                   .DeliveryRadiusKm(request.deliveryRadiusKm())
                   .status(RestrauntStatus.valueOf(RestrauntStatus.PENDING.name()))
                   .build());
           log.info("Request Sent{}",resId);
           return RestrauntResponse.builder()
                   .restrauntId(resId).details(request).message("Your Request Sent SuccessFully").build();
       }

       throw new RequestSentFailedException("Something went wrong while sending request. Try again after Sometime!!");

    }

    @Override
    public List<RestrauntRequest> findAllPendingRequest() {
        List<Restraunt> restraunts = restrauntRepository.findAllByStatus(RestrauntStatus.PENDING);
        List<RestrauntRequest> restrauntDetails = new LinkedList<>();
       restraunts.forEach(restraunt -> {
           restrauntDetails.add(RestrauntRequest.builder().restrauntName(restraunt.getRestrauntName())
                   .restrauntAddress(restraunt.getRestrauntAddress())
                   .ownerUserId(restraunt.getOwnerUserId())
                   .hours(restraunt.getHours())
                   .latitude(restraunt.getLatitude())
                   .longitude(restraunt.getLongitude())
                   .deliveryRadiusKm(restraunt.getDeliveryRadiusKm()).build());
       });
       return restrauntDetails;
    }

    @Override
    public RestrauntResponse updateRequestStatus(String resId,UpdateRestrauntRequest request) {
        Optional<Restraunt> restraunt = restrauntRepository.findById(resId);
        if(restraunt.isPresent()) {
           Restraunt resData = restraunt.get();
           resData.setStatus(RestrauntStatus.valueOf(request.status().name()));
           restrauntRepository.save(resData);
           log.info("Request status Updated Successfully");
           return RestrauntResponse.builder()
                   .restrauntId(resData.getRestrauntId())
                   .message("Request Status Updated").build();
        }
        throw new RestrauntNotFoundException("Id is not correct");

    }

    @Override
    public SuspendRestrauntResponse suspendRestrauntRequest(SuspendRestrauntRequest request) {
        Boolean isExist = restrauntRepository.existsById(request.restrauntId());
        if(isExist) {
            suspendRestrauntRepository.save(SuspendRestraunt.builder()
                    .restrauntId(request.restrauntId())
                    .reason(request.suspendReason())
                    .build());
            log.info("Request Suspend Successfully");
            return SuspendRestrauntResponse.builder()
                    .message("Request Suspend Successfully")
                    .reason(request.suspendReason()).build();
        }
        throw new RestrauntNotFoundException("Id does not found");
    }
}
