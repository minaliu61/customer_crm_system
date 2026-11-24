package com.javaee.system.mapper;

import com.javaee.system.po.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface OrderMapper {
    Order selectById(Long orderId);
    Order selectByOrderNumber(String orderNumber);
    List<Order> selectByCustomerId(Long customerId);
    List<Order> selectBySalespersonId(Long salespersonId);
    List<Order> selectByCondition(Order order);
    int insert(Order order);
    int update(Order order);
    int deleteById(Long orderId);
    Map<String, Object> statistics(Map<String, Object> params);
    List<Map<String, Object>> getSalesTrend(Map<String, Object> params);
    List<Map<String, Object>> getProductRatio(Long salespersonId);
    List<Map<String, Object>> getCustomerRanking(Map<String, Object> params);
    List<Map<String, Object>> getProductRanking(Map<String, Object> params);
    List<Map<String, Object>> getSalesRecords(Map<String, Object> params);
} 