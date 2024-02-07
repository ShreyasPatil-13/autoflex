package com.cars.autoflex.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String city;
    private String contactNumber;

    private String imageName;
    private String role; // (Customer/Owner)
}
