package com.restraunt.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder(toBuilder = true)
public class Inventory {

    @Id
    private String inventoryId;
    private String itemId;
    private String restrauntId;

}
