package com.javaee.system.service.impl;
import com.javaee.system.po.Product;
import com.javaee.system.mapper.ProductMapper;
import com.javaee.system.service.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Override
    public Product getById(Long productId) {
        return productMapper.selectById(productId);
    }
    @Override
    public Product getByProductNumber(String productNumber) {
        return productMapper.selectByProductNumber(productNumber);
    }
    @Override
    public List<Product> listAll() {
        return productMapper.selectAll();
    }
    @Override
    public PageInfo<Product> listByPage(Product product, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> list = productMapper.selectByCondition(product);
        return new PageInfo<>(list);
    }
    @Override
    public List<Product> listByCategory(String category) {
        return productMapper.selectByCategory(category);
    }
    @Override
    @Transactional
    public int add(Product product) {
        return productMapper.insert(product);
    }
    @Override
    @Transactional
    public int update(Product product) {
        return productMapper.update(product);
    }
    @Override
    @Transactional
    public int delete(Long productId) {
        return productMapper.deleteById(productId);
    }
    @Override
    @Transactional
    public int deleteBatch(List<Long> ids) {
        return productMapper.deleteBatch(ids);
    }
    @Override
    public int count() {
        return productMapper.count();
    }
    @Override
    @Transactional
    public int updateStock(Long productId, Integer quantity) {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            return 0;
        }
        if (quantity < 0 && product.getStock() < Math.abs(quantity)) {
            throw new RuntimeException("产品库存不足");
        }
        return productMapper.updateStock(productId, quantity);
    }
} 