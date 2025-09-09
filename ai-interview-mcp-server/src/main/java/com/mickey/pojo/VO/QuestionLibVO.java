package com.mickey.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QuestionLibVO {

    private String InterviewerId;

    private String questionLibId;

    private String question;

    private String referenceAnswer;

    private String aiSrc;

    private String interviewerName;

    private Integer isOn;

    private LocalDateTime createTime;

    private LocalDateTime updatedTime;
}
