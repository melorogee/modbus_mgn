package com.xwtec.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xwtec.modules.system.entity.SysPermission;
import com.xwtec.modules.system.model.TreeModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author scott
 * @since 2018-12-21
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
	
	public List<TreeModel> queryListByParentId(@Param("parentId") String parentId);
	
	/**
	  *   根据用户查询用户权限
	 */
	public List<SysPermission> queryByUser(@Param("username") String username);

	/**
	 *  查询用户按钮权限
	 * @param username
	 * @return
	 */
	public List<String> queryByUserBtn(@Param("username") String username);

}
