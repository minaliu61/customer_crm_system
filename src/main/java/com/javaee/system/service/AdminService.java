package com.javaee.system.service;
import com.javaee.system.po.Admin;
import com.github.pagehelper.PageInfo;

import java.util.List;
public interface AdminService {
    Admin getByUserId(Long userId);
    List<Admin> listAll();
    PageInfo<Admin> listByPage(String searchText, Integer pageNum, Integer pageSize);
    int add(Admin admin);
    int update(Admin admin);
    int delete(Long userId);
} 