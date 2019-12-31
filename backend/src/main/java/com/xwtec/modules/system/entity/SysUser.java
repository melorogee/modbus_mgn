package com.xwtec.modules.system.entity;

import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author scott
 * @since 2018-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    private String id;
    
    /**
     * 登录账号
     */
    private String username;

    /**
     * 用户名称
     */
    private String realname;

    /**
     * 密码
     */
    private String password;

    /**
     * md5密码盐
     */
    private String salt;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 生日
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date birthday;

    /**
     * 性别（1：男 2：女）
     */
    private Integer sex;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 状态(1：正常  2：冻结 ）
     */
    private Integer status;

    /**
     * 删除状态（0，正常，1已删除）
     */
    private String delFlag;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 角色id   如果该字段存 roleCode 则 '000002':外呼主管   '000003':外呼人员
     */
    private String roleId;

    private String deptId;//sunwei add 用户的部门
    /**
     * 备注:  <br/>
     * @auther: zhuchen
     * @param: 
     * @date: 2019/3/15 13:01
     * @return: 
     */
    private String remark;
    
    /**
     * 备用字段A:  <br/>
     * @auther: zhuchen
     * @param: 
     * @date: 2019/3/16 16:03
     * @return: 
     */
    private String remarkA;
    /**
     * 备用字段B:  <br/>
     * @auther: zhuchen
     * @param:
     * @date: 2019/3/16 16:03
     * @return:
     */
    private String remarkB;
    /**
     * 备用字段C:  <br/>
     * @auther: zhuchen
     * @param:
     * @date: 2019/3/16 16:03
     * @return:
     */
    private String remarkC;

    @TableField(exist = false)
    private String roleCode;

    @TableField(exist = false)
    private List<String> knowList;

    @TableField(exist = false)
    private List<String> knowNameList;

    @TableField(exist = false)
    private String linkKnow;
}
