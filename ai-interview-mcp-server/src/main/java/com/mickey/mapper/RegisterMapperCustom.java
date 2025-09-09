package com.mickey.mapper;

import com.mickey.pojo.Entity.AdminInfo;

public interface RegisterMapperCustom {
    AdminInfo selectAdminInfoByName(String username);
}
