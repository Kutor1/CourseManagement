package com.coursemanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coursemanagement.common.Result;
import com.coursemanagement.modules.user.domain.User;
import com.coursemanagement.modules.user.service.UserService;
import com.coursemanagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

// 处理用户登录类
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    // 用户登录
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User loginUser) {
        User user = userService.getOne(new QueryWrapper<User>().eq("username", loginUser.getUsername()));
//        if (user != null && passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
//            // 登录成功，生成 JWT
//            String token = JwtUtil.generateToken(user.getUsername());
//            return Result.success(token);
//        }

        if (user != null && loginUser.getPassword().equals(user.getPassword())) {

            if (user.getStatus() == false) {
                // 登录成功，生成 token 或 session
                String token = "some-generated-token"; // 实际项目中应生成 JWT 或其他安全凭证

                // 获取用户角色
                String role = user.getRole(); // 假设 User 类中有 getRole 方法返回用户角色

                // 获取用户id
                Long id = user.getId();

                // 获取用户名称
                String username = user.getUsername();

                // 返回 token 和角色信息 和 用户id
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("token", token);
                responseData.put("role", role);
                responseData.put("id", id);
                responseData.put("username", username);

                // 设置已登录状态并保存
                user.setStatus(true);
                userService.updateById(user);

                return Result.success(responseData);
            } else if (user.getStatus() == true) {
                return Result.error("用户已登录");
            }
        }

        return Result.error("用户名或密码错误");
    }


    // 用户注册
    @PostMapping("/register")
    public Result<Void> register(@RequestBody User user) {
        if (userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername())) != null) {
            return Result.error("用户名已存在");
        }

        user.setPassword(user.getPassword());
        userService.save(user);
        return Result.success(null);
    }

    // 用户退出
    @PostMapping("/logout/{userid}")
    public Result<String> logout(@PathVariable Long userid) {

        User user = new User();
        user.setId(userid);

        // 设置登出状态
        user.setStatus(false);
        userService.updateById(user);

        return Result.success("用户登出成功");
    }

    // 管理员登录
    @PostMapping("/control")
    public Result<Map<String, Object>> loginControl(@RequestBody User controlUser) {
        // 根据controlUser.username获取数据库信息进行匹配
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",controlUser.getUsername());
        User user = userService.getOne(queryWrapper);

        if (user != null && user.getPassword().equals(controlUser.getPassword()) ) {
            if (user.getStatus() == false) {
                // 登录成功，生成 token 或 session
                String token = "some-generated-token"; // 实际项目中应生成 JWT 或其他安全凭证

                // 获取用户角色
                String role = user.getRole(); // 假设 User 类中有 getRole 方法返回用户角色

                // 获取用户id
                Long id = user.getId();

                // 获取用户名称
                String username = user.getUsername();

                // 返回 token 和角色信息 和 用户id
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("token", token);
                responseData.put("role", role);
                responseData.put("id", id);
                responseData.put("username", username);

                // 设置已登录状态并保存
                user.setStatus(true);
                userService.updateById(user);

                return Result.success(responseData);
            } else if (user.getStatus() == true) {
                return Result.error("用户已登录");
            }
        }
        return Result.error("用户名或密码错误");
    }
}


