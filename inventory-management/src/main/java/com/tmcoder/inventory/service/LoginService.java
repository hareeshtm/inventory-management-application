package com.tmcoder.inventory.service;

import com.tmcoder.inventory.dto.UserDto;
import com.tmcoder.inventory.entity.User;

public interface LoginService {

	UserDto validateUsernameAndPwd(UserDto user);
	String insertUser(User user);

}
