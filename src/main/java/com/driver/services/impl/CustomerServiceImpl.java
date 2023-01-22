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
		Admin admin = new Admin();
		admin.getCustomerList().add(customer);
		adminRepository.save(admin);
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
		List<Driver> driverList = admin.getDriverList();
		TripBooking tripBooking = new TripBooking();
		boolean flag = false;
		for(Driver driver : driverList){
			if(driver.getCab().isBooked()==false){

				driver.getCab().setBooked(true);
				tripBooking.setCab(driver.getCab());
				tripBooking.setTripStatus(TripStatus.CONFIRMED);
				tripBooking.setFromLocation(fromLocation);
				tripBooking.setToLocation(toLocation);
				tripBooking.setDistance(distanceInKm);
				tripBooking.setDriver(driver);
				driverRepository2.save(driver);
				tripBookingRepository2.save(tripBooking);

				flag = true;
				break;
			}
		}

		if(flag == false){
			throw new Exception("No Cab available");
		}


		return tripBooking;
	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly

		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
		if(tripBooking != null){
			  tripBooking.setTripStatus(TripStatus.CANCELED);
			  Cab cab = tripBooking.getCab();
			  cab.setBooked(false);
		}

		tripBookingRepository2.save(tripBooking);

	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly

		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
		if(tripBooking != null){
			tripBooking.setTripStatus(TripStatus.COMPLETED);
			Cab cab = tripBooking.getCab();
			cab.setBooked(false);
		}

	}
}
