package com.tmcoder.inventory.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmcoder.inventory.dto.InventoryDto;
import com.tmcoder.inventory.entity.DeleteRequest;
import com.tmcoder.inventory.entity.Inventory;
import com.tmcoder.inventory.entity.UpdateRequest;
import com.tmcoder.inventory.exception.InventoryException;
import com.tmcoder.inventory.repository.DeleteRequestRepository;
import com.tmcoder.inventory.repository.InventoryRepository;
import com.tmcoder.inventory.repository.UpdateRepository;
import com.tmcoder.inventory.service.InventoryService;

@Service
public class InventoryServiceImpl implements InventoryService {

	@Autowired
	private InventoryRepository inventoryRepo;
	@Autowired
	private DeleteRequestRepository deleteRepo;
	@Autowired
	private UpdateRepository updateRepo;
	@Autowired
	private DeleteServiceImpl deleteService;
	@Autowired
	private UpdateServiceImpl updateService;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public String addInventory(InventoryDto inventory) {

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

		try {
			Inventory inventoryData = modelMapper.map(inventory, Inventory.class);
			inventoryRepo.save(inventoryData);
			if (inventory.getStatus().equals("Approved"))
				return "Inventory Added Successfully(Status:Approved)";
			else
				return "Inventory Added Successfully(Status:Pending)";
		} catch (Exception e) {
			throw new InventoryException("Not able to add inventory");
		}

	}

	@Override
	public String addPendingInventory(Inventory inventory) {

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

		inventory.setStatus("Pending");
		try {
			inventoryRepo.save(inventory);
			return "Inventory Added Successfully(Status:Pending)";
		} catch (Exception e) {
			throw new InventoryException("Not able to add inventory");
		}

	}

	@Override
	public String deleteInventory(int productId) {

		Inventory inventory = new Inventory();
		List<DeleteRequest> deleteRequestList = deleteRepo.findAll();
		for (DeleteRequest req : deleteRequestList) {
			if (req.getInventory().getProductId() == productId) {
				deleteRepo.deleteById(req.getRequestId());
				break;
			}
		}

		List<UpdateRequest> updateRequestList = updateRepo.findAll();
		for (UpdateRequest req : updateRequestList) {
			if (req.getProductId() == productId) {
				updateRepo.deleteById(req.getRequestId());
				break;
			}
		}

		try {
			inventory = inventoryRepo.getOne(productId);
			inventoryRepo.delete(inventory);
			return "Inventory deleted successfully";
		} catch (Exception e) {
			throw new InventoryException("Not able to delete inventory");
		}
	}

	@Override
	public String changeInventory(InventoryDto inventory, int productId) {
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

		if (!(inventory.getStatus().equals("Pending") || inventory.getStatus().equals("Approved")))
			throw new InventoryException("Enter valid inventory status(Pending/Approved)");

		Inventory inventoryDetails = new Inventory();
		try {
			inventoryDetails = inventoryRepo.getOne(productId);
		} catch (Exception e) {
			throw new InventoryException("No such inventory is present");
		}
		inventoryDetails.setProductName(inventory.getProductName());
		inventoryDetails.setVendor(inventory.getVendor());
		inventoryDetails.setMrp(inventory.getMrp());
		inventoryDetails.setBatchNum(inventory.getBatchNum());
		inventoryDetails.setBatchDate(inventory.getBatchDate());
		inventoryDetails.setQuantity(inventory.getQuantity());
		inventoryDetails.setStatus(inventory.getStatus());

		try {
			inventoryRepo.save(inventoryDetails);
		} catch (Exception e) {
			throw new InventoryException("Not able to update the details");
		}
		return "Updated Successfully";
	}

	@Override
	public List<InventoryDto> getAllInventory() {
		List<InventoryDto> inventoryList = new ArrayList<InventoryDto>();
		inventoryList = inventoryRepo.findAll().stream()
				.map(inventory -> modelMapper.map(inventory, InventoryDto.class)).collect(Collectors.toList());
		if (inventoryList.isEmpty()) {
			throw new InventoryException("Empty inventory list");
		}
		return inventoryList;
	}

	@Override
	public List<InventoryDto> getPendingInventory() {
		List<InventoryDto> inventoryList = new ArrayList<InventoryDto>();
		List<InventoryDto> pendingInventoryList = new ArrayList<InventoryDto>();
		inventoryList = this.getAllInventory();
		for (InventoryDto inventory : inventoryList) {
			if (inventory.getStatus().equals("Pending"))
				pendingInventoryList.add(inventory);
		}
		if (pendingInventoryList.isEmpty())
			throw new InventoryException("No Pending Inventory");
		return pendingInventoryList;
	}

	@Override
	public List<InventoryDto> getApprovedInventory() {
		List<InventoryDto> inventoryList = new ArrayList<InventoryDto>();
		List<InventoryDto> approvedInventoryList = new ArrayList<InventoryDto>();
		inventoryList = this.getAllInventory();
		for (InventoryDto inventory : inventoryList) {
			if (inventory.getStatus().equals("Approved"))
				approvedInventoryList.add(inventory);
		}
		if (approvedInventoryList.isEmpty())
			throw new InventoryException("No Pending Inventory");
		return approvedInventoryList;

	}

	@Override
	public String approvePendingInventory(int productId) {

		Inventory inventory = new Inventory();
		inventory = inventoryRepo.getOne(productId);
		if (inventory.getStatus().equals("Approved"))
			throw new InventoryException("Already Approved Item");
		try {
			inventory.setStatus("Approved");
			inventoryRepo.save(inventory);
			return "Status changed from Pending to Approved";
		} catch (Exception e) {
			throw new InventoryException("Sorry something went wrong");
		}
	}

	@Override
	public InventoryDto getSingleInventory(int productId) {

		InventoryDto inventory = new InventoryDto();
		List<InventoryDto> inventoryList = this.getAllInventory();
		int flag = 0;
		for (InventoryDto inv : inventoryList) {
			if (inv.getProductId() == productId) {
				inventory = inv;
				flag = 1;
			}
		}
		if (flag == 0) {
			throw new InventoryException("No Such Product is present");
		}
		return inventory;
	}

	@Override
	public List<InventoryDto> getPendingOrApprovedInventory(int choiceValue) {
		if (choiceValue == 1)
			return this.getApprovedInventory();
		else
			return this.getPendingInventory();
	}

	@Override
	public String deleteOrUpdateInventoryRequest(InventoryDto inventory, int choiceValue) {

		Inventory inv=new Inventory();
		inv=modelMapper.map(inventory, Inventory.class);
		if(choiceValue==1)
			return deleteService.deleteInventoryRequest(inv);
		else
			return updateService.updateInventoryRequest(inv);
	}

	@Override
	public String approveDeleteOrUpdateRequest(int choiceValue, int requestId) {
		if(choiceValue==1)
			return deleteService.approveDeleteRequest(requestId);
		else
			return updateService.approveUpdateRequest(requestId);
	}

	@Override
	public String rejectDeleteOrUpdateRequest(int choiceValue, int requestId) {
		
		if(choiceValue==1)
			return deleteService.rejectDeleteRequest(requestId);
		else
			return updateService.rejectUpdateRequest(requestId);
	}

}
