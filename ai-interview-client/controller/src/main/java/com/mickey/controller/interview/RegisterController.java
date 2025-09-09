package com.mickey.controller.interview;

import com.mickey.DTO.AdminInfoDTO;
import com.mickey.VO.AdminInfoVO;
import com.mickey.base.BaseInfoProperties;
import com.mickey.grace.result.GraceJSONResult;
import com.mickey.grace.result.ResponseStatusEnum;
import com.mickey.service.interview.RegisterService;
import com.mickey.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegisterController extends BaseInfoProperties {

    @Autowired
    private RegisterService registerService;

    /**
     * 用户注册接口
     *
     * @param adminInfoDTO 前端传来的注册信息
     * @return GraceJSONResult
     */
    @CacheEvict(cacheNames = "AdminMng", allEntries = true)
    @PostMapping("/doRegister")
    public GraceJSONResult doRegister(@Validated @RequestBody AdminInfoDTO adminInfoDTO) {
        // 1. 校验必填字段
        if (StringUtils.isBlank(adminInfoDTO.getUsername()) ||
                StringUtils.isBlank(adminInfoDTO.getPassword()) ||
                StringUtils.isBlank(adminInfoDTO.getMobile()) ||
                StringUtils.isBlank(adminInfoDTO.getRealName()) ||
                StringUtils.isBlank(adminInfoDTO.getEmail())) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_REGISTER_ERROR);
        }

        // 2. 校验密码一致
        if (!adminInfoDTO.getConfirmPassword().equals(adminInfoDTO.getPassword())) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
        }

        // 3. 检查用户名是否已存在（需要RegisterService实现）
        if (registerService.usernameExists(adminInfoDTO.getUsername())) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_ALREADY_EXIST_ERROR);
        }

        // 4. 保存用户
        AdminInfoVO adminInfoVO = registerService.saveAdmin(adminInfoDTO);
        System.out.println(JsonUtils.objectToJson(adminInfoVO));

        return GraceJSONResult.ok();
    }
}