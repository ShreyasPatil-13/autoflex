package com.cars.autoflex.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cars.autoflex.model.Car;
import com.cars.autoflex.model.User;

public interface CarRepository extends JpaRepository<Car, Long>{
	List<Car> findByOwner(User owner);
	List<Car> findByStatus(String status);
	List<Car> findByCityAndStatus(String city, String status);
}
