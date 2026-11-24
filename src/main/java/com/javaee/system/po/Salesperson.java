package com.javaee.system.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.util.Date;

/**
 * 销售人员实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salesperson implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long salespersonId;
    private Long userId;
    private String employeeId;
    private String department;
    private String position;
    private String phone;
    private String email;
    private Date hireDate;
    private String salesArea;
    private Double salesTarget;
    private Double salesPerformance;
    private String remark;

    private User user;
} 