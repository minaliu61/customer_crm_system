package com.javaee.system.mapper;
import com.javaee.system.po.Admin;
import org.apache.ibatis.annotations.Param;

import java.util.List;
public interface AdminMapper {
    Admin selectByUserId(Long userId);
    List<Admin> selectAll();
    List<Admin> selectByCondition(@Param("searchText") String searchText);
    int insert(Admin admin);
    int update(Admin admin);
    int deleteByUserId(Long userId);
} 