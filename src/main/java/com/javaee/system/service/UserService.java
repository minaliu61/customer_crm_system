package com.javaee.system.service;

import com.javaee.system.po.User;
import com.github.pagehelper.PageInfo;
import java.util.List;

public interface UserService {
    User getById(Long userId);
    User getByUsername(String username);
    List<User> listAll();
    PageInfo<User> listByPage(User user, Integer pageNum, Integer pageSize);
    int add(User user);
    int update(User user);
    int delete(Long userId);
    int deleteBatch(List<Long> ids);
    User login(String username, String password);
    boolean changePassword(Long userId, String oldPassword, String newPassword);
} 