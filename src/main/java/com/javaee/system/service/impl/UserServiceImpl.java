package com.javaee.system.service.impl;

import com.javaee.system.po.User;
import com.javaee.system.mapper.UserMapper;
import com.javaee.system.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User getById(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public User getByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public List<User> listAll() {
        return userMapper.selectAll();
    }

    @Override
    public PageInfo<User> listByPage(User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectByCondition(user);
        return new PageInfo<>(list);
    }

    @Override
    @Transactional
    public int add(User user) {
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.insert(user);
    }

    @Override
    @Transactional
    public int update(User user) {
        // 如果密码不为空，需要加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userMapper.update(user);
    }

    @Override
    @Transactional
    public int delete(Long userId) {
        return userMapper.deleteById(userId);
    }

    @Override
    @Transactional
    public int deleteBatch(List<Long> ids) {
        return userMapper.deleteBatch(ids);
    }

    @Override
    public User login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // 登录成功，清除密码
            user.setPassword(null);
            return user;
        }
        return null;
    }

    @Override
    @Transactional
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user != null && passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            return userMapper.update(user) > 0;
        }
        return false;
    }
} 