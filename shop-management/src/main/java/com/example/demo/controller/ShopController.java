package com.example.demo.controller;

import com.example.demo.entity.Shop;
import com.example.demo.service.ShopService;
import com.example.demo.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    ShopService shopService;

    @PostMapping("/add")
    /**
     * 添加
     * @param shop 商店对象
     */
    public Result add(@RequestBody Shop shop) {
        // Logic to handle user login
        if (shopService.add(shop) == 0) {
            return Result.error("添加失败");
        }
        return Result.success("添加成功");
    }

    @PostMapping("/update")
    /**
     * 更新
     * @param shop 商店对象
     */
    public Result update(@RequestBody Shop shop) {
        // Logic to handle user login
        System.out.println("我们要更新的是" + shop.getId() + "的商店");
        if (shopService.update(shop) == 0) {
            return Result.error("更新失败");
        }
        return Result.success("更新成功");

    }

    @PostMapping("/delete")
    /**
     * 删除
     * @param shop 商店对象
     */
    public Result delete(@RequestBody Shop shop) {
        // Logic to handle user login
        if (shopService.delete(shop) == 0) {
            return Result.error("删除失败");
        }
        return Result.success("删除成功");
    }

    @PostMapping("/query")
/**
 * 查询
 * @param shop 商店对象
 * @return 查询结果
 */
    public Result query(@RequestBody Shop shop) {
        // 参数验证
        if (shop == null) {
            return Result.error("Shop object cannot be null");
        }

        try {
            // 执行查询操作
            List<Shop> result =  shopService.query(shop);
            return Result.success(result);
        } catch (Exception e) {
            // 异常处理
            return Result.error("An error occurred while querying: " + e.getMessage());
        }
    }
}
