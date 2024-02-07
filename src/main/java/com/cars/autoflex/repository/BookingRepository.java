package com.cars.autoflex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cars.autoflex.model.Booking;
import com.cars.autoflex.model.Car;
import com.cars.autoflex.model.User;

public interface BookingRepository extends JpaRepository<Booking, Long>{
	List<Booking> findByCustomer(User customer);
	Car findByCar(Car car);
	List<Booking> findByOwnerId(Long id);

}
