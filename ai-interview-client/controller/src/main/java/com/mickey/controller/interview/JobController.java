package com.mickey.controller.interview;

import com.mickey.DTO.JobDTO;
import com.mickey.Entity.Job;
import com.mickey.grace.result.GraceJSONResult;
import com.mickey.service.interview.JobService;
import com.mickey.utils.PagedGridResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("job")
public class JobController {

    @Resource
    private JobService jobService;

    @PostMapping("createOrUpdate")
    public GraceJSONResult createOrUpdate(@RequestBody JobDTO jobDTO) {
        jobService.createOrUpdate(jobDTO);
        return GraceJSONResult.ok();
    }

    @GetMapping("list")
    public GraceJSONResult list(
            @RequestParam(defaultValue = "1", name = "page") Integer page,
            @RequestParam(defaultValue = "10", name = "pageSize") Integer pageSize) {

        PagedGridResult result = jobService.queryList(page, pageSize);
        return GraceJSONResult.ok(result);
    }

    @GetMapping("detail")
    public GraceJSONResult detail(@RequestParam String jobId) {

        Job detail = jobService.getDetail(jobId);
        return GraceJSONResult.ok(detail);
    }

    @PostMapping("delete")
    public GraceJSONResult delete(@RequestParam String jobId) {

        jobService.delete(jobId);
        return GraceJSONResult.ok();
    }

    @GetMapping("nameList")
    public GraceJSONResult nameList() {
        List<HashMap<String, String>> results = jobService.nameList();
        return GraceJSONResult.ok(results);
    }
}
