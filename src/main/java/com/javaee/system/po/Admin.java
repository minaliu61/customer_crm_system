package com.javaee.system.po;

import lombok.Data;

@Data
public class Admin {
    private Long adminId;
    private Long userId;
    private String position;
    private String department;
    private User user;

}