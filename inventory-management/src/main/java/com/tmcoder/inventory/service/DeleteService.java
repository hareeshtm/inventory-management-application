package com.tmcoder.inventory.service;

import java.util.List;

import com.tmcoder.inventory.entity.DeleteRequest;
import com.tmcoder.inventory.entity.Inventory;

public interface DeleteService {

	List<DeleteRequest> getAllDeleteRequests();

	String deleteInventoryRequest(Inventory inventory);

	String approveDeleteRequest(int requestId);

	String rejectDeleteRequest(int requestId);

}
