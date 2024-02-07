package com.cars.autoflex.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cars.autoflex.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	User findByEmail(String email);
}
