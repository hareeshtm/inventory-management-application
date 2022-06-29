package com.tmcoder.inventory.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmcoder.inventory.dto.InventoryDto;
import com.tmcoder.inventory.service.InventoryService;
import com.tmcoder.inventory.service.UpdateService;

@RestController
@RequestMapping("/store")
public class StoreManagerController {

	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private UpdateService updateService;

	@PostMapping("/products")
	public void addInventory(@RequestBody InventoryDto inventory) {
		inventoryService.addInventory(inventory);
	}

	@DeleteMapping("/products/{productId}")
	public void deleteInventory(@PathVariable int productId) {
		inventoryService.deleteInventory(productId);
	}

	@PutMapping("/products/{productId}")
	public void changeInventory(@RequestBody InventoryDto inventory, @PathVariable int productId) {
		inventoryService.changeInventory(inventory, productId);
	}

	@PostMapping("/products/{productId}")
	public void approveInventory(@PathVariable int productId) {
		inventoryService.approvePendingInventory(productId);
	}

	@GetMapping("/products")
	public ResponseEntity<List<InventoryDto>> getAllInventory() {
		List<InventoryDto> inventoryList = new ArrayList<InventoryDto>();
		inventoryList = inventoryService.getAllInventory();
		return ResponseEntity.status(HttpStatus.OK).body(inventoryList);
	}

	@GetMapping("/products/{choiceValue}")
	public ResponseEntity<List<InventoryDto>> getPendingOrApprovedInventory(@PathVariable int choiceValue) {
		List<InventoryDto> inventoryList = new ArrayList<InventoryDto>();
		inventoryList = inventoryService.getPendingOrApprovedInventory(choiceValue);
		return ResponseEntity.status(HttpStatus.OK).body(inventoryList);
	}
}
