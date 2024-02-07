package com.cars.autoflex.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cars.autoflex.model.Booking;
import com.cars.autoflex.model.Car;
import com.cars.autoflex.model.User;
import com.cars.autoflex.service.CarService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CarController {

	CarService carService;
	
	@Autowired
	public CarController(CarService carService) {
		super();
		this.carService = carService;
	}

//	------------------ NEW CAR PAGE -------------------

	@GetMapping("/owner/addCar")
	public String addCar( Model model, HttpSession session) {
	    Car car = new Car();
	    model.addAttribute("newCar", car);
	    return "owner/newCar";
	}
	
//	------------------ ADD CAR  -------------------
	public static void uploadImage(MultipartFile image) {

		try {
			File saveFile=new ClassPathResource("static/images/cars").getFile();
	        Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+image.getOriginalFilename());
	        
	        System.out.println(path);
	        Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
	}
	
	
	@PostMapping("/savecar")
	public String saveCar(@ModelAttribute("newCar") Car car,@RequestParam MultipartFile image, HttpSession session) {

		User user = (User) session.getAttribute("userData");
	    System.out.println(user);
	    if (user != null) {
	        try {
	    		car.setImageName(image.getOriginalFilename());
	        	car.setOwner(user);
		    	car.setStatus("available");
		        carService.addCar(car);

				CarController.uploadImage(image);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
	        
	        return "redirect:/owner/ownedCars/" + user.getUserId();
	    } else {
	        return "redirect:/login";
	    }
	}

	
//	------------------ UPDATE CAR DETAILS -------------------
	
	@PostMapping("/owner/ownedCars/editCar/{id}")
	public String editCar(@PathVariable Long id, @ModelAttribute Car car, @RequestParam MultipartFile image, Model model) {

	    Car existingCar = carService.getCar(id);

	    try {
	        existingCar.setCarName(car.getCarName());
	        existingCar.setCarModel(car.getCarModel());
	        existingCar.setCity(car.getCity());
	        existingCar.setRatePerDay(car.getRatePerDay());
	        existingCar.setSeats(car.getSeats());
	        existingCar.setRegistrationNumber(car.getRegistrationNumber());
	        existingCar.setDescription(car.getDescription());

	        if (image.getOriginalFilename() != null && !image.getOriginalFilename().isEmpty()) {
	        	existingCar.setImageName(image.getOriginalFilename());
	            CarController.uploadImage(image);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    carService.addCar(existingCar);

	    return "redirect:/owner/ownedCars/carDetails/" + existingCar.getCarId();
	}


//	------------------ DELETE CAR -------------------
	
	@GetMapping("/owner/ownedCars/deleteCar/{id}")
	public String deleteCar(@PathVariable Long id, HttpSession session) {
		User user= (User) session.getAttribute("userData");
		carService.deleteCar(id);
		return "redirect:/owner/ownedCars/"+user.getUserId();
	}
	

//	------------------ CUSTOMER CAR DETAILS PAGE -------------------
	
	@GetMapping("/customer/car/{id}")
	public String carDeatilsCust(@PathVariable Long id, Model model, HttpSession session) {
		model.addAttribute("carData", carService.getCar(id));
		model.addAttribute("newBooking", new Booking());
		return "customer/carDetails";
	}
	
//	------------------ OWNER CAR DETAILS PAGE -------------------
	
	@GetMapping("/owner/ownedCars/carDetails/{id}")
	public String carDeatils(@PathVariable Long id, Model model, HttpSession session) {
		model.addAttribute("carData", carService.getCar(id));
		return "owner/carDetails";
	}

	
//  ------------------ SEARCH BY CITY -------------------
	
	@GetMapping("/customer/customerHome/search")
	public String carBycity(@RequestParam String city ,Model model, HttpSession session) {
		
		User userData=(User) session.getAttribute("userData");
		
	    if (userData != null) {
	        model.addAttribute("userData", userData);
	        model.addAttribute("availableCars", carService.getCarsByCityAndStatus(city,"available"));
	        return "customer/customerHome";
	    } else {
	        return "redirect:/login";
	    }
	}
	
}
