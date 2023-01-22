package com.driver.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String mobileNo;
    private String password;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    List<TripBooking> tripBookingList = new ArrayList<>();
}
