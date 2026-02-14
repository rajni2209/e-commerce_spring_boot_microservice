package com.ecommerce.inventoryservice.exception;

public class InventoryNotFoundException extends RuntimeException {

    public InventoryNotFoundException(){
        super("Inventory not found");
    }

}
