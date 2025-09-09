package com.mickey;

import com.mickey.tool.DateTool;
import com.mickey.tool.EmailTool;
import com.mickey.tool.PersonalTool;
import com.mickey.tool.QuestionLibTool;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @ClassName Application
 * @Author 风间影月
 * @Version 1.0
 * @Description Application
 **/
@MapperScan("com.mickey.mapper")
@SpringBootApplication
public class Application {

//    http://localhost:9060/sse

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * 注册MCP工具
     */
    @Bean
    public ToolCallbackProvider registMCPTools(DateTool dateTool, PersonalTool personalTool, EmailTool emailTool, QuestionLibTool questionLibTool) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(dateTool, personalTool, emailTool, questionLibTool)
                .build();
    }

}
