package com.tmcoder.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tmcoder.inventory.entity.DeleteRequest;

public interface DeleteRequestRepository extends JpaRepository<DeleteRequest, Integer> {

}
