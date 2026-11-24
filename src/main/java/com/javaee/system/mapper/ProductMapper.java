package com.javaee.system.mapper;
import com.javaee.system.po.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface ProductMapper {
    Product selectById(Long productId);
    Product selectByProductNumber(String productNumber);
    List<Product> selectAll();
    List<Product> selectByCondition(Product product);
    List<Product> selectByCategory(String category);
    List<Product> selectByIds(@Param("ids") List<Long> ids);
    int insert(Product product);
    int update(Product product);
    int deleteById(Long productId);
    int deleteBatch(@Param("ids") List<Long> ids);
    int count();
    int updateStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);
} 