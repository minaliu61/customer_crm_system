package com.javaee.system.controller;
import com.javaee.system.common.Result;
import com.javaee.system.po.Salesperson;
import com.javaee.system.po.User;
import com.javaee.system.service.SalespersonService;
import com.javaee.system.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
@RestController
@RequestMapping("/api/salesperson")
public class SalespersonController {
    @Autowired
    private SalespersonService salespersonService;
    @Autowired
    private UserService userService;
    @GetMapping("/by-user-id")
    public Result<Salesperson> getByUserId(@RequestParam Long userId) {
        return Result.success(salespersonService.getByUserId(userId));
    }
    @GetMapping("/current")
    public Result<Salesperson> getCurrentSalesperson(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("未登录");
        }
        if (user.getRole() != 2) {
            return Result.error("当前用户不是销售人员");
        }
        return Result.success(salespersonService.getByUserId(user.getUserId()));
    }
    @GetMapping("/profile")
    public Result<Map<String, Object>> getProfile(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("未登录");
        }
        if (user.getRole() != 2) {
            return Result.error("当前用户不是销售人员");
        }
        Salesperson salesperson = salespersonService.getByUserId(user.getUserId());
        if (salesperson == null) {
            return Result.error("销售人员信息不存在");
        }
        List<Map<String, Object>> monthlyStats = salespersonService.getMonthlyStatistics(salesperson.getSalespersonId());
        List<Map<String, Object>> productRatio = salespersonService.getProductRatio(salesperson.getSalespersonId());
        Map<String, Object> result = new HashMap<>();
        result.put("salesperson", salesperson);
        result.put("user", user);
        result.put("monthlyStats", monthlyStats);
        result.put("productRatio", productRatio);
        return Result.success(result);
    }
    @PutMapping("/profile")
    public Result<Void> updateProfile(@RequestBody Map<String, Object> profileData, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.error("未登录");
        }
        if (currentUser.getRole() != 2) {
            return Result.error("当前用户不是销售人员");
        }
        try {
            User user = userService.getById(currentUser.getUserId());
            if (profileData.containsKey("phone")) {
                user.setPhone((String) profileData.get("phone"));
            }
            if (profileData.containsKey("email")) {
                user.setEmail((String) profileData.get("email"));
            }
            if (profileData.containsKey("realName")) {
                user.setRealName((String) profileData.get("realName"));
            }
            if (profileData.containsKey("oldPassword") && profileData.containsKey("newPassword")) {
                String oldPassword = (String) profileData.get("oldPassword");
                String newPassword = (String) profileData.get("newPassword");
                if (!user.getPassword().equals(oldPassword)) {
                    return Result.error("旧密码不正确");
                }
                user.setPassword(newPassword);
            }
            userService.update(user);
            Salesperson salesperson = salespersonService.getByUserId(user.getUserId());
            if (profileData.containsKey("department")) {
                salesperson.setDepartment((String) profileData.get("department"));
            }
            if (profileData.containsKey("position")) {
                salesperson.setPosition((String) profileData.get("position"));
            }
            salespersonService.update(salesperson);
            session.setAttribute("user", userService.getById(user.getUserId()));
            return Result.success();
        } catch (Exception e) {
            return Result.error("更新个人资料失败: " + e.getMessage());
        }
    }
    @GetMapping("/{salespersonId}")
    public Result<Salesperson> getById(@PathVariable Long salespersonId) {
        return Result.success(salespersonService.getById(salespersonId));
    }
    @GetMapping("/list")
    public Result<List<Salesperson>> list() {
        return Result.success(salespersonService.listAll());
    }
    @GetMapping("/page")
    public Result<PageInfo<Salesperson>> page(
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Salesperson salesperson = new Salesperson();
        if (StringUtils.hasText(searchText)) {
            salesperson.setEmployeeId(searchText);
        }
        return Result.success(salespersonService.listByPage(salesperson, pageNum, pageSize));
    }
    @PostMapping
    public Result<Void> add(@RequestBody Map<String, Object> requestData) {
        try {
            Map<String, Object> userData = (Map<String, Object>) requestData.get("user");
            if (userData == null) {
                return Result.error("用户信息不能为空");
            }
            User user = new User();
            user.setUsername((String) userData.get("username"));
            user.setPassword((String) userData.get("password"));
            user.setRealName((String) userData.get("realName"));
            user.setPhone((String) userData.get("phone"));
            user.setEmail((String) userData.get("email"));
            user.setStatus(Integer.parseInt(userData.get("status").toString()));
            user.setRole(2);
            if (userService.getByUsername(user.getUsername()) != null) {
                return Result.error("用户名已存在");
            }

            if (userService.add(user) <= 0) {
                return Result.error("创建用户失败");
            }
            Salesperson salesperson = new Salesperson();
            salesperson.setUserId(user.getUserId());
            salesperson.setEmployeeId((String) requestData.get("employeeId"));
            salesperson.setDepartment((String) requestData.get("department"));
            salesperson.setPosition((String) requestData.get("position"));
            return salespersonService.add(salesperson) > 0 ? Result.success() : Result.error("创建销售人员信息失败");
        } catch (Exception e) {
            return Result.error("创建销售人员失败: " + e.getMessage());
        }
    }
    @PutMapping
    public Result<Void> update(@RequestBody Map<String, Object> requestData) {
        try {
            Long salespersonId = Long.parseLong(requestData.get("salespersonId").toString());
            Salesperson salesperson = salespersonService.getById(salespersonId);
            if (salesperson == null) {
                return Result.error("销售人员不存在");
            }
            salesperson.setEmployeeId((String) requestData.get("employeeId"));
            salesperson.setDepartment((String) requestData.get("department"));
            salesperson.setPosition((String) requestData.get("position"));
            Map<String, Object> userData = (Map<String, Object>) requestData.get("user");
            if (userData != null && userData.get("userId") != null) {
                Long userId = Long.parseLong(userData.get("userId").toString());
                User user = userService.getById(userId);
                if (user != null) {
                    user.setUsername((String) userData.get("username"));
                    if (userData.get("password") != null && !((String)userData.get("password")).isEmpty()) {
                        user.setPassword((String) userData.get("password"));
                    }
                    user.setRealName((String) userData.get("realName"));
                    user.setPhone((String) userData.get("phone"));
                    user.setEmail((String) userData.get("email"));
                    user.setStatus(Integer.parseInt(userData.get("status").toString()));
                    userService.update(user);
                }
            }
            return salespersonService.update(salesperson) > 0 ? Result.success() : Result.error();
        } catch (Exception e) {
            return Result.error("更新销售人员失败: " + e.getMessage());
        }
    }
    @DeleteMapping("/{salespersonId}")
    public Result<Void> delete(@PathVariable Long salespersonId) {

        Salesperson salesperson = salespersonService.getById(salespersonId);
        if (salesperson == null) {
            return Result.error("销售人员不存在");
        }
        salespersonService.delete(salespersonId);
        if (salesperson.getUserId() != null) {
            userService.delete(salesperson.getUserId());
        }
        return Result.success();
    }
    @DeleteMapping("/batch")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        return salespersonService.deleteBatch(ids) > 0 ? Result.success() : Result.error();
    }
    @GetMapping("/performance/{salespersonId}")
    public Result<List<Map<String, Object>>> getSalespersonPerformance(@PathVariable Long salespersonId) {
        return Result.success(salespersonService.getMonthlyStatistics(salespersonId));
    }
    @GetMapping("/performance")
    public Result<List<Map<String, Object>>> getSalespersonPerformance() {
        return Result.success(salespersonService.getPerformance());
    }
    @GetMapping("/count")
    public Result<Integer> count() {
        return Result.success(salespersonService.count());
    }
    @GetMapping("/monthly-statistics")
    public Result<List<Map<String, Object>>> getMonthlyStatistics(@RequestParam(required = false) Long salespersonId) {
        if (salespersonId == null) {
            return Result.error("缺少必要参数：salespersonId");
        }
        return Result.success(salespersonService.getMonthlyStatistics(salespersonId));
    }
    @GetMapping("/product-ratio")
    public Result<List<Map<String, Object>>> getProductRatio(@RequestParam(required = false) Long salespersonId) {
        if (salespersonId == null) {
            return Result.error("缺少必要参数：salespersonId");
        }
        return Result.success(salespersonService.getProductRatio(salespersonId));
    }
} 