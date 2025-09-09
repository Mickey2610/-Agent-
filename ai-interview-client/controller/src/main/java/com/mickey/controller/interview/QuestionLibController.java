package com.mickey.controller.interview;

import com.mickey.DTO.QuestionLibDTO;
import com.mickey.VO.InitQuestionLibVO;
import com.mickey.base.BaseInfoProperties;
import com.mickey.enums.YesOrNo;
import com.mickey.grace.result.GraceJSONResult;
import com.mickey.grace.result.ResponseStatusEnum;
import com.mickey.service.interview.QuestionLibService;
import com.mickey.utils.PagedGridResult;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("questionLib")
public class QuestionLibController extends BaseInfoProperties {
    @Resource
    private QuestionLibService questionLibService;

    /**
     * 创建或更新题库
     * @param questionLibDTO
     * @return
     */
    @PostMapping("createOrUpdate")
    public GraceJSONResult createOrUpdate(@RequestBody QuestionLibDTO questionLibDTO) {
        questionLibService.createOrUpdate(questionLibDTO);
        return GraceJSONResult.ok();
    }

    /**
     * 分页查询题库列表
     * @param aiName
     * @param question
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("list")
    public GraceJSONResult list(
            @RequestParam String aiName,
            @RequestParam String question,
            @RequestParam(defaultValue = "1", name = "page") Integer page,
            @RequestParam(defaultValue = "10", name = "pageSize") Integer pageSize) {

        PagedGridResult result = questionLibService.queryList(aiName, question, page, pageSize);
        return GraceJSONResult.ok(result);
    }

    @PostMapping("show")
    public GraceJSONResult show(@RequestParam String questionLibId) {
        if (StringUtils.isBlank(questionLibId)) {
            return GraceJSONResult.error();
        }
        questionLibService.setDisplayOrNot(questionLibId, YesOrNo.YES.type);
        return GraceJSONResult.ok();
    }

    @PostMapping("hide")
    public GraceJSONResult hide(@RequestParam String questionLibId) {
        if (StringUtils.isBlank(questionLibId)) {
            return GraceJSONResult.error();
        }
        questionLibService.setDisplayOrNot(questionLibId, YesOrNo.NO.type);
        return GraceJSONResult.ok();
    }

    @PostMapping("delete")
    public GraceJSONResult delete(@RequestParam String questionLibId) {
        questionLibService.delete(questionLibId);
        return GraceJSONResult.ok();
    }

    @GetMapping("prepareQuestion")
    public GraceJSONResult prepareQuestion(@RequestParam String candidateId) {
        String candidateInfo = redis.get(REDIS_USER_INFO+":"+candidateId);
        String userToken = redis.get(REDIS_USER_TOKEN+":"+candidateId);
        if (StringUtils.isBlank(candidateInfo) || StringUtils.isBlank(userToken)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_INFO_NOT_EXIST_ERROR);
        }
        List<InitQuestionLibVO> result = questionLibService.getInitQuestionLib(candidateId,3);
        return GraceJSONResult.ok(result);
    }
}
