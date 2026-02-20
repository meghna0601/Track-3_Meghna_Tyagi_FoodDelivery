package com.restraunt.model;

import com.restraunt.enums.RestrauntStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
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
    @Enumerated(EnumType.STRING)
    private RestrauntStatus status;

}
