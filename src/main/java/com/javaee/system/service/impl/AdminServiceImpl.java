package com.javaee.system.service.impl;

import com.javaee.system.po.Admin;
import com.javaee.system.mapper.AdminMapper;
import com.javaee.system.service.AdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    
    @Autowired
    private AdminMapper adminMapper;
    
    @Override
    public Admin getByUserId(Long userId) {
        return adminMapper.selectByUserId(userId);
    }
    
    @Override
    public List<Admin> listAll() {
        return adminMapper.selectAll();
    }
    
    @Override
    public PageInfo<Admin> listByPage(String searchText, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = adminMapper.selectByCondition(searchText);
        return new PageInfo<>(list);
    }
    
    @Override
    @Transactional
    public int add(Admin admin) {
        return adminMapper.insert(admin);
    }
    
    @Override
    @Transactional
    public int update(Admin admin) {
        return adminMapper.update(admin);
    }
    
    @Override
    @Transactional
    public int delete(Long userId) {
        return adminMapper.deleteByUserId(userId);
    }
} 