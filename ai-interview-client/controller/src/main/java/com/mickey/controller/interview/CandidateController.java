package com.mickey.controller.interview;

import com.mickey.DTO.CandidateDTO;
import com.mickey.Entity.Candidate;
import com.mickey.grace.result.GraceJSONResult;
import com.mickey.service.interview.CandidateService;
import com.mickey.utils.PagedGridResult;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("candidate")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @PostMapping("createOrUpdate")
    public GraceJSONResult createOrUpdate(@Valid @RequestBody CandidateDTO candidateDTO) {
        candidateService.createOrUpdate(candidateDTO);
        return GraceJSONResult.ok();
    }

    @GetMapping("list")
    public GraceJSONResult list(@RequestParam String realName,
                                @RequestParam String mobile,
                                @RequestParam(defaultValue = "1", name = "page") Integer page,
                                @RequestParam(defaultValue = "10", name = "pageSize") Integer pageSize) {

        PagedGridResult result = candidateService.queryList(realName, mobile, page, pageSize);
        return GraceJSONResult.ok(result);
    }

    @GetMapping("detail")
    public GraceJSONResult detail(@RequestParam String candidateId) {

        Candidate detail = candidateService.getDetail(candidateId);
        return GraceJSONResult.ok(detail);
    }

    @PostMapping("delete")
    public GraceJSONResult delete(@RequestParam String candidateId) {

        candidateService.delete(candidateId);
        return GraceJSONResult.ok();
    }
}
