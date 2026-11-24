package com.javaee.system.service.impl;

import com.javaee.system.po.Salesperson;
import com.javaee.system.mapper.SalespersonMapper;
import com.javaee.system.service.SalespersonService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class SalespersonServiceImpl implements SalespersonService {

    @Autowired
    private SalespersonMapper salespersonMapper;

    @Override
    public Salesperson getById(Long salespersonId) {
        return salespersonMapper.selectById(salespersonId);
    }

    @Override
    public Salesperson getByUserId(Long userId) {
        return salespersonMapper.selectByUserId(userId);
    }

    @Override
    public List<Salesperson> listAll() {
        return salespersonMapper.selectAll();
    }

    @Override
    public PageInfo<Salesperson> listByPage(Salesperson salesperson, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Salesperson> list = salespersonMapper.selectByCondition(salesperson);
        return new PageInfo<>(list);
    }

    @Override
    @Transactional
    public int add(Salesperson salesperson) {
        return salespersonMapper.insert(salesperson);
    }

    @Override
    @Transactional
    public int update(Salesperson salesperson) {
        return salespersonMapper.update(salesperson);
    }

    @Override
    @Transactional
    public int delete(Long salespersonId) {
        return salespersonMapper.deleteById(salespersonId);
    }

    @Override
    @Transactional
    public int deleteBatch(List<Long> ids) {
        return salespersonMapper.deleteBatch(ids);
    }

    @Override
    public int count() {
        return salespersonMapper.count();
    }

    @Override
    public List<Map<String, Object>> getPerformance() {
        // 从数据库查询销售人员业绩排行
        return salespersonMapper.selectPerformance();
    }

    @Override
    public List<Map<String, Object>> getMonthlyStatistics(Long salespersonId) {
        // 从数据库查询销售人员月度统计数据
        return salespersonMapper.selectMonthlyStatistics(salespersonId);
    }

    @Override
    public List<Map<String, Object>> getProductRatio(Long salespersonId) {
        // 从数据库查询销售人员产品销售占比
        return salespersonMapper.selectProductRatio(salespersonId);
    }
} 