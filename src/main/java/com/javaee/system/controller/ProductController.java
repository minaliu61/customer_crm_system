package com.javaee.system.controller;
import com.javaee.system.po.Product;
import com.javaee.system.service.ProductService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping("/{productId}")
    public Map<String, Object> getById(@PathVariable Long productId) {
        Map<String, Object> result = new HashMap<>();
        Product product = productService.getById(productId);
        if (product != null) {
            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", product);
        } else {
            result.put("code", 404);
            result.put("msg", "产品不存在");
        }
        return result;
    }
    @GetMapping("/number/{productNumber}")
    public Map<String, Object> getByProductNumber(@PathVariable String productNumber) {
        Map<String, Object> result = new HashMap<>();
        Product product = productService.getByProductNumber(productNumber);
        if (product != null) {
            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", product);
        } else {
            result.put("code", 404);
            result.put("msg", "产品不存在");
        }
        return result;
    }
    @GetMapping("/list")
    public Map<String, Object> list() {
        Map<String, Object> result = new HashMap<>();
        List<Product> products = productService.listAll();
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", products);
        return result;
    }
    @GetMapping("/page")
    public Map<String, Object> page(Product product, 
                                    @RequestParam(defaultValue = "1") Integer pageNum, 
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(required = false) String categoryId) {
        Map<String, Object> result = new HashMap<>();
        if (categoryId != null && !categoryId.isEmpty()) {
            product.setCategory(categoryId);
        }
        PageInfo<Product> pageInfo = productService.listByPage(product, pageNum, pageSize);
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", pageInfo);
        return result;
    }
    @GetMapping("/category/{category}")
    public Map<String, Object> listByCategory(@PathVariable String category) {
        Map<String, Object> result = new HashMap<>();
        List<Product> products = productService.listByCategory(category);
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", products);
        return result;
    }
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Product product) {
        Map<String, Object> result = new HashMap<>();
        if (product.getStatus() == null) {
            product.setStatus(1);
        }
        product.setCreateTime(new Date());
        product.setUpdateTime(new Date());
        int rows = productService.add(product);
        if (rows > 0) {
            result.put("code", 200);
            result.put("msg", "添加成功");
            result.put("data", product);
        } else {
            result.put("code", 500);
            result.put("msg", "添加失败");
        }
        return result;
    }
    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody Product product) {
        Map<String, Object> result = new HashMap<>();
        product.setUpdateTime(new Date());
        
        int rows = productService.update(product);
        if (rows > 0) {
            result.put("code", 200);
            result.put("msg", "修改成功");
        } else {
            result.put("code", 500);
            result.put("msg", "修改失败");
        }
        return result;
    }
    @DeleteMapping("/{productId}")
    public Map<String, Object> delete(@PathVariable Long productId) {
        Map<String, Object> result = new HashMap<>();
        int rows = productService.delete(productId);
        if (rows > 0) {
            result.put("code", 200);
            result.put("msg", "删除成功");
        } else {
            result.put("code", 500);
            result.put("msg", "删除失败");
        }
        return result;
    }
    @DeleteMapping("/batch/{ids}")
    public Map<String, Object> deleteBatch(@PathVariable List<Long> ids) {
        Map<String, Object> result = new HashMap<>();
        int rows = productService.deleteBatch(ids);
        if (rows > 0) {
            result.put("code", 200);
            result.put("msg", "批量删除成功");
        } else {
            result.put("code", 500);
            result.put("msg", "批量删除失败");
        }
        return result;
    }
    @GetMapping("/count")
    public Map<String, Object> count() {
        Map<String, Object> result = new HashMap<>();
        int count = productService.count();
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", count);
        return result;
    }
    @PutMapping("/stock/{productId}/{quantity}")
    public Map<String, Object> updateStock(@PathVariable Long productId, @PathVariable Integer quantity) {
        Map<String, Object> result = new HashMap<>();
        try {
            int rows = productService.updateStock(productId, quantity);
            if (rows > 0) {
                result.put("code", 200);
                result.put("msg", "库存更新成功");
            } else {
                result.put("code", 500);
                result.put("msg", "库存更新失败");
            }
        } catch (RuntimeException e) {
            result.put("code", 400);
            result.put("msg", e.getMessage());
        }
        return result;
    }
} 