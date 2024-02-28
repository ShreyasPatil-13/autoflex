package com.cars.autoflex.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "car_id",referencedColumnName = "carId")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "userId")
    private User customer;

    private Long ownerId;
    private double totalAmount;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private int days;
    private LocalDate bookingDate;
    private String status;
    
    
}
