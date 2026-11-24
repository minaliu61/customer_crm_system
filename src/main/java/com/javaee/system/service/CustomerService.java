package com.javaee.system.service;
import com.javaee.system.po.Customer;
import com.github.pagehelper.PageInfo;
import java.util.List;
import java.util.Map;
public interface CustomerService {
    Customer getById(Long customerId);
    Customer getByUserId(Long userId);
    List<Customer> listAll();
    PageInfo<Customer> listByPage(Customer customer, Integer pageNum, Integer pageSize);
    int add(Customer customer);
    int update(Customer customer);
    int delete(Long customerId);
    int deleteBatch(List<Long> ids);
    int count();
     int countBySalesperson(Long salespersonId);
    List<Map<String, Object>> getCustomersBySalespersonUserId(Long salespersonUserId, String searchText, String industry, Integer pageNum, Integer pageSize);
    long countCustomersBySalespersonUserId(Long salespersonUserId, String searchText, String industry);
}