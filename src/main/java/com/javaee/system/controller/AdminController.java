package com.javaee.system.controller;
import com.javaee.system.common.Result;
import com.javaee.system.po.Admin;
import com.javaee.system.po.User;
import com.javaee.system.service.AdminService;
import com.javaee.system.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;
    @GetMapping("/page")
    public Result<PageInfo<Admin>> page(@RequestParam(required = false) String searchText,
                                      @RequestParam(defaultValue = "1") Integer pageNum,
                                      @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(adminService.listByPage(searchText, pageNum, pageSize));
    }
    @GetMapping("/{userId}")
    public Result<Admin> getByUserId(@PathVariable Long userId) {
        return Result.success(adminService.getByUserId(userId));
    }
    @PostMapping
    public Result<Void> add(@RequestBody Admin admin) {
        User user = admin.getUser();
        user.setRole(1);
        if (userService.getByUsername(user.getUsername()) != null) {
            return Result.error("用户名已存在");
        }
        if (userService.add(user) <= 0) {
            return Result.error("创建用户失败");
        }
        admin.setUserId(user.getUserId());
        return adminService.add(admin) > 0 ? Result.success() : Result.error("创建管理员信息失败");
    }
    @PutMapping
    public Result<Void> update(@RequestBody Admin admin) {
        if (admin.getUser() != null && admin.getUser().getUserId() != null) {
            admin.setUserId(admin.getUser().getUserId());
            userService.update(admin.getUser());
            return adminService.update(admin) > 0 ? Result.success() : Result.error();
        }
        return Result.error("用户ID不能为空");
    }
    @DeleteMapping("/{userId}")
    public Result<Void> delete(@PathVariable Long userId) {
        adminService.delete(userId);
        return userService.delete(userId) > 0 ? Result.success() : Result.error();
    }

}