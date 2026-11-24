package com.javaee.system.service;

import com.javaee.system.po.ProductCategory;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CategoryService {
     PageInfo<ProductCategory> listByPage(ProductCategory category, Integer pageNum, Integer pageSize);
    List<ProductCategory> listAll();
     ProductCategory getById(Long categoryId);
    boolean add(ProductCategory category);
    boolean update(ProductCategory category);
     boolean delete(Long categoryId);
} 