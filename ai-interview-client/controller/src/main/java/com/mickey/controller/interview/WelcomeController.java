package com.mickey.controller.interview;

import com.mickey.DTO.VerifySMSDTO;
import com.mickey.Entity.Candidate;
import com.mickey.VO.CandidateVO;
import com.mickey.base.BaseInfoProperties;
import com.mickey.grace.result.GraceJSONResult;
import com.mickey.grace.result.ResponseStatusEnum;
import com.mickey.mapper.CandidateMapper;
import com.mickey.mapper.CandidateMapperCustom;
import com.mickey.mapper.InterviewerMapper;
import com.mickey.service.interview.CandidateService;
import com.mickey.service.interview.InterviewRecordService;
import com.mickey.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(("welcome"))
public class WelcomeController extends BaseInfoProperties {

    private final CandidateService candidateService;
    private final InterviewRecordService interviewRecordService;
    private final InterviewerMapper interviewerMapper;
    private final CandidateMapperCustom candidateMapperCustom;
    private final CandidateMapper candidateMapper;

    public WelcomeController(CandidateService candidateService, InterviewRecordService interviewRecordService, InterviewerMapper interviewerMapper, CandidateMapperCustom candidateMapperCustom, CandidateMapper candidateMapper) {
        super();
        this.candidateService = candidateService;
        this.interviewRecordService = interviewRecordService;
        this.interviewerMapper = interviewerMapper;
        this.candidateMapperCustom = candidateMapperCustom;
        this.candidateMapper = candidateMapper;
    }

    /**
     * 获得短信验证码
     *
     * @param mobile
     * @return
     */
    @PostMapping("getSMSCode")
    public GraceJSONResult getSMSCode(@RequestParam String mobile) {

        if (StringUtils.isBlank(mobile)) {
            return GraceJSONResult.error();
        }
        /*
        Math.random() - 生成一个 [0,1) 之间的随机 double 值
        Math.random() * 9 - 将随机数范围扩展到 [0,9)
        Math.random() * 9 + 1 - 将范围移动到 [1,10)，确保第一位数字不为 0
        (Math.random() * 9 + 1) * 100000 - 乘以 100000 将数字变成 6 位数范围 [100000, 999999]
        (int) - 将 double 结果强制转换为整数，去掉小数部分
        String.valueOf() - 将整数转换为字符串表示形式
         */
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        System.out.println("=== 发送验证码 ===");
        System.out.println("本次生成的随机6位数密码是：" + code);
        System.out.println("===============");
        Candidate candidate = candidateMapperCustom.selectCandidateByMobile(mobile);
        candidate.setRandomPassword(code);
        candidateMapper.updateById(candidate);
        // 把验证码存入redis中，用于后续进入面试的校验
//        redis.set(MOBILE_SMSCODE + ":" + mobile, code, 5 * 60);

        return GraceJSONResult.ok();
    }

    @PostMapping("verify")
    public GraceJSONResult verify(@Validated @RequestBody VerifySMSDTO verifySMSDTO) {
        String mobile = verifySMSDTO.getMobile();
        String code = verifySMSDTO.getSmsCode();

        // 1. 从Redis获得验证码进行校验判断是否匹配
//        String redisCode = redis.get(MOBILE_SMSCODE + ":" + mobile);
        Candidate candidate1 = candidateMapperCustom.selectCandidateByMobile(mobile);
        String verifyCode =candidate1.getRandomPassword();
        if ((StringUtils.isBlank(verifyCode)) || !verifyCode.equalsIgnoreCase(code)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }
        // 2. 根据mobile查询数据库，判断用户是否存在，是否是候选人
        Candidate candidate = candidateService.queryMobileIsExist(mobile);

        if (candidate == null) {
            // 2.1 如果查询的用户为空，则表示该用户不是候选人，无法进入面试流程
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_INFO_NOT_EXIST_ERROR);
        } else {
            // 2.2 如果不为空，则需要判断用户是否已经面试过，如果面试过，则无法再次面试
//            boolean isExist = interviewRecordService.isCandidateRecordExist(candidate.getId());
//            if (isExist) {
//                return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_ALREADY_DID_INTERVIEW_ERROR);
//            }
        }
        // 3. 保存用户token信息，保存分布式会话到Redis中
        String uToken = UUID.randomUUID().toString();
        redis.set(REDIS_USER_TOKEN + ":" + candidate.getId(), uToken);
        // 4. 用户进入面试流程后（登录以后），删除Redis中的验证码
//        redis.del(MOBILE_SMSCODE + ":" + mobile);

        // 5. 返回用户信息给前端
        CandidateVO candidateVO = new CandidateVO();
        BeanUtils.copyProperties(candidate, candidateVO);
        candidateVO.setUserToken(uToken);
        candidateVO.setCandidateId(candidate.getId());

        // 6. 应聘者的会话信息保存3小时
        redis.set(REDIS_USER_INFO + ":" + candidate.getId(), JsonUtils.objectToJson(candidateVO), 3 * 60 * 60);

        return GraceJSONResult.ok(candidateVO);
    }
}
