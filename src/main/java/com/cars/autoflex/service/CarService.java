package com.cars.autoflex.service;

import java.util.List;

import com.cars.autoflex.model.Car;
import com.cars.autoflex.model.User;

public interface CarService {
	//save car
	Car addCar(Car car);
	
	//list of car
	List<Car> getCars();
	
	//single car
	Car getCar(Long carId);
	
	//list of cars by single owner
	List<Car> getCarsByOwner(User owner);
	
	//update
	Car updateCar(Car car);
	
	//delete
	void deleteCar(Long carId);
	
	//list of available cars
	List<Car> getCarsByStatus(String status);
	
	//search
	List<Car> getCarsByCityAndStatus(String city, String status);

}
