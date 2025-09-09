package com.mickey.controller.rag;

import com.mickey.Entity.ChatEntity;
import com.mickey.service.rag.ChatService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName HelloController
 * @Author 风间影月
 * @Version 1.0
 * @Description HelloController
 **/
@Slf4j
@RestController
@RequestMapping("chat")
public class ChatController {


    @Resource
    private ChatService chatService;

    @PostMapping("doChat")
    public void doChat(@RequestBody ChatEntity chatEntity){
        // 打印请求是否到达
        log.info("✅ Controller方法已触发：/chat/doChat");
        chatService.doChat(chatEntity);
    }

}
