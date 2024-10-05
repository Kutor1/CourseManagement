package com.coursemanagement.modules.course.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

//course实体类
@Data
@TableName("courses")
public class Course {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String title;
    private String description;
    private String schedule; // 课程时间
    private Long teacher_id;  // 教师id
    private String teachername; //教师名称

}
