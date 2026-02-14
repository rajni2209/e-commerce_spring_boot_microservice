package com.ecommerce.inventoryservice.service;

import com.ecommerce.inventoryservice.entity.Inventory;
import com.ecommerce.inventoryservice.exception.InventoryNotFoundException;
import com.ecommerce.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryManagementService {

    private final InventoryRepository inventoryRepository;

    public Inventory create(Long productId , int quantity){
        Inventory inventory = new Inventory();
        inventory.setProductId(productId);
        inventory.setAvailableQuantity(quantity);
        return inventoryRepository.save(inventory);
    }

    public Inventory update(Long productId , int quantity){
        Inventory inventory = inventoryRepository
                .findByProductId(productId)
                        .orElseThrow(InventoryNotFoundException::new);
        inventory.setAvailableQuantity(quantity);
        return inventoryRepository.save(inventory);
    }

    public Inventory get(Long productId){
        return inventoryRepository
                .findByProductId(productId)
                .orElseThrow(InventoryNotFoundException::new);
    }

    public List<Inventory> fetchAll(){
        return inventoryRepository.findAll();
    }

    public void delete(Long productId){
        Inventory inventory = inventoryRepository
                .findByProductId(productId)
                .orElseThrow(InventoryNotFoundException::new);
        inventoryRepository.delete(inventory);
    }

}
