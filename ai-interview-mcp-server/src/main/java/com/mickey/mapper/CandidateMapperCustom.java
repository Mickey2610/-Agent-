package com.mickey.mapper;

import com.mickey.pojo.Entity.AdminInfo;
import com.mickey.pojo.Entity.Candidate;

/**
 * <p>
 * 应聘者表 Mapper 接口
 * </p>
 *
 * @author Mickey
 * @since 2025-06-11
 */
public interface CandidateMapperCustom {
    Candidate selectCandidateByName(String realName);
}
