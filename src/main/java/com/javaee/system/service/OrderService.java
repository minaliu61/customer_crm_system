package com.javaee.system.service;
import com.javaee.system.po.Order;
import com.github.pagehelper.PageInfo;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
public interface OrderService {
    Order getById(Long orderId);
    Order getByOrderNumber(String orderNumber);
    int create(Order order);
    int update(Order order);
    int delete(Long orderId);
    List<Order> listByCustomerId(Long customerId);
    List<Order> listBySalespersonId(Long salespersonId);
    PageInfo<Order> listByPage(Order order, Integer pageNum, Integer pageSize);
    int updateStatus(Long orderId, Integer status);
     int updatePayStatus(Long orderId, Integer payStatus);
    Map<String, Object> statistics(Map<String, Object> params);
    List<Map<String, Object>> getSalesTrend(Long salespersonId, String period);
    List<Map<String, Object>> getProductRatio(Long salespersonId);
    List<Map<String, Object>> getCustomerRanking(Long salespersonId, Date startDate, Date endDate, Integer limit);
    List<Map<String, Object>> getProductRanking(Long salespersonId, Date startDate, Date endDate, Integer limit);
    List<Map<String, Object>> getSalesRecords(Map<String, Object> params);
    void exportSalesData(Map<String, Object> params, OutputStream outputStream) throws Exception;
} 