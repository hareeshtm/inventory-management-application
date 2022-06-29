package com.tmcoder.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tmcoder.inventory.dto.UserDto;
import com.tmcoder.inventory.service.LoginService;

@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private LoginService loginService;

	@PostMapping("/login")
	public ResponseEntity<UserDto> loginToAccount(@RequestBody UserDto user) {

		UserDto userData = new UserDto();
		userData = loginService.validateUsernameAndPwd(user);
		return ResponseEntity.status(HttpStatus.OK).body(userData);
	}
}
