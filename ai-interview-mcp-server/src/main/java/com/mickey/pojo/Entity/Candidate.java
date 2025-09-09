package com.mickey.pojo.Entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 应聘者表
 * </p>
 *
 * @author Mickey
 * @since 2025-06-11
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Candidate implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String randomPassword;
    /**
     * 真实姓名（需国密）
     */
    private String realName;

    /**
     * 应聘者身份证号码
     */
    private String identityNum;

    /**
     * 应聘者手机号
     */
    private String mobile;

    /**
     * 性别，1:男 0:女 2:保密
     */
    private Integer sex;

    /**
     * 应聘者照片
     */
    private String face;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 生日
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime birthday;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 地址
     */
    private String address;

    /**
     * 应聘职位的主键id
     */
    private String jobId;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedTime;


}
