package com.mickey.controller.rag;

import com.mickey.service.rag.ChatService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @ClassName HelloController
 * @Author 风间影月
 * @Version 1.0
 * @Description HelloController
 **/
@RestController
@RequestMapping("hello")
public class Hello2Controller {

    // http://127.0.0.1:8080/hello/world

    @Resource
    private ChatService chatService;

    @GetMapping("world")
    public String world(){
        return "Hello Mickey!";
    }

    @GetMapping("chat")
    public String chat(String msg){
        return chatService.chatTest(msg);
    }

    @GetMapping("chat/stream/response")
    public Flux<ChatResponse> chatStreamResponse(String msg, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        return chatService.streamResponse(msg);
    }

    @GetMapping("chat/stream/str")
    public Flux<String> chatStreamStr(String msg, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        return chatService.streamStr(msg);
    }

}
