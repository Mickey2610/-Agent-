package com.mickey.controller.interview;

import com.mickey.DTO.AdminInfoDTO;
import com.mickey.DTO.ChangePasswordDTO;
import com.mickey.Entity.AdminInfo;
import com.mickey.JwtClaimsConstant;
import com.mickey.VO.AdminInfoVO;
import com.mickey.config.JwtProperties;
import com.mickey.grace.result.GraceJSONResult;
import com.mickey.grace.result.ResponseStatusEnum;
import com.mickey.mapper.RegisterMapper;
import com.mickey.mapper.RegisterMapperCustom;
import com.mickey.service.interview.AdminProfileService;
import com.mickey.utils.JsonUtils;
import com.mickey.utils.JwtUtil;
import com.mickey.utils.RedisOperator;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import static com.mickey.base.BaseInfoProperties.REDIS_ADMIN_INFO;

@Slf4j
@RestController
@RequestMapping("admin")
public class AdminProfileController {

    @Resource
    private RedisOperator redis;
    @Autowired
    private AdminProfileService adminProfileService;

    @GetMapping("profile")
    public GraceJSONResult getAdminInfo(HttpServletRequest request) {
        AdminInfoVO adminInfoVO = adminProfileService.setProfile(request);
        if (adminInfoVO == null) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.TICKET_INVALID);
        }
        return GraceJSONResult.ok(adminInfoVO);
    }

    @PostMapping("updateProfile")
    public GraceJSONResult updateProfile(HttpServletRequest request, @RequestBody AdminInfoDTO updateInfoDTO) {

        AdminInfoVO updatedInfoVO = adminProfileService.updateProfile(request, updateInfoDTO);
        if (updatedInfoVO == null) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.TICKET_INVALID);
        }
        return GraceJSONResult.ok();
    }

    @PostMapping("changePassword")
    public GraceJSONResult changePassword(HttpServletRequest request,
                                          @RequestBody ChangePasswordDTO changePasswordDTO) {
        Integer flag = adminProfileService.changePassword(request, changePasswordDTO);
        if (flag == 1) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.PASSWORD_NOT_EQUAL_USER_NAME_ERROR);
        } else if (flag == 2) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.PASSWORD_EQUAL_CONFIRM_PASSWORD_ERROR);
        } else if (flag == 3) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.NEW_PASSWORD_NOT_EQUAL_CONFIRM_PASSWORD_ERROR);
        } else {
            return GraceJSONResult.ok();
        }
    }
}
