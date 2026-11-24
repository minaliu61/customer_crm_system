package com.javaee.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "login";
    }

    @GetMapping("/login.html")
    public String login() {
        return "login";
    }

    @GetMapping("/register.html")
    public String register() {
        return "register";
    }

    @GetMapping("/customer/profile.html")
    public String customerProfile() {
        return "customer/profile";
    }
    
    @GetMapping("/customer/change-password.html")
    public String customerChangePassword() {
        return "customer/change-password";
    }

    @GetMapping("/customer/order-list.html")
    public String customerOrderList() {
        return "customer/order-list";
    }

    @GetMapping("/customer/order-detail.html")
    public String customerOrderDetail() {
        return "customer/order-detail";
    }
    
    @GetMapping("/customer/product-detail.html")
    public String customerProductDetail() {
        return "customer/product-detail";
    }


    @GetMapping("/sales/profile.html")
    public String salesProfile() {
        return "sales/profile";
    }

    @GetMapping("/sales/change-password.html")
    public String salesChangePassword() {
        return "sales/change-password";
    }

    @GetMapping("/sales/customer-list.html")
    public String salesCustomerList() {
        return "sales/customer-list";
    }

    @GetMapping("/sales")
    public String salesC() {
        return "sales/customer-list";
    }

    @GetMapping("/sales/order-list.html")
    public String salesOrderList() {
        return "sales/order-list";
    }

    @GetMapping("/sales/order-create.html")
    public String salesOrderCreate() {
        return "sales/order-create";
    }


    @GetMapping("/admin/index.html")
    public String adminIndex() {
        return "admin/index";
    }
    
    @GetMapping("/admin/profile.html")
    public String adminProfile() {
        return "admin/profile";
    }
    
    @GetMapping("/admin/change-password.html")
    public String adminChangePassword() {
        return "admin/change-password";
    }

    @GetMapping("admin/admin-list.html")
    public String adminAdminList() {
        return "admin/admin-list";
    }
    
    @GetMapping("admin/salesperson-list.html")
    public String adminSalespersonList() {
        return "admin/salesperson-list";
    }
    
    @GetMapping("admin/customer-list.html")
    public String adminCustomerList() {
        return "admin/customer-list";
    }
    
    @GetMapping("admin/product-category.html")
    public String adminProductCategory() {
        return "admin/product-category";
    }
    
    @GetMapping("admin/product-list.html")
    public String adminProductList() {
        return "admin/product-list";
    }
    
    @GetMapping("admin/order-list.html")
    public String adminOrderList() {
        return "admin/order-list";
    }
}

