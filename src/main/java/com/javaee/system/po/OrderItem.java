package com.javaee.system.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单明细实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 订单明细ID
     */
    private Long itemId;
    
    /**
     * 订单ID
     */
    private Long orderId;
    
    /**
     * 产品ID
     */
    private Long productId;
    
    /**
     * 产品名称
     */
    private String productName;
    
    /**
     * 产品编号
     */
    private String productNumber;
    
    /**
     * 产品单价
     */
    private BigDecimal productPrice;
    
    /**
     * 购买数量
     */
    private Integer quantity;
    
    /**
     * 小计金额
     */
    private BigDecimal subtotalAmount;
    
    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
    
    /**
     * 非数据库字段 - 产品信息
     */
    private Product product;
} 