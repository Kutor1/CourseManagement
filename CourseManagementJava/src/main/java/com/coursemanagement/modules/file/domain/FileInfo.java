package com.coursemanagement.modules.file.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

// 文件实体类
@Data
@TableName("fileinfo")
public class FileInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String filename;
    private String filepath;
}
