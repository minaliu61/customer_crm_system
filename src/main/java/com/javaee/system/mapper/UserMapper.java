package com.javaee.system.mapper;

import com.javaee.system.po.User;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface UserMapper {
    User selectById(Long userId);
    User selectByUsername(String username);
    List<User> selectAll();
    List<User> selectByCondition(User user);
    int insert(User user);
    int update(User user);
    int deleteById(Long userId);
    int deleteBatch(@Param("ids") List<Long> ids);
} 