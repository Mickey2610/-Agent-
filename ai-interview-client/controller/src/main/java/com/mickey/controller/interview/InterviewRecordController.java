package com.mickey.controller.interview;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mickey.DTO.UpdatedScoreDTO;
import com.mickey.service.mq.InterviewRecordProducer;
import com.mickey.task.ChatGLMTask;
import com.mickey.DTO.SubmitAnswerDTO;
import com.mickey.task.DeepSeekTaskCustom;
import com.mickey.grace.result.GraceJSONResult;
import com.mickey.service.interview.InterviewRecordService;
import com.mickey.utils.PagedGridResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("interviewRecord")
public class InterviewRecordController {

    @Resource
    private ChatGLMTask chatGLMTask;

    @Resource
    private DeepSeekTaskCustom deepSeekTask;
    @Resource
    private InterviewRecordProducer interviewRecordProducer; // 注入生产者

    @Resource
    private InterviewRecordService interviewRecordService;
    @CacheEvict(cacheNames = "interviewRecordList", allEntries = true)
    @PostMapping("collect")
    public GraceJSONResult collect(@RequestBody SubmitAnswerDTO submitAnswerDTO) {
        try {
            // 使用生产者发送消息
            interviewRecordProducer.sendSubmitAnswerMessage(submitAnswerDTO);
            return GraceJSONResult.ok();
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return GraceJSONResult.error();
        }
    }

    @Cacheable(cacheNames = "interviewRecordList", keyGenerator = "interviewRecordKeyGenerator")
    @GetMapping("list")
    public GraceJSONResult list(@RequestParam String realName,
                                @RequestParam String mobile,
                                @RequestParam(defaultValue = "1", name = "page") Integer page,
                                @RequestParam(defaultValue = "10", name = "pageSize") Integer pageSize) {
        PagedGridResult result = interviewRecordService.queryList(realName, mobile, page, pageSize);
        return GraceJSONResult.ok(result);
    }

    @CacheEvict(cacheNames = "interviewRecordList", allEntries = true)
    @DeleteMapping("/delete")
    public GraceJSONResult delete(@RequestParam String interviewRecordId) {
        interviewRecordService.delete(interviewRecordId);
        return GraceJSONResult.ok();
    }

    @CacheEvict(cacheNames = "interviewRecordList", allEntries = true)
    @PutMapping("/updateScore")
    public GraceJSONResult updateScore(@RequestBody @Validated UpdatedScoreDTO updatedScoreDTO) {
        interviewRecordService.update(updatedScoreDTO);
        return GraceJSONResult.ok();
    }

}
