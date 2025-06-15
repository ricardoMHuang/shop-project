package com.example.demo.controller;

import com.example.demo.service.UserService;
import com.example.demo.util.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户操作
 * @author Administrator
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/user")   // Base URL for all user-related endpoints
@Tag(name = "用户操作")
public class UserController {
    @Autowired
    UserService userService;
    // User-related endpoints will be defined here
    @PostMapping("/login")
    public Result login() {
        // Logic to handle user login

        // Return a success response
        System.out.println("登录成功");
        return Result.success("登录成功");
    }
}

