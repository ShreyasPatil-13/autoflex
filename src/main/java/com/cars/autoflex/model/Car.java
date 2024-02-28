package com.cars.autoflex.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Car {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long carId;

	    private String carName;
	    private String carModel;
	    private double ratePerDay;
	    private String city;

	    @ManyToOne
	    @JoinColumn(name = "owner_id", referencedColumnName = "userId")
	    private User owner;

	    private int seats;
	    private String registrationNumber;

	    @Column(length = 2000)
	    private String description;
	    private String imageName;
	    
	    private String status; // (Available/Booked)
}
