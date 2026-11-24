package com.javaee.system.mapper;

import com.javaee.system.po.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    OrderItem selectById(Long itemId);
    List<OrderItem> selectByOrderId(Long orderId);
    List<OrderItem> selectByProductId(Long productId);
    int insert(OrderItem orderItem);
    int batchInsert(@Param("orderItems") List<OrderItem> orderItems);
    int update(OrderItem orderItem);
    int deleteById(Long itemId);
    int deleteByOrderId(Long orderId);
} 