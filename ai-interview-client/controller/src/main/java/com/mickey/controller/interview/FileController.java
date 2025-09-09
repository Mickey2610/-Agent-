package com.mickey.controller.interview;

import com.mickey.base.MinIOConfig;
import com.mickey.base.MinIOUtils;
import com.mickey.grace.result.GraceJSONResult;
import com.mickey.grace.result.ResponseStatusEnum;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("file")
public class FileController {

    @Resource
    private MinIOConfig minIOConfig;

    @PostMapping("uploadInterviewerImage")
    public GraceJSONResult uploadInterviewerImage(@RequestParam("file") MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }
        filename = "interviewer/" + dealWithoutFileName(filename);
        String imageUrl = MinIOUtils.uploadFile(minIOConfig.getBucketName(),
                filename,
                file.getInputStream(),
                true);
        return GraceJSONResult.ok(imageUrl);
    }

    @PostMapping("uploadInterviewVideo")
    public GraceJSONResult uploadInterviewerVideo(@RequestParam("file") MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }
        filename = "interviewerVideo/" + dealWithoutFileName(filename);
        String videoUrl = MinIOUtils.uploadFile(minIOConfig.getBucketName(),
                filename,
                file.getInputStream(),
                true);
        return GraceJSONResult.ok(videoUrl);
    }

    public String dealWithFileName(String fileName) {
        String suffixName = fileName.substring(fileName.lastIndexOf(".")); //获取图片后缀名
        String fileName2 = fileName.substring(0, fileName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString(); //给予文件随机名称
        return fileName2 + "_" + uuid + suffixName;
    }

    public String dealWithoutFileName(String fileName) {
        String suffixName = fileName.substring(fileName.lastIndexOf(".")); //获取图片后缀名
        String uuid = UUID.randomUUID().toString(); //给予文件随机名称
        return uuid + suffixName;
    }
}
