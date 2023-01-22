package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.AdminRepository;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;

import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;
	@Autowired
	private AdminRepository adminRepository;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);

	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		Customer customer = customerRepository2.findById(customerId).get();
		if(customer != null){
			customerRepository2.deleteById(customerId);
		}

	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query

		Admin admin = new Admin();
		List<Driver> driverList = driverRepository2.findAll();
		driverList.sort((a,b)->a.getDriverId()-b.getDriverId());

		boolean flag = false;
		for(Driver driver : driverList){
			if(driver.getCab().isAvailable()==true){

				TripBooking booked = new TripBooking();
				driver.getCab().setAvailable(false);
				booked.setBill(driver.getCab().getPerKmRate()*distanceInKm);
				booked.setDistanceInKm(distanceInKm);
				booked.setToLocation(toLocation);
				booked.setFromLocation(fromLocation);

				Customer customer = customerRepository2.findById(customerId).get();
				booked.getCustomer().setCustomerId(customerId);
				booked.getDriver().setDriverIdId(driver.getDriverId());
				customer.getTripBookingList().add(booked);
				driver.getTripBookingList().add(booked);

				driverRepository2.save(driver);
				tripBookingRepository2.save(booked);

				return booked;
			}
		}


		throw new Exception("No Cab available");



	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly

		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
		if(tripBooking != null){
			  tripBooking.setTripStatus(TripStatus.CANCELED);
			  Cab cab = tripBooking.getDriver().getCab();
			  cab.setAvailable(true);
		}

		tripBookingRepository2.save(tripBooking);

	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly

		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
		if(tripBooking != null){
			tripBooking.setTripStatus(TripStatus.COMPLETED);
			Cab cab = tripBooking.getDriver().getCab();
			cab.setAvailable(true);
		}

	}
}
