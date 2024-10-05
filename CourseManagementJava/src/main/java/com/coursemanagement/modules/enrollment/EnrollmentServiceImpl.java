package com.coursemanagement.modules.enrollment;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coursemanagement.modules.enrollment.domain.Enrollment;
import com.coursemanagement.modules.enrollment.mapper.EnrollmentMapper;
import com.coursemanagement.modules.enrollment.service.EnrollmentService;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentServiceImpl extends ServiceImpl<EnrollmentMapper, Enrollment> implements EnrollmentService {

}
