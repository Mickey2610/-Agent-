package com.mickey.controller.interview;

import com.mickey.DTO.InterviewerDTO;
import com.mickey.grace.result.GraceJSONResult;
import com.mickey.service.interview.InterviewerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("interviewer")
public class InterviewerController {

    @Resource
    private InterviewerService interviewerService;


    /**
     * 创建或者更新数字人面试官信息
     *
     * @return
     */
    @PostMapping("createOrUpdate")
    public GraceJSONResult createOrUpdate(@Valid @RequestBody InterviewerDTO interviewerDTO) {
        interviewerService.createOrUpdate(interviewerDTO);
        return GraceJSONResult.ok();
    }

    /**
     * 查询所有面试官数据
     * @return
     */
    @GetMapping("list")
    public GraceJSONResult list() {
        return GraceJSONResult.ok(interviewerService.queryAll());
    }

    /**
     * 删除数字人面试官
     * @param interviewerId
     * @return
     */
    @DeleteMapping("/delete")
    public GraceJSONResult delete(@RequestParam String interviewerId) {
        interviewerService.delete(interviewerId);
        return GraceJSONResult.ok();
    }


}
