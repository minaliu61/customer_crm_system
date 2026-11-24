package com.javaee.system.po;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long productId;
    private String productName;
    private String productNumber;
    private String category;
    private String model;
    private String description;
    private BigDecimal price;
    private BigDecimal costPrice;
    private Integer stock;
    private Integer sales;
    private String unit;
    private String supplier;
    private Integer status;
    private String imagePath;
    private String remark;
    private Date createTime;
    private Date updateTime;
    private String categoryName;
    private Double avgRating;
    private Integer commentCount;
}