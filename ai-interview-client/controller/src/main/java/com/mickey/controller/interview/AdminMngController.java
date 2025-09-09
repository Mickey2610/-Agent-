package com.mickey.controller.interview;

import com.mickey.Entity.AdminInfo;
import com.mickey.grace.result.GraceJSONResult;
import com.mickey.service.interview.AdminMngService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminMngController {
    @Resource
    private AdminMngService adminMngService;

    /**
     * 获取员工列表（分页）
     * 对应前端 API: GET /employee/list?page=1&pageSize=10
     */
    @Cacheable(cacheNames = "AdminMng", keyGenerator = "adminMngKeyGenerator")
    @GetMapping("/list")
    public GraceJSONResult list() {
        List<AdminInfo> adminInfos = adminMngService.queryAllAdminInfo();
        return GraceJSONResult.ok(adminInfos);
    }

    /**
     * 删除员工
     * @param username
     * @return
     */
    @CacheEvict(cacheNames = "AdminMng", allEntries = true)
    @PostMapping("/delete")
    public GraceJSONResult delete(@RequestParam String username) {
        adminMngService.delete(username);
        return GraceJSONResult.ok();
    }

    /**
     * 更新员工启用状态
     * @param username 用户名
     * @param isEnabled 启用状态（true/false）
     */
    @CacheEvict(cacheNames = "AdminMng", allEntries = true)
    @PostMapping("/updateStatus")
    public GraceJSONResult updateStatus(
            @RequestParam String username,
            @RequestParam Boolean isEnabled) {

        log.info("修改用户状态: username={}, isEnabled={}", username, isEnabled);
        adminMngService.updateStatus(username, isEnabled);
        return GraceJSONResult.ok();
    }

}
