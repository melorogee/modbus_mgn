package com.xwtec.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xwtec.common.api.vo.Result;
import com.xwtec.modules.system.entity.SysUser;
import com.xwtec.modules.system.entity.SysUserRole;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author scott
 * @since 2018-12-20
 */
public interface ISysUserService extends IService<SysUser> {
	/**
	 * 根据用户名获取有效的用户信息:  <br/>
	 * @auther: zhuchen
	 * @param: 
	 * @date: 2019/3/14 16:15
	 * @return: 
	 */
	public SysUser getUserByName(String username);

	/**
	 * 添加用户和用户角色关系
	 * @param user
	 * @param roles
	 */
	public void addUserWithRole(SysUser user,String roles);

	/**
	 * 新增外呼人员、外呼主管调用创建用户信息接口:  <br/>
	 * @auther: zhuchen
	 * @param: 
	 * @date: 2019/3/14 10:45
	 */
	public void addUserInfoForBase(SysUser user) throws Exception;
	
	/**
	 * 修改用户和用户角色关系
	 * @param user
	 * @param type
	 */
	public void editUserWithRole(SysUser user,String type);

	/**
	 * 获取用户的授权角色
	 * @param username
	 * @return List<String>
	 */
	public List<String> getRole(String username);
	/**
	 * 根据userId获取角色信息:  <br/>
	 * @auther: zhuchen
	 * @param: userName
	 * @date: 2019/3/14 14:22
	 * @return:  Map<String,String>
	 */
	public Map<String,String> getRoleInfoByUserName(String userName);
	
	/**
	 * 获取列表:  <br/>
	 * @auther: zhuchen
	 * @param: 
	 * @date: 2019/3/18 20:51
	 * @return: 
	 */
	 IPage<SysUser> getPage(Page<SysUser> page,SysUser user);

	/**
	 * 保存用户知识点关系
	 * @param params
	 */
	void saveUserKnow(Map<String, Object> params) throws Exception;
}
