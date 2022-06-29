package com.tmcoder.inventory.service;

import java.util.List;

import com.tmcoder.inventory.entity.Inventory;
import com.tmcoder.inventory.entity.UpdateRequest;

public interface UpdateService {

	List<UpdateRequest> getAllUpdateRequests();

	String updateInventoryRequest(Inventory inventory);

	String approveUpdateRequest(int requestId);

	String rejectUpdateRequest(int requestId);

}
