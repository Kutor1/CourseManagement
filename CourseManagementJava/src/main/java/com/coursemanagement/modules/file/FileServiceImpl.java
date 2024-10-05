package com.coursemanagement.modules.file;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coursemanagement.modules.file.domain.FileInfo;
import com.coursemanagement.modules.file.mapper.FileMapper;
import com.coursemanagement.modules.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, FileInfo> implements FileService {
    @Autowired
    private FileMapper fileInfoMapper;

    // 开启事务
    @Transactional
    public boolean deleteFileByPath(String filepath) {
        // 查询文件信息
        FileInfo fileInfo = fileInfoMapper.selectOne(new QueryWrapper<FileInfo>().eq("filepath", filepath));
        if (fileInfo != null) {
            // 删除数据库记录
            int rows = fileInfoMapper.deleteById(fileInfo.getId());
            if (rows > 0) {
                // 尝试删除实际的文件
                File file = new File(filepath);
                if (file.exists()) {
                    return file.delete(); // 返回文件删除结果
                }
                return true; // 如果文件不存在，则只删除数据库记录
            }
        }
        return false; // 如果文件信息不存在或删除失败，返回 false
    }
}
