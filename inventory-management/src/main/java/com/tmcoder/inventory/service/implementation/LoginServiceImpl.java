package com.tmcoder.inventory.service.implementation;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmcoder.inventory.dto.UserDto;
import com.tmcoder.inventory.entity.User;
import com.tmcoder.inventory.exception.LoginException;
import com.tmcoder.inventory.repository.UserRepository;
import com.tmcoder.inventory.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private UserRepository userDao;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public UserDto validateUsernameAndPwd(UserDto user) {

		User userData = new User();
		if (user.getUserName() == null)
			throw new LoginException("Please enter the username");
		if (user.getPassword() == null)
			throw new LoginException("Please enter the password");

		Optional<User> op = Optional.of(userDao.findUserByUserName(user.getUserName()));
		if (op.isPresent())
			userData = op.get();
		else
			throw new LoginException("Invalid Credentials");

		if (!(userData.getPassword().equals(user.getPassword())))
			throw new LoginException("Invalid Passowrd");

		return modelMapper.map(userData, UserDto.class);
	}

	@Override
	public String insertUser(User user) {

		userDao.save(user);
		return "user added successfully";
	}

}
