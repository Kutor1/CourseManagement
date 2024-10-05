package com.coursemanagement.modules.file.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coursemanagement.common.Result;
import com.coursemanagement.modules.file.FileServiceImpl;
import com.coursemanagement.modules.file.domain.FileInfo;
import com.coursemanagement.modules.file.service.FileService;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

// 文件业务处理
@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    FileService fileService;

    @Autowired
    FileServiceImpl fileServiceImpl;

//    private final String uploadDir = "C:/Users/duanh/Desktop/test/"; // 本地测试环境

    private final String uploadDir = "/www/filedata/"; // 服务器环境

    private final Tika tika = new Tika();

    // 上传文件
    @PostMapping
    public Result<String> uploadFile (@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return Result.error("Upload failed, file is empty.");
        }

        // 读取文件信息
        String fileName = file.getOriginalFilename();
        String filePath = uploadDir + fileName;
        File dest = new File(filePath);
        file.transferTo(dest);

        // 查找是否有同名文件，禁止上传同名文件。
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("filename",fileName);
        if (fileService.getOne(queryWrapper) != null) {
            return Result.error("请勿上传同名文件");
        }

        FileInfo fileInfo = new FileInfo();
        fileInfo.setFilename(fileName);
        fileInfo.setFilepath(filePath);
        fileService.save(fileInfo);

        return Result.success("Upload successful.");
    }

    // 获取文件列表
    @GetMapping
    public Result<List> getFilesList () {
        return Result.success(fileService.list());
    }


    // 下载文件
    @GetMapping("/{id}")
    public ResponseEntity<FileSystemResource> downloadFile (@PathVariable Long id) throws IOException {
        FileInfo fileInfo = fileService.getById(id);
        if (fileInfo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        File file = new File(fileInfo.getFilepath());
        if (!file.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();

// 使用 MimeUtility 编码文件名，确保空格和特殊字符正确处理
//        String encodedFilename = MimeUtility.encodeText(fileInfo.getFilename(), "UTF-8", "B");


//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"");
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileInfo.getFilename());

        // 文件名使用 UTF-8 编码
        String filename = fileInfo.getFilename();
        String encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");

        // 设置 Content-Disposition 头
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"; filename*=UTF-8''" + encodedFilename);

        // Use Tika to detect MIME type
        String mimeType = tika.detect(file);
        headers.add(HttpHeaders.CONTENT_TYPE, mimeType);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new FileSystemResource(file));
    }

    // 文件删除
    @DeleteMapping
    public Result<String> deleteFile (@RequestParam String filepath) {
        boolean result = fileServiceImpl.deleteFileByPath(filepath);
        if (result) {
            return Result.success("文件删除成功");
        } else {
            return Result.error("文件删除失败或不存在");
        }
    }
}

