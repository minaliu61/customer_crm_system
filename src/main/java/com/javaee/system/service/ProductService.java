package com.javaee.system.service;

import com.javaee.system.po.Product;
import com.github.pagehelper.PageInfo;
import java.util.List;
public interface ProductService {
    Product getById(Long productId);
    Product getByProductNumber(String productNumber);
    List<Product> listAll();
    PageInfo<Product> listByPage(Product product, Integer pageNum, Integer pageSize);
    List<Product> listByCategory(String category);
    int add(Product product);
    int update(Product product);
    int delete(Long productId);
    int deleteBatch(List<Long> ids);
    int count();
    int updateStock(Long productId, Integer quantity);
} 