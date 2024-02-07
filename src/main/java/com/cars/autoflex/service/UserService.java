package com.cars.autoflex.service;

import com.cars.autoflex.model.User;

public interface UserService {
	User saveUser(User user);
	User findByEmail(String email);
	User findById(Long id);
}
