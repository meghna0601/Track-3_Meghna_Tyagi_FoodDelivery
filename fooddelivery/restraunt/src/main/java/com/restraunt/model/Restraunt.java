package com.restraunt.model;

import com.restraunt.enums.RestrauntStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder(toBuilder = true)
public class Restraunt {
    @Id
    private String restrauntId;
    private String restrauntName;
    private String ownerUserId;
    private String restrauntAddress;
    private Double latitude;
    private Double longitude;
    private Integer hours;
    private Integer DeliveryRadiusKm;
    private RestrauntStatus status;

}
