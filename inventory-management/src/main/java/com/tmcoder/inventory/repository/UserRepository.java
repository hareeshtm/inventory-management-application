package com.tmcoder.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tmcoder.inventory.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

	@Query("select u from User u where u.userName=?1")
	User findUserByUserName(String userName);
}
