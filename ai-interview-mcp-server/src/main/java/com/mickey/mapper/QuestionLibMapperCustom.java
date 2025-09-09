package com.mickey.mapper;

import com.mickey.pojo.VO.QuestionLibVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 面试题库表（每个数字人面试官都会对应一些面试题） Mapper 接口
 * </p>
 *
 * @author Mickey
 * @since 2025-06-11
 */
public interface QuestionLibMapperCustom{

    public List<QuestionLibVO> queryQuestionLibList(@Param("paramMap")Map<String, Object> map);

}
