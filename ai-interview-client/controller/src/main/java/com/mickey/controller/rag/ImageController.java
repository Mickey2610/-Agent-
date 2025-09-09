package com.mickey.controller.rag;

import com.mickey.Entity.ChatEntity;
import com.mickey.grace.result.GraceJSONResult;
import com.mickey.service.rag.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("image")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/analyze")
    public GraceJSONResult analyzeImage(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("currentUserName") String currentUserName,
            @RequestParam("message") String message,
            @RequestParam("botMsgId") String botMsgId) {
        // 打印请求是否到达
        log.info("✅ Controller方法已触发：/image/analyze");

        // 打印文本参数
        log.info("currentUserName: {}", currentUserName);
        log.info("message: {}", message);
        log.info("botMsgId: {}", botMsgId);

        // 打印上传的文件信息
        if (files != null && !files.isEmpty()) {
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                log.info("上传的文件[{}]：名称={}, 大小={} KB, 类型={}",
                        i + 1,
                        file.getOriginalFilename(),
                        file.getSize() / 1024,
                        file.getContentType());
            }
        } else {
            log.warn("⚠️ 没有上传任何文件");
        }

        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setCurrentUserName(currentUserName);
        chatEntity.setMessage(message);
        chatEntity.setBotMsgId(botMsgId);

        imageService.analyzeImage(files, chatEntity);
        return GraceJSONResult.ok();
    }
}
