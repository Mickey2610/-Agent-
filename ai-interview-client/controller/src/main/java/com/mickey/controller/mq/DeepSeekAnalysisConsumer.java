package com.mickey.controller.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mickey.DTO.AnswerDTO;
import com.mickey.DTO.SubmitAnswerDTO;
import com.mickey.Entity.InterviewRecord;
import com.mickey.Entity.Job;
import com.mickey.service.interview.InterviewRecordService;
import com.mickey.service.interview.JobService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * RocketMQ 消费者，用于处理面试答案分析任务
 */
@Component
@RocketMQMessageListener(
        topic = "DEEPSEEK_ANALYSIS_TOPIC", // 监听的 Topic
        consumerGroup = "deepseek-analysis-consumer-group" // 消费者组名
)
@Slf4j
public class DeepSeekAnalysisConsumer implements RocketMQListener<String> {

    @Autowired
    private JobService jobService;

    @Autowired
    private InterviewRecordService interviewRecordService;

    private final ChatClient chatClient;

    // 通过构造器注入 OpenAiChatModel (或你实际使用的 AI 模型 Bean)
    public DeepSeekAnalysisConsumer(OpenAiChatModel openAiChatModel) {
        this.chatClient = ChatClient.builder(openAiChatModel).build();
    }

    @Override
    public void onMessage(String message) {
        log.info("收到面试分析消息: {}", message);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 1. 反序列化消息，得到 SubmitAnswerDTO
            SubmitAnswerDTO submitAnswerDTO = objectMapper.readValue(message, SubmitAnswerDTO.class);

            // 2. 执行原来 display 方法中的核心逻辑
            processInterviewAnalysis(submitAnswerDTO);

            log.info("面试分析处理完成，候选人ID: {}", submitAnswerDTO.getCandidateId());

        } catch (JsonProcessingException e) {
            log.error("解析 RocketMQ 消息失败，消息内容: {}", message, e);
            // TODO: 处理反序列化错误
        } catch (Exception e) {
            log.error("处理面试分析消息时发生异常", e);
            // TODO: 处理业务逻辑异常，可能需要重试或告警
        }
    }

    // 将原来 display 方法的核心逻辑提取出来
    private void processInterviewAnalysis(SubmitAnswerDTO submitAnswerDTO) {
        // 1. 获得职位提示词前缀
        String jobId = submitAnswerDTO.getJobId();
        Job job = jobService.getDetail(jobId);
        if (job == null) {
            log.warn("职位信息未找到，jobId: {}", jobId);
            return;
        }
        String prompt = job.getPrompt();

        // 2. 组装问题内容
        List<AnswerDTO> answerList = submitAnswerDTO.getQuestionAnswerList(); // 确保 AnswerDTO 也可序列化
        StringBuilder content = new StringBuilder();
        for (AnswerDTO answer : answerList) {
            content.append("提问：").append(answer.getQuestion()).append("\n");
            content.append("正确答案：").append(answer.getReferenceAnswer()).append("\n");
            content.append("回答内容：").append(answer.getAnswerContent()).append("\n\n");
        }

        String submitContent = prompt + "\n\n" + content.toString();
        log.debug("提交给 AI 的内容: {}", submitContent);

        // 3. 把内容提交到 DeepSeek 进行面试分析
        String finalResult = doChat(submitContent);

        // 4. 保存面试记录
        InterviewRecord record = new InterviewRecord();
        record.setCandidateId(submitAnswerDTO.getCandidateId());
        record.setTakeTime(submitAnswerDTO.getTotalSeconds());
        record.setJobName(job.getJobName());
        record.setAnswerContent(content.toString());
        record.setResult(finalResult);
        record.setCreateTime(LocalDateTime.now());
        record.setUpdatedTime(LocalDateTime.now());
        interviewRecordService.save(record);

        log.info("面试记录已保存，候选人ID: {}", submitAnswerDTO.getCandidateId());
    }

    private String doChat(String submitContent) {
        // 使用 Spring AI 的 ChatClient 调用模型
        return chatClient.prompt().user(submitContent).call().content();
    }
}