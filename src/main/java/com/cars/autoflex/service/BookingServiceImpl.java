package com.cars.autoflex.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cars.autoflex.model.Booking;
import com.cars.autoflex.model.Car;
import com.cars.autoflex.model.User;
import com.cars.autoflex.repository.BookingRepository;

@Service
public class BookingServiceImpl implements BookingService{

	BookingRepository bookingRepository;

	@Autowired
	public BookingServiceImpl(BookingRepository bookingRepository) {
		super();
		this.bookingRepository = bookingRepository;
	}

	@Override
	public Booking saveBooking(Booking booking) {
		return bookingRepository.save(booking);
	}

	@Override
	public List<Booking> getBookings() {
		return bookingRepository.findAll();
	}

	@Override
	public List<Booking> getBookingByCustomer(User customer) {
		return bookingRepository.findByCustomer(customer);
	}

	@Override
	public void deleteBooking(Long id) {
		bookingRepository.deleteById(id);
	}

	@Override
	public Car findByCar(Car car) {
		return bookingRepository.findByCar(car);
	}

	@Override
	public Booking getBookingById(Long id) {
		return bookingRepository.findById(id).get();
	}

	@Override
	public List<Booking> findByOwner(Long id) {
		return bookingRepository.findByOwnerId(id);
	}
	
	
}
