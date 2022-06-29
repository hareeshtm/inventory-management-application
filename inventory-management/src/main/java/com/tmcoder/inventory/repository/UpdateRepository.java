package com.tmcoder.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tmcoder.inventory.entity.UpdateRequest;

public interface UpdateRepository extends JpaRepository<UpdateRequest, Integer> {

}
