package com.tmcoder.inventory.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.tmcoder.inventory.entity.DeleteRequest;
import com.tmcoder.inventory.entity.UpdateRequest;
import com.tmcoder.inventory.service.DeleteService;
import com.tmcoder.inventory.service.InventoryService;
import com.tmcoder.inventory.service.UpdateService;

@RestController
@RequestMapping("/department")
public class DepartmentManagerController {

	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private DeleteService deleteService;
	@Autowired
	private UpdateService updateService;

	@GetMapping("/products/{productId}")
	public ResponseEntity<InventoryDto> getSingleInventory(@PathVariable int productId) {
		InventoryDto inventory = new InventoryDto();
		inventory = inventoryService.getSingleInventory(productId);
		return ResponseEntity.status(HttpStatus.OK).body(inventory);
	}

	@PostMapping("/products/{choiceValue}")
	public void deleteOrUpdateInventoryRequest(@RequestBody InventoryDto inventory, @PathVariable int choiceValue) {
		inventoryService.deleteOrUpdateInventoryRequest(inventory, choiceValue);
	}

	@PutMapping("/products/{choiceValue}/requests/{requestId}")
	public void approveDeleteOrUpdateRequest(@PathVariable int choiceValue, @PathVariable int requestId) {
		inventoryService.approveDeleteOrUpdateRequest(choiceValue, requestId);
	}

	@DeleteMapping("/products/{choiceValue}/requests/{requestId}")
	public void rejectDeleteOrUpdateRequest(@PathVariable int choiceValue, @PathVariable int requestId) {
		inventoryService.rejectDeleteOrUpdateRequest(choiceValue, requestId);
	}

	@GetMapping("/products/requests/{choiceValue}")
	public ResponseEntity<Object> getAllDeleteOrUpdateRequests(@PathVariable int choiceValue) {
		List<UpdateRequest> updateRequestList = new ArrayList<UpdateRequest>();
		List<DeleteRequest> deleteRequestList = new ArrayList<DeleteRequest>();
		if (choiceValue == 0) {
			updateRequestList = updateService.getAllUpdateRequests();
			return ResponseEntity.status(HttpStatus.OK).body(updateRequestList);
		} else {
			deleteRequestList = deleteService.getAllDeleteRequests();
			return ResponseEntity.status(HttpStatus.OK).body(deleteRequestList);
		}
	}

}
