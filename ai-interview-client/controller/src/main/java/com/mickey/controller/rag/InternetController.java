package com.mickey.controller.rag;


import com.mickey.Entity.ChatEntity;
import com.mickey.service.rag.ChatService;
import com.mickey.service.rag.SesrXngService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName HelloController
 * @Author 风间影月
 * @Version 1.0
 * @Description HelloController
 **/
@Slf4j
@RestController
@RequestMapping("internet")
public class InternetController {

    @Resource
    private SesrXngService sesrXngService;

    @Resource
    private ChatService chatService;

    @GetMapping("/test")
    public Object test(@RequestParam("query") String query){
        return sesrXngService.search(query);
    }

    @PostMapping("/search")
    public void search(@RequestBody ChatEntity chatEntity, HttpServletResponse response){
        log.info("✅ Controller方法已触发：/internet/search");
        response.setCharacterEncoding("UTF-8");
        chatService.doInternetSearch(chatEntity);
    }

}
