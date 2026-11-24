package com.javaee.system.service;
import com.javaee.system.po.Salesperson;
import com.github.pagehelper.PageInfo;
import java.util.List;
import java.util.Map;
public interface SalespersonService {
    Salesperson getById(Long salespersonId);
    Salesperson getByUserId(Long userId);
    List<Salesperson> listAll();
    PageInfo<Salesperson> listByPage(Salesperson salesperson, Integer pageNum, Integer pageSize);
    int add(Salesperson salesperson);
    int update(Salesperson salesperson);
    int delete(Long salespersonId);
    int deleteBatch(List<Long> ids);
    int count();
    List<Map<String, Object>> getPerformance();
    List<Map<String, Object>> getMonthlyStatistics(Long salespersonId);
    List<Map<String, Object>> getProductRatio(Long salespersonId);
} 