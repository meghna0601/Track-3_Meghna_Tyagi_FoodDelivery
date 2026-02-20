package com.restraunt.model;

import com.restraunt.enums.ItemCategory;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Items {

    @Id
    private String itemId;
    private Date createdAt;
    private Date updatedAt;
    private String itemName;
    @Enumerated(EnumType.STRING)
    private ItemCategory itemcategory;
    private Integer quantity;
    private Integer price;
    private Boolean isVeg;
    private Boolean isOutOfStock;
}
