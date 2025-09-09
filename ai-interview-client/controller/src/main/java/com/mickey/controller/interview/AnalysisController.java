package com.mickey.controller.interview;

import com.mickey.VO.CandidateReportVO;
import com.mickey.VO.Top10VO;
import com.mickey.grace.result.GraceJSONResult;
import com.mickey.grace.result.ResponseStatusEnum;
import com.mickey.service.interview.AnalysisService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;

/**
 * 面试数据分析接口
 */
@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    /**
     * 获取面试分数排名
     *
     * @return { "top5": [...], "top10": [...] }
     */
    @GetMapping("/interviewTopScores")
    public GraceJSONResult getInterviewTopScores(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end)
    {
        Top10VO interviewTopScores = analysisService.getInterviewTopScores(begin, end);
        return GraceJSONResult.ok(interviewTopScores);
    }

    /**
     * 获取近期应聘者新增数据
     * @return { "7days": [...], "30days": [...] }
     */
    @GetMapping("/candidateGrowth")
    public GraceJSONResult getRecentCandidateGrowth(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        CandidateReportVO candidateReportVO = analysisService.getCandidateReport(begin, end);
        return GraceJSONResult.ok(candidateReportVO);
    }

    @GetMapping("/report")
    public void exportAnalysisReport(
            HttpServletResponse response,
            @RequestParam(name = "days", defaultValue = "7") int days) {

        LocalDate begin = LocalDate.now().minusDays(days);
        LocalDate end = LocalDate.now();

        try {
            XSSFWorkbook workbook = analysisService.exportAnalysisReport(begin, end);
//            String fileName = "分析报表_" + begin + "_至_" + end + ".xlsx";

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            workbook.write(response.getOutputStream());
            workbook.close();

        } catch (Exception e) {
            log.error("导出报表失败", e);
            GraceJSONResult.errorCustom(ResponseStatusEnum.USER_NAME_NOT_EXIST_ERROR);
        }
    }
}