package com.restraunt.model;

import com.restraunt.enums.ItemCategory;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Builder(toBuilder = true)
public class Items {

    @Id
    private String itemId;
    private Date createdAt;
    private Date updatedAt;
    private String itemName;
    private ItemCategory itemcategory;
    private Integer quantity;
    private Integer price;
    private Boolean isVeg;
    private Boolean isOutOfStock;
}
