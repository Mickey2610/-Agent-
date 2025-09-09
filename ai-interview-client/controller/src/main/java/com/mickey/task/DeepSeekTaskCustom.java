package com.mickey.task;

import com.mickey.DTO.AnswerDTO;
import com.mickey.DTO.SubmitAnswerDTO;
import com.mickey.Entity.InterviewRecord;
import com.mickey.Entity.Job;
import com.mickey.service.interview.InterviewRecordService;
import com.mickey.service.interview.JobService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
// sk-40f5937de2f04edbbd41e1a4f4e528d7

@Component
@Slf4j
public class DeepSeekTaskCustom {

    @Resource
    private JobService jobService;

    private ChatClient chatClient;
    @Resource
    private InterviewRecordService interviewRecordService;

    // 构造器注入，自动配置方式（推荐）
    public DeepSeekTaskCustom(OpenAiChatModel openAiChatModel) {
        this.chatClient =  ChatClient.builder(openAiChatModel).build();
    }

    @Async
    public void display(SubmitAnswerDTO submitAnswerDTO) {
        log.info("开始异步AI智能分析...");
//        System.out.println(submitAnswerBO);

        /********** 拼接提示词与回答的文字内容 **********/
        // 1. 获得职位提示词前缀
        String jobId = submitAnswerDTO.getJobId();
        Job job = jobService.getDetail(jobId);
        String prompt = job.getPrompt();

        // 2. 组装问题内容
        List<AnswerDTO> answerList = submitAnswerDTO.getQuestionAnswerList();
        String content = "";
        for (AnswerDTO answer : answerList) {
            content += "提问：" + answer.getQuestion() + "\n";
            content += "正确答案：" + answer.getReferenceAnswer() + "\n";
            content += "回答内容：" + answer.getAnswerContent() + "\n\n";
        }

        String submitContent = prompt + "\n\n" + content;
//        String scorePrompt = "请根据以下面试内容给候选人打分，直接返回0-100的整数分数，不要任何其他文字说明。\n\n" +
//                "面试内容:\n" + submitContent;

        System.out.println(submitContent);
        /********** 把内容提交到DeepSeek进行面试分析 **********/
        String finalResult = doChat(submitContent);
//        Integer score = getScore(scorePrompt);
        InterviewRecord record = new InterviewRecord();
        record.setCandidateId(submitAnswerDTO.getCandidateId());
        record.setTakeTime(submitAnswerDTO.getTotalSeconds());
        record.setJobName(job.getJobName());
        record.setAnswerContent(content);
        record.setResult(finalResult);
        record.setCreateTime(LocalDateTime.now());
        record.setUpdatedTime(LocalDateTime.now());
//        record.setScore(score);
        interviewRecordService.save(record);


        System.out.println("DeepSeek调用完成");
    }

    private String doChat(String submitContent) {
        return chatClient.prompt().user(submitContent).call().content();
    }

    private Integer getScore(String scorePrompt) {
        /********** 把面试分数单独存储在数据库中 **********/
        String finalScore = doChat(scorePrompt);
        int score;
        try {
            score = Integer.parseInt(finalScore.trim());
            // 确保分数在0-100范围内
            score = Math.max(0, Math.min(100, score));
        } catch (NumberFormatException e) {
            log.error("AI返回的分数格式无效: {}", finalScore);
            score = -1; // 或用默认值
        }
        return score;
    }


}