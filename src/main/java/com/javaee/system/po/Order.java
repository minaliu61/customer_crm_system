package com.javaee.system.po;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long orderId;
    private String orderNumber;
    private Long customerId;
    private String customerName;
    private Long salespersonId;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private Integer status;
    private Integer payStatus;
    private String payMethod;
    private Date payTime;
    private Date shipTime;
    private Date completeTime;
    private String address;
    private String phone;
    private String contactPerson;
    private String remark;
    private Date createTime;
    private Date updateTime;
    private Customer customer;
    private Salesperson salesperson;
    private List<OrderItem> orderItems;
    private Long userId;
} 