package com.coursemanagement.modules.file.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coursemanagement.modules.file.domain.FileInfo;
import com.coursemanagement.modules.file.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

public interface FileService extends IService<FileInfo> {

}
