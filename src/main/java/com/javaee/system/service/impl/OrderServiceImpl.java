package com.javaee.system.service.impl;

import com.javaee.system.po.*;
import com.javaee.system.mapper.*;
import com.javaee.system.service.OrderService;
import com.javaee.system.service.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private OrderItemMapper orderItemMapper;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SalespersonMapper salespersonMapper;

    @Override
    public Order getById(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order != null) {
            // 查询订单明细
            List<OrderItem> orderItems = orderItemMapper.selectByOrderId(orderId);
            order.setOrderItems(orderItems);
        }

        Customer customer = customerMapper.selectById(order.getCustomerId());

        User user = userMapper.selectById(customer.getUserId());
        order.getCustomer().setUser(user);
        return order;
    }

    @Override
    public Order getByOrderNumber(String orderNumber) {
        Order order = orderMapper.selectByOrderNumber(orderNumber);
        if (order != null) {
            // 查询订单明细
            List<OrderItem> orderItems = orderItemMapper.selectByOrderId(order.getOrderId());
            order.setOrderItems(orderItems);
        }
        return order;
    }

    @Override
    @Transactional
    public int create(Order order) {
        // 生成订单编号
        try{
            String orderNumber = generateOrderNumber();
        order.setOrderNumber(orderNumber);

        // 设置默认状态
        if (order.getStatus() == null) {
            order.setStatus(0); // 待审核状态
        }
        // 设置创建时间和更新时间
        Date now = new Date();
        order.setCreateTime(now);
        order.setUpdateTime(now);

        // 计算订单总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = order.getOrderItems();


        if (!orderItems.isEmpty()) {
            for (OrderItem item : orderItems) {
                BigDecimal multiplied = new BigDecimal(item.getQuantity()).multiply(item.getProductPrice());
                totalAmount =     totalAmount.add(multiplied);
            }
        }

            return orderMapper.insert(order);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return 0;
    }
    /**
     * 生成订单编号
     */
    private String generateOrderNumber() {
        // 生成格式：yyyyMMdd + 6位随机数
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomStr = String.format("%06d", new Random().nextInt(1000000));
        return dateStr + randomStr;
    }

    @Override
    @Transactional
    public int update(Order order) {
        order.setUpdateTime(new Date());
        return orderMapper.update(order);
    }

    @Override
    @Transactional
    public int delete(Long orderId) {
        // 删除订单明细
        orderItemMapper.deleteByOrderId(orderId);
        // 删除订单
        return orderMapper.deleteById(orderId);
    }

    @Override
    public List<Order> listByCustomerId(Long customerId) {
        return orderMapper.selectByCustomerId(customerId);
    }

    @Override
    public List<Order> listBySalespersonId(Long salespersonId) {
        return orderMapper.selectBySalespersonId(salespersonId);
    }

    @Override
    public PageInfo<Order> listByPage(Order order, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> list = orderMapper.selectByCondition(order);
        list.stream().forEach(resultOrder -> {
            Customer customer = customerMapper.selectById(resultOrder.getCustomerId());

            User user = userMapper.selectById(customer.getUserId());
            resultOrder.getCustomer().setUser(user);
                }
        );
        return new PageInfo<>(list);
    }

    @Override
    @Transactional
    public int updateStatus(Long orderId, Integer status) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setStatus(status);
        
        // 如果订单已完成，设置完成时间
        if (status == 3) {
            order.setCompleteTime(new Date());
        }
        // 如果订单已发货，设置发货时间
        else if (status == 2) {
            order.setShipTime(new Date());
        }
        
        order.setUpdateTime(new Date());
        return orderMapper.update(order);
    }

    @Override
    @Transactional
    public int updatePayStatus(Long orderId, Integer payStatus) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setPayStatus(payStatus);
        
        // 如果已支付，设置支付时间
        if (payStatus == 2) {
            order.setPayTime(new Date());
        }
        
        order.setUpdateTime(new Date());
        return orderMapper.update(order);
    }

    @Override
    public Map<String, Object> statistics(Map<String, Object> params) {
        return orderMapper.statistics(params);
    }

    @Override
    public List<Map<String, Object>> getSalesTrend(Long salespersonId, String period) {
        Map<String, Object> params = new HashMap<>();
        params.put("salespersonId", salespersonId);
        params.put("period", period);
        return orderMapper.getSalesTrend(params);
    }

    @Override
    public List<Map<String, Object>> getProductRatio(Long salespersonId) {
        return orderMapper.getProductRatio(salespersonId);
    }
    
    @Override
    public List<Map<String, Object>> getCustomerRanking(Long salespersonId, Date startDate, Date endDate, Integer limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("salespersonId", salespersonId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("limit", limit);
        return orderMapper.getCustomerRanking(params);
    }
    
    @Override
    public List<Map<String, Object>> getProductRanking(Long salespersonId, Date startDate, Date endDate, Integer limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("salespersonId", salespersonId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("limit", limit);
        return orderMapper.getProductRanking(params);
    }
    
    @Override
    public List<Map<String, Object>> getSalesRecords(Map<String, Object> params) {
        return orderMapper.getSalesRecords(params);
    }
    
    @Override
    public void exportSalesData(Map<String, Object> params, OutputStream outputStream) throws Exception {
        // 获取销售记录数据
        List<Map<String, Object>> salesData = orderMapper.getSalesRecords(params);
        
        // 导出为Excel文件
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("销售记录");
        
        // 创建表头
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("订单号");
        headerRow.createCell(1).setCellValue("客户名称");
        headerRow.createCell(2).setCellValue("产品名称");
        headerRow.createCell(3).setCellValue("数量");
        headerRow.createCell(4).setCellValue("金额");
        headerRow.createCell(5).setCellValue("销售日期");
        headerRow.createCell(6).setCellValue("状态");
        
        // 填充数据
        int rowIndex = 1;
        for (Map<String, Object> record : salesData) {
            Row row = sheet.createRow(rowIndex);
            row.createCell(0).setCellValue(toString(record.get("orderNumber")));
            row.createCell(1).setCellValue(toString(record.get("customerName")));
            row.createCell(2).setCellValue(toString(record.get("productName")));
            row.createCell(3).setCellValue(toInteger(record.get("quantity")));
            row.createCell(4).setCellValue(toDouble(record.get("amount")));
            row.createCell(5).setCellValue(toString(record.get("salesDate")));
            row.createCell(6).setCellValue(getStatusText(record.get("status")));
            rowIndex++;
        }
        
        // 自动调整列宽
        for (int colIndex = 0; colIndex < 7; colIndex++) {
            sheet.autoSizeColumn(colIndex);
        }
        
        // 输出到输出流
        workbook.write(outputStream);
        workbook.close();
    }

    private String toString(Object obj) {
        return obj == null ? "" : obj.toString();
    }
    
    private int toInteger(Object obj) {
        if (obj == null) {
            return 0;
        }
        try {
            return Integer.parseInt(obj.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    private double toDouble(Object obj) {
        if (obj == null) {
            return 0.0;
        }
        try {
            return Double.parseDouble(obj.toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    private String getStatusText(Object status) {
        if (status == null) {
            return "未知";
        }
        
        int statusValue = Integer.parseInt(status.toString());
        switch (statusValue) {
            case 0: return "待付款";
            case 1: return "已付款";
            case 2: return "已发货";
            case 3: return "已完成";
            case 4: return "已取消";
            default: return "未知";
        }
    }
} 