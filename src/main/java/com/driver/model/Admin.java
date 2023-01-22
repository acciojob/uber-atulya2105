package com.driver.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String password;


    private List<Customer> customerList = new ArrayList<>();
    private  List<Driver> driverList = new ArrayList<>();
    public Admin(String name, String password) {
        this.name = name;
        this.password = password;
    }
    public Admin(){

    }

    public void setPassword(String password) {
        this.password = password;
    }



    public List<Driver> getDriverList() {
        return driverList;
    }

    public void setDriverList(List<Driver> driverList) {
        this.driverList = driverList;
    }


    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }
}
