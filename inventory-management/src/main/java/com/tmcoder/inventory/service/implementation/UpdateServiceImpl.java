package com.tmcoder.inventory.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmcoder.inventory.dto.InventoryDto;
import com.tmcoder.inventory.entity.Inventory;
import com.tmcoder.inventory.entity.UpdateRequest;
import com.tmcoder.inventory.exception.InventoryException;
import com.tmcoder.inventory.repository.UpdateRepository;
import com.tmcoder.inventory.service.UpdateService;

@Service
public class UpdateServiceImpl implements UpdateService {

	@Autowired
	private UpdateRepository updateRepo;
	@Autowired
	private InventoryServiceImpl inventoryService;

	@Override
	public String updateInventoryRequest(Inventory inventory) {
		UpdateRequest updateRequest = new UpdateRequest();
		List<InventoryDto> inventoryList = new ArrayList<InventoryDto>();
		String productNamePattern = "^[\\p{L} .'-]+$";
		String vendorPattern = "^[\\p{L} .'-]+$";
		String mrpPattern = "^[1-9]\\d{0,7}(?:\\.\\d{1,4})?$";
		String batchNumberPattern = "^[1-9]+$";
		String quantityPattern = "^[1-9]+$";
		String mrpToString = Double.toString(inventory.getMrp());
		String quantityToString = Integer.toString(inventory.getQuantity());

		if ((!inventory.getProductName().matches(productNamePattern)) || inventory.getProductName().length() == 0)
			throw new InventoryException("Enter a valid product name");
		if ((!inventory.getVendor().matches(vendorPattern)) || inventory.getVendor().length() == 0)
			throw new InventoryException("Enter a valid vendor");
		if (!mrpToString.matches(mrpPattern))
			throw new InventoryException("Enter a valid price");
		if (!inventory.getBatchNum().matches(batchNumberPattern))
			throw new InventoryException("Enter a valid Batch Number");
		if (inventory.getBatchDate() == "")
			throw new InventoryException("Enter a valid batch date");
		if (!quantityToString.matches(quantityPattern))
			throw new InventoryException("Enter a valid quantity");
		if (inventory.getQuantity() == 0)
			throw new InventoryException("Enter a valid Quantity");
		inventoryList = inventoryService.getAllInventory();
		int flag = 0;
		for (InventoryDto inv : inventoryList) {
			if (inv.getProductId() == inventory.getProductId())
				flag = 1;
		}
		if (flag == 0)
			throw new InventoryException("No such Inventory is present");
		List<UpdateRequest> updateRequests = updateRepo.findAll();
		for (UpdateRequest req : updateRequests) {
			if (req.getProductId() == inventory.getProductId())
				throw new InventoryException("This inventory is already registered for updation");
		}
		try {
			updateRequest.setProductId(inventory.getProductId());
			updateRequest.setProductName(inventory.getProductName());
			updateRequest.setVendor(inventory.getVendor());
			updateRequest.setMrp(inventory.getMrp());
			updateRequest.setBatchNum(inventory.getBatchNum());
			updateRequest.setBatchDate(inventory.getBatchDate());
			updateRequest.setQuantity(inventory.getQuantity());
			updateRequest.setStatus("Pending");
			updateRepo.save(updateRequest);
		} catch (Exception e) {
			throw new InventoryException("Not able to send request to update the inventory");
		}
		return "Request for updating inventory is successfully sent to Store Manager";
	}

	@Override
	public List<UpdateRequest> getAllUpdateRequests() {
		List<UpdateRequest> updateRequests = new ArrayList<UpdateRequest>();
		updateRequests = updateRepo.findAll();
		if (updateRequests.isEmpty())
			throw new InventoryException("Empty request list");
		return updateRequests;
	}

	@Override
	public String approveUpdateRequest(int requestId) {
		List<UpdateRequest> updateRequests = new ArrayList<UpdateRequest>();
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequests = this.getAllUpdateRequests();
		int flag = 0;
		for (UpdateRequest req : updateRequests) {
			if (req.getRequestId() == requestId) {
				flag = 1;
				updateRequest = req;
			}
		}
		if (flag == 0)
			throw new InventoryException("The product that you are trying to update is already deleted");
		InventoryDto inventory = new InventoryDto();
		inventory.setProductId(updateRequest.getProductId());
		inventory.setProductName(updateRequest.getProductName());
		inventory.setVendor(updateRequest.getVendor());
		inventory.setMrp(updateRequest.getMrp());
		inventory.setBatchNum(updateRequest.getBatchNum());
		inventory.setBatchDate(updateRequest.getBatchDate());
		inventory.setQuantity(updateRequest.getQuantity());
		inventory.setStatus(updateRequest.getStatus());
		System.err.println(inventory);
		inventoryService.changeInventory(inventory, inventory.getProductId());
		try {
			updateRepo.delete(updateRequest);
		} catch (Exception e) {
			throw new InventoryException("Somenting went wrong");
		}

		return "Inventory updated successfully";
	}

	@Override
	public String rejectUpdateRequest(int requestId) {
		List<UpdateRequest> updateRequests = new ArrayList<UpdateRequest>();
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequests = this.getAllUpdateRequests();
		int flag = 0;
		for (UpdateRequest req : updateRequests) {
			if (req.getRequestId() == requestId) {
				flag = 1;
				updateRequest = req;
			}
		}
		if (flag == 0)
			throw new InventoryException("Invalid request");
		try {
			updateRepo.delete(updateRequest);
		} catch (Exception e) {
			throw new InventoryException("Somenting went wrong");
		}

		return "Update request rejected successfully";

	}

}
