package com.mickey.controller.rag;

import com.mickey.enums.SSEMsgType;
import com.mickey.utils.SSEServer;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @ClassName SSEController
 * @Author 风间影月
 * @Version 1.0
 * @Description SSEController
 **/
@RestController
@RequestMapping("sse")
public class SSEController {

    /**
     * @Description: 前端发送连接的请求，连接SSE服务
     * @Author 风间影月
     * @param
     * @return String
     */
    @GetMapping(path = "connect", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter connect(@RequestParam String userId){
        return SSEServer.connect(userId);
    }

    /**
     * @Description: SSE发送单个消息
     * @Author 风间影月
     * @param
     * @return String
     */
    @GetMapping("sendMessage")
    public Object sendMessage(@RequestParam String userId, @RequestParam String message){
        SSEServer.sendMsg(userId, message, SSEMsgType.MESSAGE);
        return "OK";
    }

    /**
     * @Description: SSE发送单个消息 - add
     * @Author 风间影月
     * @param userId
     * @param message
     * @return Object
     */
    @GetMapping("sendMessageAdd")
    public Object sendMessageAdd(@RequestParam String userId, @RequestParam String message) throws Exception {
        for (int i = 0; i < 10; i++) {
            Thread.sleep(200);
            SSEServer.sendMsg(userId, message, SSEMsgType.ADD);
        }
        return "OK";
    }

    /**
     * @Description: SSE发送群消息
     * @Author 风间影月
     * @param message
     * @return Object
     */
    @GetMapping("sendMessageAll")
    public Object sendMessageAll(@RequestParam String message){
        SSEServer.sendMsgToAllUsers(message);
        return "OK";
    }

}
