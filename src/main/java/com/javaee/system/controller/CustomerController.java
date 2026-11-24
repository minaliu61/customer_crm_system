package com.javaee.system.controller;
import com.javaee.system.common.Result;
import com.javaee.system.po.Customer;
import com.javaee.system.po.User;
import com.javaee.system.service.CustomerService;
import com.javaee.system.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @GetMapping("/by-user-id")
    public Result<Customer> getByUserId(@RequestParam Long userId) {
        return Result.success(customerService.getByUserId(userId));
    }
    @GetMapping("/current")
    public Result<Customer> getCurrentCustomer(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("未登录");
        }
        if (user.getRole() != 3) {
            return Result.error("当前用户不是客户");
        }
        return Result.success(customerService.getByUserId(user.getUserId()));
    }
    @GetMapping("/my")
    public Result<PageInfo<Customer>> getMyCustomers(
            HttpSession session,
            @RequestParam(required = false) String searchText,
            @RequestParam(required = false) String industry,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("未登录");
        }
        if (user.getRole() != 2) {
            return Result.error("当前用户不是销售人员");
        }
        Long salespersonId = null;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", user.getUserId());
            List<Map<String, Object>> result = customerService.getCustomersBySalespersonUserId(user.getUserId(),
                    searchText, industry, pageNum, pageSize);
            PageInfo<Customer> pageInfo = new PageInfo<>();
            pageInfo.setList(result.stream()
                    .map(map -> {
                        Customer customer = new Customer();
                        customer.setCustomerId(((Number) map.get("customer_id")).longValue());
                        customer.setCompany((String) map.get("company"));
                        customer.setIndustry((String) map.get("industry"));
                        customer.setAddress((String) map.get("address"));
                        customer.setCity((String) map.get("city"));
                        customer.setProvince((String) map.get("province"));
                        User customerUser = new User();
                        customerUser.setRealName((String) map.get("real_name"));
                        customerUser.setPhone((String) map.get("phone"));
                        customerUser.setEmail((String) map.get("email"));
                        customer.setUser(customerUser);
                        return customer;
                    })
                    .collect(Collectors.toList()));
            pageInfo.setPageNum(pageNum);
            pageInfo.setPageSize(pageSize);
            pageInfo.setTotal(customerService.countCustomersBySalespersonUserId(user.getUserId(), searchText,
                    industry));

            return Result.success(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取客户列表失败: " + e.getMessage());
        }
    }
    @GetMapping("/{customerId}")
    public Result<Customer> getById(@PathVariable Long customerId) {
        return Result.success(customerService.getById(customerId));
    }
    @GetMapping("/list")
    public Result<List<Customer>> list() {
        return Result.success(customerService.listAll());
    }
    @GetMapping("/page")
    public Result<PageInfo<Customer>> page(
            Customer customer,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        if (username != null || realName != null || phone != null || email != null) {
            User user = new User();
            user.setUsername(username);
            user.setRealName(realName);
            user.setPhone(phone);
            user.setEmail(email);
            if (customer == null) {
                customer = new Customer();
            }
            customer.setUser(user);
        }
        return Result.success(customerService.listByPage(customer, pageNum, pageSize));
    }
    @PostMapping
    public Result<Void> add(@RequestBody Customer customer) {
        return customerService.add(customer) > 0 ? Result.success() : Result.error();
    }
    @PutMapping
    public Result<Void> update(@RequestBody Customer customer) {
        return customerService.update(customer) > 0 ? Result.success() : Result.error();
    }
    @DeleteMapping("/{customerId}")
    public Result<Void> delete(@PathVariable Long customerId) {
        return customerService.delete(customerId) > 0 ? Result.success() : Result.error();
    }
    @DeleteMapping("/batch")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        return customerService.deleteBatch(ids) > 0 ? Result.success() : Result.error();
    }
    @GetMapping("/count")
    public Result<Integer> count() {
        return Result.success(customerService.count());
    }

    @GetMapping("/count-by-salesperson")
    public Result<Integer> countBySalesperson(@RequestParam(required = false) Long salespersonId) {
        if (salespersonId == null) {
            return Result.success("请提供销售人员ID(salespersonId)参数", 0);
        }
        return Result.success(customerService.countBySalesperson(salespersonId));
    }
} 