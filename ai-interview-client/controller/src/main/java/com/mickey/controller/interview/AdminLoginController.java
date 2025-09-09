package com.mickey.controller.interview;

import com.mickey.DTO.AdminLoginDTO;

import com.mickey.Entity.AdminInfo;
import com.mickey.JwtClaimsConstant;
import com.mickey.VO.AdminInfoVO;
import com.mickey.VO.AdminLoginVO;
import com.mickey.config.JwtProperties;
import com.mickey.mapper.RegisterMapper;
import com.mickey.mapper.RegisterMapperCustom;
import com.mickey.service.interview.AdminLoginService;
import com.mickey.service.interview.RegisterService;
import com.mickey.utils.JsonUtils;
import com.mickey.utils.JwtUtil;
import com.mickey.grace.result.GraceJSONResult;
import com.mickey.grace.result.ResponseStatusEnum;
import com.mickey.utils.RedisOperator;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.mickey.base.BaseInfoProperties.REDIS_ADMIN_INFO;

@Slf4j
@RestController
@RequestMapping("admin")
public class AdminLoginController {

    @Resource
    public RedisOperator redis;

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private AdminLoginService adminLoginService;
    @Autowired
    private RegisterService registerService;
    @Autowired
    private RegisterMapperCustom registerMapperCustom;
    @Autowired
    private RegisterMapper registerMapper;


    @PostMapping("login")
    public GraceJSONResult login(@Validated @RequestBody AdminLoginDTO adminLoginDTO) {
        String username = adminLoginDTO.getUsername();
        String password = adminLoginDTO.getPassword();

        // 1. 验证逻辑用户名是否存在
        if (!(registerService.usernameExists(username))) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_NAME_NOT_EXIST_ERROR);
        }
        // 2. 验证用户名密码是否匹配
        if (!(adminLoginService.passwordMatch(password, username))) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.PASSWORD_NOT_EQUAL_USER_NAME_ERROR);
        }

        // 3. 判断用户是否可以访问
        AdminInfo adminInfo = registerMapperCustom.selectAdminInfoByName(username);
        if (!adminInfo.getIsEnabled()) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_FORBIDDEN_ERROR);
        }
        adminInfo.setLastLoginTime(LocalDateTime.now());
        registerMapper.updateById(adminInfo);

        // 4. 生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.ADMIN_ID, Long.parseLong(adminInfo.getId()));
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);
        log.info("本次登录生成的JWT token为: {}", token);

        // 5. 构建响应VO
        AdminInfoVO adminInfoVO = new AdminInfoVO();
        BeanUtils.copyProperties(adminInfo, adminInfoVO);

        AdminLoginVO result = AdminLoginVO.builder()
                .currentRole(adminInfoVO.getRole())
                .token(token)
                .userInfo(adminInfoVO)
                .build();
        redis.set(REDIS_ADMIN_INFO + ":" + token, JsonUtils.objectToJson(adminInfoVO), 3 * 60 * 60);
        return GraceJSONResult.ok(result);
    }

    @PostMapping("/logout")
    public GraceJSONResult logout(HttpServletRequest request) {
        adminLoginService.deleteRedis(request);
        return GraceJSONResult.ok();
    }
}