package com.cars.autoflex.controller;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cars.autoflex.helpers.Message;
import com.cars.autoflex.model.Car;
import com.cars.autoflex.model.User;
import com.cars.autoflex.service.BookingService;
import com.cars.autoflex.service.CarService;
import com.cars.autoflex.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	UserService userService;
	CarService carService;
	BookingService bookingService;

	public UserController(UserService userService, CarService carService, BookingService bookingService) {
		super();
		this.userService = userService;
		this.carService = carService;
		this.bookingService = bookingService;
	}

//	------------------ HOME PAGE -------------------
	@GetMapping("/")
	public String homePage(HttpSession session) {
		session.invalidate();
		return "index";
	}

//	------------------ SIGN UP PAGE -------------------
	@GetMapping("/register")
    public String registerPage(Model model, HttpSession session) {
		session.invalidate();
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }


	public static void uploadImage(MultipartFile image) {
	
	    try {
	        File saveFile = new ClassPathResource("static/images/users").getFile();
	        Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + image.getOriginalFilename());
	
	        Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


    @PostMapping("/saveuser")
    public String newUser(@ModelAttribute("user") User user, @RequestParam MultipartFile image, HttpSession session) {
        
    	// check if user already exists
    	User isUserExist=userService.findByEmail(user.getEmail());
    	
    	if(isUserExist != null) {
    		session.setAttribute("message", new Message("warning", "User already exist!! Please sign in to continue."));
            return "register";
    	}
    	
    	// save image name to user (db)
    	user.setImageName(image.getOriginalFilename());

    	// save image in folder
        UserController.uploadImage(image);

        // save user to db
        User savedUser = userService.saveUser(user);

        if (savedUser != null) {
            session.setAttribute("message", new Message("success", "Registration successful!! Log In to continue"));
            return "redirect:/login";
        }

        session.setAttribute("message", new Message("danger", "Registration failed!! Try again"));
        return "register";
    }
	
    

//	------------------ LOGIN PAGE -------------------
    
	@GetMapping("/login")
	public String loginPage(Model model, HttpSession session) {
		User userLogin= new User();
		model.addAttribute("userLogin", userLogin);
		return "login";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
	    session.invalidate();
	    return "redirect:/";
	}

	
	@PostMapping("/checkuser")
	public String checkUser(@ModelAttribute("userLogin") User user, HttpSession session, Model model) {
		
		User checkUserData=userService.findByEmail(user.getEmail());
		
		if(checkUserData != null && user.getPassword().equals(checkUserData.getPassword())) {
			session.setAttribute("userData", checkUserData);
			return checkUserData.getRole().equals("customer") ? "redirect:/customer/customerHome/"+checkUserData.getUserId() : "redirect:/owner/ownerHome/"+checkUserData.getUserId();
		}
		session.setAttribute("message", new Message("danger", "Invalid Credentials !! Try again."));
		return "login";
	}
	
	

//	------------------ CUSTOMER HOME PAGE -------------------
	
	@GetMapping("/customer/customerHome/{id}")	
	public String customer(@PathVariable Long id, Model model, HttpSession session) {
		try {
			User userData=(User) session.getAttribute("userData");
			
		    if (userData != null && userData.getRole().equals("customer") && userData.getUserId().equals(id)) {
		        model.addAttribute("availableCars", carService.getCarsByStatus("available"));
		        return "customer/customerHome";
		    } else {
		        return "redirect:/login";
		    }
		}
		catch(Exception e) {
			session.setAttribute("message", new Message("warning", "Session Timed Out!!"));
			return "redirect:/login";
		}
	}	
	

//	------------------ CUSTOMER PROFILE PAGE -------------------
	@GetMapping("/customer/profile/{id}")
	public String customerProfile(@PathVariable Long id, HttpSession session) {
		try {
			return "customer/profile";			
		}
		catch(Exception e) {
			session.setAttribute("message", new Message("warning", "Session Timed Out!!"));
			return "redirect:/login";
		}
	}
	
//	------------------ OWNER HOME PAGE -------------------
	@GetMapping("/owner/ownerHome/{userId}")
	public String owner(@PathVariable Long userId, HttpSession session, Model model) {
		try {
		    User userData = (User) session.getAttribute("userData");
		    if (userData != null && userData.getRole().equals("owner") && userData.getUserId().equals(userId)) {
		        model.addAttribute("bookings", bookingService.findByOwner(userId));
		        return "owner/ownerHome";
		    } else {
		        return "redirect:/login";
		    }
		}
		catch(Exception e) {
			session.setAttribute("message", new Message("warning", "Session Timed Out!!"));
			return "redirect:/login";
		}
	}	

//	------------------ OWNED CARS PAGE -------------------
	@GetMapping("/owner/ownedCars/{userId}")
	public String ownedCars(@PathVariable Long userId, Model model,HttpSession session) {
		try {
		    User user = userService.findById(userId); 
		    List<Car> userCars = carService.getCarsByOwner(user);

		        model.addAttribute("newCar", new Car());
		        model.addAttribute("cars", userCars);

		    return "owner/ownedCars";
		}
		catch(Exception e) {
			session.setAttribute("message", new Message("warning", "Session Timed Out!!"));
			return "redirect:/login";
		}
	}


//	------------------ OWNER PROFILE PAGE -------------------
	@GetMapping("/owner/profile/{id}")
	public String ownerProfile(@PathVariable Long id, HttpSession session) {
		try {
			return "owner/profile";			
		}
		catch(Exception e) {
			session.setAttribute("message", new Message("warning", "Session Timed Out!!"));
			return "redirect:/login";
		}
	}
	

}
