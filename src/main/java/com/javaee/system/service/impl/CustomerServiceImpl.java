package com.javaee.system.service.impl;

import com.javaee.system.po.Customer;
import com.javaee.system.po.User;
import com.javaee.system.mapper.CustomerMapper;
import com.javaee.system.mapper.UserMapper;
import com.javaee.system.service.CustomerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public Customer getById(Long customerId) {
        Customer customer = customerMapper.selectById(customerId);
        if (customer != null) {
            customer.setUser(userMapper.selectById(customer.getUserId()));
        }
        return customer;
    }

    @Override
    public Customer getByUserId(Long userId) {
        Customer customer = customerMapper.selectByUserId(userId);
        if (customer != null) {
            customer.setUser(userMapper.selectById(userId));
        }
        return customer;
    }

    @Override
    public List<Customer> listAll() {
        List<Customer> customers = customerMapper.selectAll();
        // 查询关联的用户信息
        customers.forEach(customer -> {
            customer.setUser(userMapper.selectById(customer.getUserId()));
        });
        return customers;
    }

    @Override
    public PageInfo<Customer> listByPage(Customer customer, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Customer> list = customerMapper.selectByCondition(customer);
        // 查询关联的用户信息
        list.forEach(c -> {
            c.setUser(userMapper.selectById(c.getUserId()));
        });
        return new PageInfo<>(list);
    }

    @Override
    @Transactional
    public int add(Customer customer) {

        User user = customer.getUser();
        user.setUsername(user.getRealName());
        user.setRole(3);
        user.setPassword("$2a$10$IiVT3D.bJ4GcgJfIZfF2KOSvNHdqRFOLFZbHtZFZO1FRKAyqXjacO");
        user.setAvatar("/crm/default.png");
        user.setStatus(1);
        if(customer.getUser()!=null){
            userMapper.insert(user);
        }
        if(user.getUserId()!=null){
            customer.setUserId(user.getUserId());
        }


        return customerMapper.insert(customer);
    }

    @Override
    @Transactional
    public int update(Customer customer) {
        return customerMapper.update(customer);
    }

    @Override
    @Transactional
    public int delete(Long customerId) {
        // 先查询是否存在
        Customer customer = customerMapper.selectById(customerId);
        if (customer != null) {
            // 删除用户信息
            int i = customerMapper.deleteById(customerId);
            // 删除客户信息

            userMapper.deleteById(customer.getUserId());
            return i;
        }
        return 0;
    }

    @Override
    @Transactional
    public int deleteBatch(List<Long> ids) {
        // 先查询所有客户信息
        List<Customer> customers = customerMapper.selectByIds(ids);
        if (!customers.isEmpty()) {
            // 删除所有关联的用户
            List<Long> userIds = customers.stream().map(Customer::getUserId).collect(java.util.stream.Collectors.toList());
            userMapper.deleteBatch(userIds);
            // 删除客户信息
            return customerMapper.deleteBatch(ids);
        }
        return 0;
    }

    @Override
    public int count() {
        return customerMapper.count();
    }

    @Override
    public int countBySalesperson(Long salespersonId) {
        return customerMapper.countBySalesperson(salespersonId);
    }
    
    @Override
    public List<Map<String, Object>> getCustomersBySalespersonUserId(Long salespersonUserId, String searchText, String industry, Integer pageNum, Integer pageSize) {
        // 构建查询参数
        Map<String, Object> params = new HashMap<>();
        params.put("salespersonUserId", salespersonUserId);
        
        if (searchText != null && !searchText.isEmpty()) {
            params.put("searchText", "%" + searchText + "%");
        }
        
        if (industry != null && !industry.isEmpty()) {
            params.put("industry", industry);
        }
        
        // 设置分页
        PageHelper.startPage(pageNum, pageSize);
        
        // 调用Mapper查询
        return customerMapper.selectCustomersBySalespersonUserId(params);
    }
    
    @Override
    public long countCustomersBySalespersonUserId(Long salespersonUserId, String searchText, String industry) {
        // 构建查询参数
        Map<String, Object> params = new HashMap<>();
        params.put("salespersonUserId", salespersonUserId);
        
        if (searchText != null && !searchText.isEmpty()) {
            params.put("searchText", "%" + searchText + "%");
        }
        
        if (industry != null && !industry.isEmpty()) {
            params.put("industry", industry);
        }
        
        // 调用Mapper查询总数
        return customerMapper.countCustomersBySalespersonUserId(params);
    }
} 