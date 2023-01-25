package com.driver.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int driverId;


    private String mobile;
    private String password;



    public int getDriverId() {
        return driverId;
    }



    public Driver() {
    }


    @OneToOne
    @JoinColumn
    private Cab cab;

    @OneToMany(mappedBy = "driver",cascade = CascadeType.ALL)
    private List<TripBooking> tripBookingList  = new ArrayList<>();

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Cab getCab() {
        return cab;
    }

    public void setCab(Cab cab) {
        this.cab = cab;
    }

    public List<TripBooking> getTripBookingList() {
        return tripBookingList;
    }

    public void setTripBookingList(List<TripBooking> tripBookingList) {
        this.tripBookingList = tripBookingList;
    }

    public Driver(int id, String mobile, String password) {
        this.driverId = id;
        this.mobile = mobile;
        this.password = password;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public Driver(int id, String mobile, String password, Cab cab) {
        this.driverId = id;
        this.mobile = mobile;
        this.password = password;
        this.cab = cab;
    }

    public Driver(String mobile, String password) {

        this.mobile = mobile;
        this.password = password;
    }

}
