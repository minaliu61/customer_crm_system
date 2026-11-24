package com.javaee.system.controller;
import com.javaee.system.po.Order;
import com.javaee.system.po.User;
import com.javaee.system.service.OrderService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @GetMapping("/{orderId}")
    public Map<String, Object> getById(@PathVariable Long orderId) {
        Map<String, Object> result = new HashMap<>();
        Order order = orderService.getById(orderId);
        if (order != null) {
            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", order);
        } else {
            result.put("code", 404);
            result.put("msg", "订单不存在");
        }
        return result;
    }
    @GetMapping("/page-by-customer")
    public Map<String, Object> pageByCustomer(Order order,
                                    @RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize) {
        Map<String, Object> result = new HashMap<>();
        PageInfo<Order> pageInfo = orderService.listByPage(order, pageNum, pageSize);
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", pageInfo);
        return result;
    }
    @GetMapping("/recent")
    public Map<String, Object> recent(Order order,
                                              @RequestParam(defaultValue = "1") Integer pageNum,
                                              @RequestParam(defaultValue = "10") Integer pageSize) {
        Map<String, Object> result = new HashMap<>();
        PageInfo<Order> pageInfo = orderService.listByPage(order, pageNum, pageSize);
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", pageInfo);
        return result;
    }
    @GetMapping("/number/{orderNumber}")
    public Map<String, Object> getByOrderNumber(@PathVariable String orderNumber) {
        Map<String, Object> result = new HashMap<>();
        Order order = orderService.getByOrderNumber(orderNumber);
        if (order != null) {
            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", order);
        } else {
            result.put("code", 404);
            result.put("msg", "订单不存在");
        }
        return result;
    }
    @GetMapping("/customer/{customerId}")
    public Map<String, Object> listByCustomerId(@PathVariable Long customerId) {
        Map<String, Object> result = new HashMap<>();
        List<Order> orders = orderService.listByCustomerId(customerId);
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", orders);
        return result;
    }
    @GetMapping("/salesperson/{salespersonId}")
    public Map<String, Object> listBySalespersonId(@PathVariable Long salespersonId) {
        Map<String, Object> result = new HashMap<>();
        List<Order> orders = orderService.listBySalespersonId(salespersonId);
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", orders);
        return result;
    }
    @GetMapping("/my")
    public Map<String, Object> getMyOrders(
            HttpSession session,
            Order order,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Map<String, Object> result = new HashMap<>();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            result.put("code", 401);
            result.put("msg", "未登录");
            return result;
        }
        if (user.getRole() != 2) {
            result.put("code", 403);
            result.put("msg", "当前用户不是销售人员");
            return result;
        }
        if (order == null) {
            order = new Order();
        }
        PageInfo<Order> pageInfo = orderService.listByPage(order, pageNum, pageSize);
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", pageInfo);
        return result;
    }
    @GetMapping("/page")
    public Map<String, Object> page(Order order,
                                    @RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize) {
        Map<String, Object> result = new HashMap<>();
        PageInfo<Order> pageInfo = orderService.listByPage(order, pageNum, pageSize);
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", pageInfo);
        return result;
    }
    @PostMapping("/create")
    public Map<String, Object> create(@RequestBody Order order) {
        Map<String, Object> result = new HashMap<>();
        try {
            int rows = orderService.create(order);
            if (rows > 0) {
                result.put("code", 200);
                result.put("msg", "订单创建成功");
                result.put("data", order);
            } else {
                result.put("code", 500);
                result.put("msg", "订单创建失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "订单创建异常: " + e.getMessage());
        }
        return result;
    }
    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody Order order) {
        Map<String, Object> result = new HashMap<>();
        order.setUpdateTime(new Date());
        int rows = orderService.update(order);
        if (rows > 0) {
            result.put("code", 200);
            result.put("msg", "订单更新成功");
        } else {
            result.put("code", 500);
            result.put("msg", "订单更新失败");
        }
        return result;
    }
    @DeleteMapping("/{orderId}")
    public Map<String, Object> delete(@PathVariable Long orderId) {
        Map<String, Object> result = new HashMap<>();
        int rows = orderService.delete(orderId);
        if (rows > 0) {
            result.put("code", 200);
            result.put("msg", "订单删除成功");
        } else {
            result.put("code", 500);
            result.put("msg", "订单删除失败");
        }
        return result;
    }
    @PutMapping("/status")
    public Map<String, Object> updateStatus(@RequestParam Long orderId, @RequestParam Integer status) {
        Map<String, Object> result = new HashMap<>();
        int rows = orderService.updateStatus(orderId, status);
        if (rows > 0) {
            result.put("code", 200);
            result.put("msg", "订单状态更新成功");
        } else {
            result.put("code", 500);
            result.put("msg", "订单状态更新失败");
        }
        return result;
    }
    @PutMapping("/{orderId}")
    public Map<String, Object> updatePayStatus(@RequestBody Order order) {
        Map<String, Object> result = new HashMap<>();
        int rows = orderService.update(order);
        if (rows > 0) {
            result.put("code", 200);
            result.put("msg", "支付状态更新成功");
        } else {
            result.put("code", 500);
            result.put("msg", "支付状态更新失败");
        }
        return result;
    }
    @GetMapping("/statistics")
    public Map<String, Object> statistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false) Long salespersonId,
            @RequestParam(required = false) Long customerId) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("salespersonId", salespersonId);
        params.put("customerId", customerId);
        
        Map<String, Object> statistics = orderService.statistics(params);
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", statistics);
        return result;}

    @GetMapping("/salesTrend")
    public Map<String, Object> getSalesTrend(
            @RequestParam(required = false) Long salespersonId,
            @RequestParam(defaultValue = "month") String period) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> data = orderService.getSalesTrend(salespersonId, period);
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", data);
        return result;
    }
    @GetMapping("/productRatio")
    public Map<String, Object> getProductRatio(@RequestParam(required = false) Long salespersonId) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> data = orderService.getProductRatio(salespersonId);
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", data);
        return result;
    }
    @GetMapping("/customerRanking")
    public Map<String, Object> getCustomerRanking(
            @RequestParam(required = false) Long salespersonId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(defaultValue = "10") Integer limit) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> data = orderService.getCustomerRanking(salespersonId, startDate, endDate, limit);
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", data);
        return result;
    }
    @GetMapping("/productRanking")
    public Map<String, Object> getProductRanking(
            @RequestParam(required = false) Long salespersonId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(defaultValue = "10") Integer limit) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> data = orderService.getProductRanking(salespersonId, startDate, endDate, limit);
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", data);
        return result;
    }
    @GetMapping("/salesRecords")
    public Map<String, Object> getSalesRecords(
            @RequestParam(required = false) Long salespersonId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String productName,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        params.put("salespersonId", salespersonId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("status", status);
        params.put("customerName", customerName);
        params.put("productName", productName);
        params.put("offset", (pageNum - 1) * pageSize);
        params.put("limit", pageSize);
        List<Map<String, Object>> data = orderService.getSalesRecords(params);
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", data);
        return result;
    }
    @GetMapping("/exportSalesData")
    public void exportSalesData(
            @RequestParam(required = false) Long salespersonId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String productName,
            javax.servlet.http.HttpServletResponse response) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("salespersonId", salespersonId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("status", status);
        params.put("customerName", customerName);
        params.put("productName", productName);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = "sales_data_" + System.currentTimeMillis() + ".xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        orderService.exportSalesData(params, response.getOutputStream());
    }
} 