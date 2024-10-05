package com.coursemanagement.modules.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coursemanagement.common.Result;
import com.coursemanagement.modules.course.domain.Course;
import com.coursemanagement.modules.course.service.CourseService;
import com.coursemanagement.modules.enrollment.domain.Enrollment;
import com.coursemanagement.modules.enrollment.service.EnrollmentService;
import com.coursemanagement.modules.user.domain.User;
import com.coursemanagement.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// user业务处理
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseService courseService;

    // 获取所有用户信息
    @GetMapping
    public Result<List<User>> getAllUsers() {
        //  排除管理员账户
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("role", "CONTROL");
        List<User> users = userService.list(queryWrapper);
        return Result.success(users);
    }

    //获取当前用户名
    @GetMapping("/{id}")
    public Result<User> getUserName(@PathVariable Long id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    // 管理员编辑用户信息
    @PostMapping
    public Result<User> editUser(@RequestBody User user) {
        userService.updateById(user);
        return Result.success(user);
    }

    // 用户更改用户名
    @PutMapping("/{userid}&{newname}")
    public Result<User> updateUserName(@PathVariable String newname, @PathVariable Long userid) {
        // 查找新用户名是否重复，重复则报错
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", newname);
        User olduser = userService.getOne(queryWrapper);
        User user = userService.getById(userid);

        // 保存旧教师用户名
        String oldname = user.getUsername();

        if ( olduser == null ) {
            // 更新用户信息
            user.setUsername(newname);
            user.setStatus(false);
            userService.updateById(user);

            // 如果该用户为教师，则更新enrollments表中teachername字段
            if ("TEACHER".equals(user.getRole())) {
                // 创建查询条件，查找所有teachername为oldname的记录
                QueryWrapper<Enrollment> enrollmentQueryWrapper = new QueryWrapper<>();
                enrollmentQueryWrapper.eq("teachername", oldname);

                QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
                courseQueryWrapper.eq("teachername", oldname);

                // 构造更新对象，将oldname替换为newname
                Enrollment enrollment = new Enrollment();
                enrollment.setTeachername(newname);

                Course course = new Course();
                course.setTeachername(newname);

                // 批量更新enrollments表中的teachername字段
                enrollmentService.update(enrollment, enrollmentQueryWrapper);

                courseService.update(course, courseQueryWrapper);
            }
        } else if ( olduser.getUsername().equals(newname) ){
            return Result.error("用户名已占用");
        }
        return Result.success(user);
    }


    // 用户更改密码
    @PutMapping
    public Result<User> updatePassword(@RequestHeader("currentpassword")String currentpassword, @RequestHeader("newpassword")String newpassword, @RequestHeader("userid") Long userid) {
//        根据当前id查询原密码 与 数据库是否一致
        User user = userService.getById(userid);
        if (!user.getPassword().equals(currentpassword)){
            return Result.error("用户密码错误");
        } else {
            user.setPassword(newpassword);
            user.setStatus(false);
            userService.updateById(user);
        }
        return Result.success(user);
    }

    // 管理删除用户
    @DeleteMapping("/{userid}")
    public Result<String> deleteUser(@PathVariable Long userid) {
        userService.removeById(userid);
        return Result.success("User deleted successfully.");
    }
}
