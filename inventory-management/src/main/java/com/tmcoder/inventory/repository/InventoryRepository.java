package com.tmcoder.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tmcoder.inventory.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

}
