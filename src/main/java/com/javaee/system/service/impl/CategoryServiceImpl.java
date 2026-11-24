package com.javaee.system.service.impl;

import com.javaee.system.po.ProductCategory;
import com.javaee.system.mapper.CategoryMapper;
import com.javaee.system.service.CategoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public PageInfo<ProductCategory> listByPage(ProductCategory category, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ProductCategory> list = categoryMapper.selectByCondition(category);
        return new PageInfo<>(list);
    }

    @Override
    public List<ProductCategory> listAll() {
        return categoryMapper.selectAll();
    }

    @Override
    public ProductCategory getById(Long categoryId) {
        return categoryMapper.selectById(categoryId);
    }

    @Override
    public boolean add(ProductCategory category) {
        // 设置创建时间和更新时间
        Date now = new Date();
        category.setCreateTime(now);
        category.setUpdateTime(now);
        
        // 如果状态为空，默认启用
        if (category.getStatus() == null) {
            category.setStatus(1);
        }
        
        return categoryMapper.insert(category) > 0;
    }

    @Override
    public boolean update(ProductCategory category) {
        // 设置更新时间
        category.setUpdateTime(new Date());
        return categoryMapper.update(category) > 0;
    }

    @Override
    public boolean delete(Long categoryId) {
        return categoryMapper.deleteById(categoryId) > 0;
    }
} 