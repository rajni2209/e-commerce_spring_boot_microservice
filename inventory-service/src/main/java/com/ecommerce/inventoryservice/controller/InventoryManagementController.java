package com.ecommerce.inventoryservice.controller;


import com.ecommerce.inventoryservice.DTO.InventoryRequest;
import com.ecommerce.inventoryservice.DTO.InventoryResponse;
import com.ecommerce.inventoryservice.entity.Inventory;
import com.ecommerce.inventoryservice.service.InventoryManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory/admin")
@RequiredArgsConstructor
public class InventoryManagementController {

    private final InventoryManagementService inventoryManagementService;

    @GetMapping("/get/{productId}")
    public ResponseEntity<InventoryResponse> getInventory(@PathVariable Long productId){
        Inventory inventory = inventoryManagementService.get(productId);
        return ResponseEntity.ok(InventoryResponse.from(inventory));
    }

    @GetMapping("/get")
    public ResponseEntity<List<InventoryResponse>> getAllInventory(){
        List<InventoryResponse> list = inventoryManagementService.fetchAll()
                .stream()
                .map(InventoryResponse::from)
                .toList();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/add")
    public ResponseEntity<InventoryResponse> addInventory(
            @RequestBody @Valid InventoryRequest request
    ){
        Inventory inventory = inventoryManagementService.create(
                request.getProductId(),
                request.getQuantity()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(InventoryResponse.from(inventory));
    }

    @PutMapping("/update")
    public ResponseEntity<InventoryResponse> updateInventory(
            @RequestBody @Valid InventoryRequest request
    ){
        Inventory update = inventoryManagementService.update(
                request.getProductId(),
                request.getQuantity()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(InventoryResponse.from(update));
    }

    @DeleteMapping("/delete/{productId}")
    public void delete(
            @PathVariable Long productId
    ){
        inventoryManagementService.delete(productId);
    }

}
