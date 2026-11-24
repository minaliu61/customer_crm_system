package com.javaee.system.po;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class ProductCategory {
    private Long categoryId;
    private String categoryName;
    private String description;
    private Long parentId;
    private Integer status; // 0-禁用，1-启用
    private Date createTime;
    private Date updateTime;
    
    // 非数据库字段
    private List<ProductCategory> children;
    private String parentName;
} 