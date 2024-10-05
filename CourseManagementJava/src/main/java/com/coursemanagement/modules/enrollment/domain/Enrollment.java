package com.coursemanagement.modules.enrollment.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

// enrollment实体类
@Data
@TableName("enrollments") // 指定数据库表单
public class Enrollment {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("studentid")
    private Long studentid;
    @TableField("courseid")
    private Long courseid;
    private String title;
    private String schedule;
    private String description;
    private String teachername;
    private Boolean status;

}
