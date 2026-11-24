package com.javaee.system.po;
import lombok.Data;
@Data
public class Customer {
    private Long customerId;
    private Long userId;
    private String company;
    private String industry;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private User user;
} 