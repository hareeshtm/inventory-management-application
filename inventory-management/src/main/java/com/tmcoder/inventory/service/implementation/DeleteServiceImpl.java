package com.tmcoder.inventory.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmcoder.inventory.dto.InventoryDto;
import com.tmcoder.inventory.entity.DeleteRequest;
import com.tmcoder.inventory.entity.Inventory;
import com.tmcoder.inventory.entity.UpdateRequest;
import com.tmcoder.inventory.exception.InventoryException;
import com.tmcoder.inventory.repository.DeleteRequestRepository;
import com.tmcoder.inventory.repository.UpdateRepository;
import com.tmcoder.inventory.service.DeleteService;

@Service
public class DeleteServiceImpl implements DeleteService {

	@Autowired
	private DeleteRequestRepository deleteRepo;
	@Autowired
	private InventoryServiceImpl inventoryService;
	@Autowired
	private UpdateRepository updateRepo;

	@Override
	public String deleteInventoryRequest(Inventory inventory) {
		DeleteRequest deleteRequest = new DeleteRequest();
		List<InventoryDto> inventoryList = new ArrayList<InventoryDto>();
		inventoryList = inventoryService.getAllInventory();
		int flag = 0;
		for (InventoryDto inv : inventoryList) {
			if (inv.getProductId() == inventory.getProductId())
				flag = 1;
		}
		if (flag == 0) {
			throw new InventoryException("No Such Inventory is present");
		}

		List<DeleteRequest> deleteRequests = deleteRepo.findAll();

		int flg = 0;
		for (DeleteRequest req : deleteRequests) {
			if (req.getInventory().getProductId() == inventory.getProductId())
				flg = 1;
		}
		if (flg == 1)
			throw new InventoryException("This Inventory is already requested for deletion");
		try {
			deleteRequest.setInventory(inventory);
			deleteRepo.save(deleteRequest);
		} catch (Exception e) {
			throw new InventoryException("Not able to send request to delete the particular Inventory");
		}
		return "Request for deleting inventory issuccessfully send to Store Manager";
	}

	@Override
	public List<DeleteRequest> getAllDeleteRequests() {

		List<DeleteRequest> deleteRequests = new ArrayList<DeleteRequest>();
		deleteRequests = deleteRepo.findAll();
		if (deleteRequests.isEmpty())
			throw new InventoryException("Empty Request List");
		return deleteRequests;
	}

	@Override
	public String approveDeleteRequest(int requestId) {

		List<DeleteRequest> deleteRequests = new ArrayList<DeleteRequest>();
		DeleteRequest deleteRequest = new DeleteRequest();
		deleteRequests = this.getAllDeleteRequests();
		int flag = 0;
		for (DeleteRequest req : deleteRequests) {
			if (req.getRequestId() == requestId) {
				flag = 1;
				deleteRequest = req;
			}
		}
		if (flag == 0)
			throw new InventoryException("Invalid Request");

		flag = 0;
		List<InventoryDto> inventoryList = inventoryService.getAllInventory();
		for (InventoryDto inv : inventoryList) {
			if (inv.getProductId() == deleteRequest.getInventory().getProductId())
				flag = 1;
		}
		if (flag == 0)
			throw new InventoryException("The Product that you are trying to delete is already deleted");

		int productId = deleteRequest.getInventory().getProductId();
		List<UpdateRequest> updateRequestList = updateRepo.findAll();
		for (UpdateRequest req : updateRequestList) {
			if (req.getProductId() == productId) {
				updateRepo.deleteById(req.getRequestId());
				break;
			}
		}
		try {
			deleteRepo.delete(deleteRequest);
		} catch (Exception e) {
			throw new InventoryException("Something went wrong");
		}
		inventoryService.deleteInventory(productId);
		return "Inventory Deleted Successfully";
	}

	@Override
	public String rejectDeleteRequest(int requestId) {
		List<DeleteRequest> deleteRequests = new ArrayList<DeleteRequest>();
		DeleteRequest deleteRequest = new DeleteRequest();
		deleteRequests = this.getAllDeleteRequests();
		int flag = 0;
		for (DeleteRequest req : deleteRequests) {
			if (req.getRequestId() == requestId) {
				flag = 1;
				deleteRequest = req;
			}
		}
		if (flag == 0)
			throw new InventoryException("Invalid Request");
		try {
			deleteRepo.delete(deleteRequest);
		} catch (Exception e) {
			throw new InventoryException("Something went wrong");
		}
		return "Delete request rejected successfully";
	}

}
