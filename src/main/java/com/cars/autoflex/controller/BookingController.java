package com.cars.autoflex.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.cars.autoflex.helpers.Message;
import com.cars.autoflex.model.Booking;
import com.cars.autoflex.model.Car;
import com.cars.autoflex.model.User;
import com.cars.autoflex.service.BookingService;
import com.cars.autoflex.service.CarService;
import com.cars.autoflex.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BookingController {

	BookingService bookingService;
	CarService carService;
	UserService userService;

	public BookingController(BookingService bookingService, CarService carService, UserService userService) {
		super();
		this.bookingService = bookingService;
		this.carService = carService;
		this.userService = userService;
	}

	//save booking
	@PostMapping("/car/book/{id}")
	public String bookCar(@PathVariable Long id, @ModelAttribute("newBooking") Booking booking, HttpSession session) {
		try {
			Car car= carService.getCar(id);
			User user= (User) session.getAttribute("userData");
			booking.setCar(car);
			booking.setCustomer(user);
			booking.setStatus("booked");
			booking.setOwnerId(car.getOwner().getUserId());
			car.setStatus("booked");
			carService.addCar(car);
		    booking.setBookingDate(LocalDate.now());
			
			bookingService.saveBooking(booking);
			
			return "redirect:/customer/customerHome/"+user.getUserId();
			
		}
		catch(Exception e) {
			session.setAttribute("message", new Message("warning", "Session Timed Out!!"));
			return "redirect:/login";
		}
	}
	
	//get booking 
	@GetMapping("/customer/bookings/{id}")
	public String bookingsList(@PathVariable Long id, Model model,HttpSession session) {

		try {
			User user= userService.findById(id);
			
			model.addAttribute("bookingData", bookingService.getBookingByCustomer(user));
			
			return "customer/bookingList";
			
		}
		catch(Exception e) {
			session.setAttribute("message", new Message("warning", "Session Timed Out!!"));
			return "redirect:/login";
		}

	}
	
	
	//cancel booking
	@GetMapping("/customer/booking/cancel/{id}")
	public String cancelBooking(@PathVariable Long id, HttpSession session) {
		try {
			Booking b=bookingService.getBookingById(id);

			Long carId=b.getCar().getCarId();
			Car car=carService.getCar(carId);

			car.setStatus("available");
			carService.addCar(car);
			
			bookingService.deleteBooking(id);
			
			User user= (User) session.getAttribute("userData");
			return "redirect:/customer/bookings/"+user.getUserId();			
		}
		catch(Exception e) {
			session.setAttribute("message", new Message("warning", "Session Timed Out!!"));
			return "redirect:/login";
		}

	}
	
	//mark as done booking
	@GetMapping("/booking/done/{id}")
	public String deleteBooking(@PathVariable Long id, HttpSession session) {
		try {
			Booking booking= bookingService.getBookingById(id);
			Car car= booking.getCar();
			
			bookingService.deleteBooking(id);
			
			car.setStatus("available");
			carService.addCar(car);
			return "redirect:/owner/ownerHome/"+booking.getOwnerId();
		}
		catch(Exception e) {
			session.setAttribute("message", new Message("warning", "Session Timed Out!!"));
			return "redirect:/login";
		}
	}

}
