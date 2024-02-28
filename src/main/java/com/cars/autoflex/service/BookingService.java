package com.cars.autoflex.service;

import java.util.List;

import com.cars.autoflex.model.Booking;
import com.cars.autoflex.model.Car;
import com.cars.autoflex.model.User;

public interface BookingService {
	Booking saveBooking(Booking booking);
	
	List<Booking> getBookings();
	
	List<Booking> getBookingByCustomer(User cutomer );
	
	Car findByCar(Car car);
	Booking getBookingById(Long id);
	
	List<Booking> findByOwner(Long id);
	
	void deleteBooking(Long id);
}
