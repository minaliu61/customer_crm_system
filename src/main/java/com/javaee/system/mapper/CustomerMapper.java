package com.javaee.system.mapper;
import com.javaee.system.po.Customer;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;
public interface CustomerMapper {
    Customer selectById(Long customerId);
    Customer selectByUserId(Long userId);
    List<Customer> selectAll();
    List<Customer> selectByCondition(Customer customer);
    List<Customer> selectByIds(@Param("ids") List<Long> ids);
    int insert(Customer customer);
    int update(Customer customer);
    int deleteById(Long customerId);
    int deleteBatch(@Param("ids") List<Long> ids);
    int count();
    int countBySalesperson(Long salespersonId);
    List<Map<String, Object>> selectCustomersBySalespersonUserId(Map<String, Object> params);
    long countCustomersBySalespersonUserId(Map<String, Object> params);
} 