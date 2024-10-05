package com.coursemanagement.modules.course;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coursemanagement.modules.course.domain.Course;
import com.coursemanagement.modules.course.mapper.CourseMapper;
import com.coursemanagement.modules.course.service.CourseService;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
}
