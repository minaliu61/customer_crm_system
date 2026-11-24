package com.javaee.system.mapper;

import com.javaee.system.po.ProductCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {
    ProductCategory selectById(@Param("categoryId") Long categoryId);
    List<ProductCategory> selectByCondition(ProductCategory category);
    List<ProductCategory> selectAll();
    int insert(ProductCategory category);
    int update(ProductCategory category);
    int deleteById(@Param("categoryId") Long categoryId);
} 