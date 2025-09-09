package com.mickey.tool;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mickey.enums.ListSortEnum;
import com.mickey.mapper.InterviewerMapper;
import com.mickey.mapper.QuestionLibMapper;
import com.mickey.mapper.QuestionLibMapperCustom;
import com.mickey.pojo.Entity.Interviewer;
import com.mickey.pojo.Entity.QuestionLib;
import com.mickey.pojo.VO.QuestionLibVO;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class QuestionLibTool {

    @Resource
    private QuestionLibMapper questionLibMapper;

    @Resource
    private InterviewerMapper interviewerMapper;

    @Resource
    private QuestionLibMapperCustom questionLibMapperCustom;

    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateQuestionLibRequest {
        @ToolParam(description = "面试题内容，内容十五字以内")
        private String question;
        @ToolParam(description = "面试题参考答案")
        private String referenceAnswer;
        @ToolParam(description = "关联的数字人面试官名称（如小琳、Bob等）")
        private String aiName;
        @ToolParam(description = "关联的数字人面试官的视频")
        private String aiSrc;
    }

    @Tool(description = "创建新的面试题")
    public String createNewQuestionLib(CreateQuestionLibRequest request) {
        log.info("========== 调用MCP工具：createNewQuestionLib() ==========");
        log.info(String.format("| 参数 request 为： %s", request.toString()));
        log.info("========== End ==========");

        // 根据aiName查询面试官ID
        QueryWrapper<Interviewer> interviewerWrapper = new QueryWrapper<>();
        interviewerWrapper.eq("ai_name", request.getAiName());
        Interviewer interviewer = interviewerMapper.selectOne(interviewerWrapper);

        if (interviewer == null) {
            return "创建失败：找不到指定的数字人面试官";
        }

        QuestionLib questionLib = new QuestionLib();
        BeanUtils.copyProperties(request, questionLib);

        // 设置面试官ID
        questionLib.setInterviewerId(interviewer.getId());

        // 生成ID并设置时间
        questionLib.setId(RandomStringUtils.randomNumeric(12));
        questionLib.setCreateTime(LocalDateTime.now());
        questionLib.setUpdatedTime(LocalDateTime.now());
        questionLib.setIsOn(1); // 默认启用

        questionLibMapper.insert(questionLib);

        return "面试题创建成功";
    }

    // 根据面试官名称获取对应的视频资源地址
    @Transactional
    @Tool(description = "根据用户输入获取ai_src")
    public String getAiSrcByInterviewerName(String aiName) {

        return "";
    }

    @Transactional
    @Tool(description = "根据面试题删除面试题")
    public String deleteQuestionLib(String question) {
        log.info("========== 调用MCP工具：deleteQuestionLib() ==========");
        log.info(String.format("| 参数 question 为： %s", question));
        log.info("========== End ==========");

        QueryWrapper<QuestionLib> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("question", question);

        int delete = questionLibMapper.delete(queryWrapper);
        if (delete <= 0) {
            return "面试题删除失败，或面试题可能不存在";
        }
        return "面试题删除成功";
    }

    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryQuestionLibRequest {
        @ToolParam(description = "面试题ID", required = false)
        private String questionLibId;
        @ToolParam(description = "面试题内容", required = false)
        private String question;
        @ToolParam(description = "数字人面试官名称", required = false)
        private String aiName;
        @ToolParam(description = "是否启用（1:启用, 0:关闭）", required = false)
        private Integer isOn;
        @ToolParam(description = "排序方式（asc:升序, desc:降序）", required = false)
        private ListSortEnum sortEnum;
    }

    @Tool(description = "根据条件查询面试题列表")
    public List<QuestionLibVO> queryQuestionLibListByCondition(QueryQuestionLibRequest request) {
        log.info("========== 调用MCP工具：queryQuestionLibListByCondition() ==========");
        log.info(String.format("| 参数 request 为： %s", request.toString()));
        log.info("========== End ==========");

        Map<String, Object> paramMap = new HashMap<>();
        if (StringUtils.isNotBlank(request.getQuestion())) {
            paramMap.put("question", request.getQuestion());
        }
        if (StringUtils.isNotBlank(request.getAiName())) {
            paramMap.put("aiName", request.getAiName());
        }

        return questionLibMapperCustom.queryQuestionLibList(paramMap);
    }

    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModifyQuestionLibRequest {
        @ToolParam(description = "面试题ID", required = true)
        private String questionLibId;
        @ToolParam(description = "面试题内容", required = false)
        private String question;
        @ToolParam(description = "参考答案", required = false)
        private String referenceAnswer;
        @ToolParam(description = "数字人面试官名称", required = false)
        private String aiName;
        @ToolParam(description = "是否启用（1:启用, 0:关闭）", required = false)
        private Integer isOn;
    }

    @Tool(description = "修改面试题信息")
    public String modifyQuestionLib(ModifyQuestionLibRequest request) {
        log.info("========== 调用MCP工具：modifyQuestionLib() ==========");
        log.info(String.format("| 参数 request 为： %s", request.toString()));
        log.info("========== End ==========");

        QuestionLib questionLib = new QuestionLib();
        BeanUtils.copyProperties(request, questionLib);

        // 如果有修改面试官名称，需要更新interviewerId和aiSrc
        if (StringUtils.isNotBlank(request.getAiName())) {
            QueryWrapper<Interviewer> interviewerWrapper = new QueryWrapper<>();
            interviewerWrapper.eq("ai_name", request.getAiName());
            Interviewer interviewer = interviewerMapper.selectOne(interviewerWrapper);

            if (interviewer == null) {
                return "修改失败：找不到指定的数字人面试官";
            }

            questionLib.setInterviewerId(interviewer.getId());
            questionLib.setAiSrc(getAiSrcByInterviewerName(request.getAiName()));
        }

        questionLib.setUpdatedTime(LocalDateTime.now());

        QueryWrapper<QuestionLib> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", request.getQuestionLibId());

        int update = questionLibMapper.update(questionLib, queryWrapper);
        if (update <= 0) {
            return "面试题更新失败，或面试题可能不存在";
        }

        return "面试题更新成功";
    }

    @Tool(description = "启用或禁用面试题")
    public String toggleQuestionLibStatus(String questionLibId, Integer isOn) {
        log.info("========== 调用MCP工具：toggleQuestionLibStatus() ==========");
        log.info(String.format("| 参数 questionLibId: %s, isOn: %d", questionLibId, isOn));
        log.info("========== End ==========");

        QuestionLib questionLib = new QuestionLib();
        questionLib.setIsOn(isOn);
        questionLib.setUpdatedTime(LocalDateTime.now());

        QueryWrapper<QuestionLib> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", questionLibId);

        int update = questionLibMapper.update(questionLib, queryWrapper);
        if (update <= 0) {
            return "操作失败，面试题可能不存在";
        }

        return isOn == 1 ? "面试题已启用" : "面试题已禁用";
    }
}