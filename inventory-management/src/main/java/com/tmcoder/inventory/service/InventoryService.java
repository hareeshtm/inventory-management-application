package com.tmcoder.inventory.service;

import java.util.List;

import com.tmcoder.inventory.dto.InventoryDto;
import com.tmcoder.inventory.entity.Inventory;

public interface InventoryService {

	InventoryDto getSingleInventory(int productId);

	String deleteOrUpdateInventoryRequest(InventoryDto inventory, int choiceValue);

	String approveDeleteOrUpdateRequest(int choiceValue, int requestId);

	String rejectDeleteOrUpdateRequest(int choiceValue, int requestId);

	String addInventory(InventoryDto inventory);

	String deleteInventory(int productId);

	String changeInventory(InventoryDto inventory, int productId);

	String approvePendingInventory(int productId);

	List<InventoryDto> getAllInventory();

	List<InventoryDto> getPendingOrApprovedInventory(int choiceValue);

	String addPendingInventory(Inventory inventory);

	List<InventoryDto> getApprovedInventory();

	List<InventoryDto> getPendingInventory();
}
