package com.xwtec.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xwtec.modules.system.entity.SysUser;
import com.xwtec.modules.system.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 * @author scott
 * @since 2018-12-21
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
	/**
	 * 根据用户账号获取角色信息:  <br/>
	 * @auther: zhuchen
	 * @param: 
	 * @date: 2019/3/14 9:43
	 * @return: 
	 */
	@Select("select role_code from sys_role where id in (select role_id from sys_user_role where user_id = (select id from sys_user where status=1 and del_flag='0' and username=#{username}))")
	List<String> getRoleByUserName(@Param("username") String username);
	
	/**
	 * 根据用户账号获取角色信息:  <br/>
	 * @auther: zhuchen
	 * @param: 
	 * @date: 2019/3/17 22:05
	 * @return: 
	 */
	@Select("SELECT  t1.role_code roleCode,t1.role_name roleName, t.role_id roleId ,t.user_id userId FROM sys_user_role t INNER JOIN sys_role t1 ON t.role_id = t1.id WHERE t.status=1 AND t.del_flag='0'  and user_id = (select id from sys_user where status=1 and del_flag='0' and username=#{userName}) ")
	Map<String,String> getRoleInfoByUserName(@Param("userName") String userName);

	/**
	 * 根据roleCode获取角色id:  <br/>
	 * @auther: zhuchen
	 * @param: roleCode 角色code
	 * @date: 2019/3/14 9:43
	 * @return:
	 */
	@Select("select id  from sys_role where status = '1' and del_flag='0' and role_code = #{roleCode}")
	String getRoleByRoleCode(@Param("roleCode") String roleCode);

	@Select("select role_code  from sys_role where status = '1' and del_flag='0' and id = #{roleId}")
	String getRoleCodeByRoleId(@Param("roleId") String role_code);
	/**
	 * 根据用户id删除对应用户角色信息  <br/>
	 * @auther: zhuchen
	 * @param: userRole 用户角色
	 * @date: 2019/3/14 9:44
	 * @return: 
	 */
	void updateRoleByUserId(SysUserRole userRole);
	
	/**
	 * 根据角色id和删除状态获取用户角色关系数量:  <br/>
	 * @auther: zhuchen
	 * @param: 
	 * @date: 2019/3/16 11:35
	 * @return: 
	 */
	@Select("select count(0)  from sys_user_role where del_flag=#{delFlag} and role_id = #{roleId}")
	int getUserRoleByRoleId(SysUserRole userRole);


}
