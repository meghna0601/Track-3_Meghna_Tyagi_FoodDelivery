package com.restraunt.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Entity
@Builder(toBuilder = true)
@Data
public class SuspendRestraunt {
    @Id
    private String id;
    private String restrauntId;
    private String reason;
}
