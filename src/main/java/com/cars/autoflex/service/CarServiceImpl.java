package com.cars.autoflex.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cars.autoflex.model.Car;
import com.cars.autoflex.model.User;
import com.cars.autoflex.repository.CarRepository;

@Service
public class CarServiceImpl implements CarService{

	private final CarRepository carRepository;
	UserService userService;

	@Autowired
	public CarServiceImpl(CarRepository carRepository) {
		super();
		this.carRepository = carRepository;
	}
	 
	@Override
	public Car addCar(Car car) {
		return carRepository.save(car);
	}

	@Override
	public List<Car> getCars() {
		List<Car> cars=carRepository.findAll();
		return cars;
	}

	@Override
	public Car getCar(Long id) {
		return carRepository.findById(id).get();
	}

	@Override
	public Car updateCar(Car car) {
		return carRepository.save(car);
	}

	@Override
	public void deleteCar(Long carId) {
		carRepository.deleteById(carId);
	}

	@Override
	public List<Car> getCarsByOwner(User owner) {
		return carRepository.findByOwner(owner);
	}

	@Override
	public List<Car> getCarsByStatus(String status) {
		return carRepository.findByStatus(status);
	}

	@Override
	public List<Car> getCarsByCityAndStatus(String city, String status) {
		return carRepository.findByCityAndStatus(city, status);
	}
	
	
}
