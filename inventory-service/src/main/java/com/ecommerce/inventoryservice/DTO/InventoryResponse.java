package com.ecommerce.inventoryservice.DTO;

import com.ecommerce.inventoryservice.entity.Inventory;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryResponse {

    private Long productId;
    private int quantity;

    public static InventoryResponse from(Inventory inventory){
        return new InventoryResponse(
                inventory.getProductId(),
                inventory.getAvailableQuantity()
        );
    }

}
