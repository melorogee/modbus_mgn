package com.xwtec.modules.system.mapper;

import com.xwtec.modules.system.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author scott
 * @since 2018-12-20
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
	/**
	 * 根据登录名获取用户信息:  <br/>
	 * @auther: zhuchen
	 * @param: 
	 * @date: 2019/3/14 16:09
	 * @return: 
	 */
	public SysUser getUserByName(@Param("username") String username);

	/**
	 * 更新用户信息:  <br/>
	 * @auther: zhuchen
	 * @param: 
	 * @date: 2019/3/14 9:18
	 * @return: 
	 */
	public void updateUserInfo(SysUser user);

	/**
	 * 获取列表
	 *
	 * @param
	 * @return
	 */
	List<SysUser> getPageList(SysUser sysUser);

	/**
	 * 根据user查询知识类别列表
	 * @param userId
	 * @return
	 */
	List<String> selectKnowledgeIdByUserId(String userId);

	/**
	 * 根据userId
	 * @param userId
	 * @return
	 */
	List<String> selectKnowListByUser(String userId);

	/**
	 * 根据userId
	 * @param userId
	 * @return
	 */
	List<String> selectKnowNameListByUser(String userId);

	/**
	 * 保存用户知识点关系
	 * @param params
	 */
	void insertUserKnow(Map params);

	/**
	 * 根据用户id删除关联知识点
	 * @param userId
	 */
	void deleteUserKnow(String userId);
}
