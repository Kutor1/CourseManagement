package com.coursemanagement.modules.course.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coursemanagement.common.Result;
import com.coursemanagement.modules.course.domain.Course;
import com.coursemanagement.modules.course.service.CourseService;
import com.coursemanagement.modules.enrollment.domain.Enrollment;
import com.coursemanagement.modules.enrollment.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// 处理课程业务
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    // 获取当前课程详情(courseid)
    @GetMapping("/{id}")
    public Result<Course> getCourseById(@PathVariable Long id) {
        Course course = courseService.getById(id);
        return Result.success(course);
    }

    // 获取当前教师的课程信息，根据请求头中的 teacher_id 获取课程列表
    @GetMapping
    public Result<List<Course>> getCoursesByTeacherId(@RequestHeader("teacher_id") Long teacherId) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id", teacherId);
        List<Course> courses = courseService.list(queryWrapper);
        return Result.success(courses);
    }

    // 根据搜索内容查询该教师的课程
    @GetMapping("/search/{searchText}")
    public  Result<List<Course>> getSearchCourses(@RequestHeader("teacher_id") Long teacherId, @PathVariable String searchText) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        // 根据教师id确定搜索范围
        queryWrapper.eq("teacher_id", teacherId);
        queryWrapper.like("title", searchText);
        List<Course> courses = courseService.list(queryWrapper);
        return Result.success(courses);
    }

    // 获取所有可选择的课程信息
    @GetMapping("/getAllCourses/{studentid}")
    public Result<List<Map<String, Object>>> getAllCourses(@PathVariable Long studentid) {

        // 获取学生已选课程的 enrollment 列表
        QueryWrapper<Enrollment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("studentid", studentid);
        List<Enrollment> enrollments = enrollmentService.list(queryWrapper);

        // 获取学生已选课程的 course_id 列表
        Set<Long> enrolledCourseIds = enrollments.stream()
                .map(Enrollment::getCourseid)
                .collect(Collectors.toSet());

        // 获取所有课程，并排除学生已选课程
        List<Map<String, Object>> courses = courseService.listMaps();
        List<Map<String, Object>> availableCourses = courses.stream()
                .filter(course -> !enrolledCourseIds.contains((Long) course.get("id")))
                .collect(Collectors.toList());

        return Result.success(availableCourses);
    }

    // 搜索可选课程
    @GetMapping("/getAvailableCourses/{searchText}")
    public Result<List<Map<String, Object>>> getAvailableCourses(@RequestHeader("studentid") Long studentid, @PathVariable String searchText) {

        // 获取学生已选课程的 course_id 列表
        QueryWrapper<Enrollment> enrollmentQuery = new QueryWrapper<>();
        enrollmentQuery.eq("studentid", studentid).select("courseid");
        List<Enrollment> enrollments = enrollmentService.list(enrollmentQuery);
        Set<Long> enrolledCourseIds = enrollments.stream()
                .map(Enrollment::getCourseid)
                .collect(Collectors.toSet());

        // 如果没有已选课程，enrolledCourseIds 为空时，跳过这个条件
        QueryWrapper<Course> courseQuery = new QueryWrapper<>();
        if (!enrolledCourseIds.isEmpty()) {
            courseQuery.notIn("id", enrolledCourseIds);
        }

        // 模糊匹配课程名称
        if (searchText != null && !searchText.trim().isEmpty()) {
            courseQuery.like("title", searchText.trim());
        }

        // 获取符合条件的课程列表
        List<Map<String, Object>> availableCourses = courseService.listMaps(courseQuery);

        return Result.success(availableCourses);
    }


    // 创建新课程
    @PostMapping("/{teachername}")
    public Result<Course> createCourse(@RequestBody Course course, @RequestHeader("teacher_id") Long teacherId, @PathVariable String teachername) {
        course.setTeacher_id(teacherId);
        course.setTeachername(teachername);
        courseService.save(course);
        return Result.success(course);
    }

    // 教师管理更新现有课程
    @PutMapping("/{id}")
    public Result<Course> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        // 对courses数据库更新
        course.setId(id);
        courseService.updateById(course);

        // 对enrollments数据库更新，对学生已选课程信息更新
        Enrollment enrollment = new Enrollment();
        enrollment.setCourseid(course.getId());
        enrollment.setDescription(course.getDescription());
        enrollment.setTitle(course.getTitle());
        enrollment.setSchedule(course.getSchedule());
        enrollment.setTeachername(course.getTeachername());

        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("courseid",course.getId());
        enrollmentService.update(enrollment,queryWrapper);
        return Result.success(course);
    }

    // 教师删除课程
    @DeleteMapping("/{id}")
    public Result<String> deleteCourse(@PathVariable Long id) {
        courseService.removeById(id);
        return Result.success("Course deleted successfully.");
    }
}

