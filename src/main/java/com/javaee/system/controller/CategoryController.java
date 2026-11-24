package com.javaee.system.controller;

import com.javaee.system.common.Result;
import com.javaee.system.po.ProductCategory;
import com.javaee.system.service.CategoryService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public Result<PageInfo<ProductCategory>> page(ProductCategory category,
                                   @RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<ProductCategory> pageInfo = categoryService.listByPage(category, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    @GetMapping("/list")
    public Result<List<ProductCategory>> list() {
        List<ProductCategory> categories = categoryService.listAll();
        return Result.success(categories);
    }

    @GetMapping("/all")
    public Result<List<ProductCategory>> all() {
        return list(); // 复用list方法
    }

    @GetMapping("/{categoryId}")
    public Result<ProductCategory> getById(@PathVariable Long categoryId) {
        ProductCategory category = categoryService.getById(categoryId);
        return Result.success(category);
    }

    @PostMapping
    public Result<Void> add(@RequestBody ProductCategory category) {
        boolean success = categoryService.add(category);
        return success ? Result.success() : Result.error("添加失败");
    }

    @PutMapping
    public Result<Void> update(@RequestBody ProductCategory category) {
        boolean success = categoryService.update(category);
        return success ? Result.success() : Result.error("更新失败");
    }

    @DeleteMapping("/{categoryId}")
    public Result<Void> delete(@PathVariable Long categoryId) {
        boolean success = categoryService.delete(categoryId);
        return success ? Result.success() : Result.error("删除失败");
    }
} 