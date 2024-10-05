package com.coursemanagement.modules.enrollment.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coursemanagement.common.Result;
import com.coursemanagement.modules.course.domain.Course;
import com.coursemanagement.modules.enrollment.domain.Enrollment;
import com.coursemanagement.modules.enrollment.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// enrollment业务处理
@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    // 获取学生当前已选择的所有课程
    @GetMapping
    public Result<List<Enrollment>> getAllEnrollments(@RequestHeader("student_id") Long studentid) {
        QueryWrapper<Enrollment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("studentid", studentid);
//        queryWrapper.ne("status", true); // 排除 status 为 true 的记录
        List<Enrollment> enrollments = enrollmentService.list(queryWrapper);
        return Result.success(enrollments);
    }


    //选课
    @PostMapping
    public Result<Enrollment> createEnrollment(@RequestBody Enrollment enrollment) {
            enrollmentService.save(enrollment);
            return Result.success(enrollment);
    }


    // 退课
    @DeleteMapping("/{enrollid}")
    public Result<String> deleteEnrollment(@PathVariable Long enrollid) {
        enrollmentService.removeById(enrollid);
        return Result.success("Enrollment deleted successfully.");
    }
}
