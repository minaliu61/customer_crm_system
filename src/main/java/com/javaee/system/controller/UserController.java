package com.javaee.system.controller;

import com.javaee.system.common.Result;
import com.javaee.system.po.User;
import com.javaee.system.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public Result<User> login(@RequestBody User user,HttpSession session) {
        User loginUser = userService.login(user.getUsername(), user.getPassword());
        if (loginUser != null) {
            // 登录成功，将用户信息存入session
            session.setAttribute("user", loginUser);
            return Result.success(loginUser);
        }
        return Result.error("用户名或密码错误");
    }
    @GetMapping("/logout")
    public Result<Void> logout(HttpSession session) {
        session.removeAttribute("user");
        return Result.success();
    }
    @GetMapping("/current")
    public Result<User> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return Result.success(user);
        }
        return Result.error("未登录");
    }
    @GetMapping("/{userId}")
    public Result<User> getById(@PathVariable Long userId) {
        return Result.success(userService.getById(userId));
    }
    @GetMapping("/list")
    public Result<List<User>> list() {
        return Result.success(userService.listAll());
    }
    @GetMapping("/page")
    public Result<PageInfo<User>> page(User user,
                                      @RequestParam(defaultValue = "1") Integer pageNum,
                                      @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(userService.listByPage(user, pageNum, pageSize));
    }
    @PostMapping
    public Result<Long> add(@RequestBody User user) {
        // 校验用户名是否已存在
        if (userService.getByUsername(user.getUsername()) != null) {
            return Result.error("用户名已存在");
        }
        int rows = userService.add(user);
        return rows > 0 ? Result.success(user.getUserId()) : Result.error();
    }
    @PutMapping
    public Result<Void> update(@RequestBody User user) {
        return userService.update(user) > 0 ? Result.success() : Result.error();
    }
    @DeleteMapping("/{userId}")
    public Result<Void> delete(@PathVariable Long userId) {
        return userService.delete(userId) > 0 ? Result.success() : Result.error();
    }
    @DeleteMapping("/batch")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        return userService.deleteBatch(ids) > 0 ? Result.success() : Result.error();
    }
    @PostMapping("/change-password")
    public Result<Void> changePassword(@RequestParam Long userId,
                                     @RequestParam String oldPassword,
                                     @RequestParam String newPassword) {
        return userService.changePassword(userId, oldPassword, newPassword) ? 
            Result.success() : Result.error("原密码错误");
    }
} 