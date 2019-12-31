package com.xwtec.modules.system.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xwtec.common.api.vo.Result;
import com.xwtec.common.util.oConvertUtils;
import com.xwtec.modules.system.entity.SysRole;
import com.xwtec.modules.system.entity.SysUserRole;
import com.xwtec.modules.system.service.ISysRoleService;
import com.xwtec.modules.system.service.ISysUserRoleService;
import com.xwtec.modules.system.service.ISysUserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author scott
 * @since 2018-12-19
 */
@RestController
@RequestMapping("/sys/role")
@Slf4j
public class SysRoleController {
	@Autowired
	private ISysRoleService sysRoleService;


	@Autowired
	private ISysUserRoleService sysUserRoleService;

	@Autowired
	private ISysUserService sysUserService;
	/**
	  * 分页列表查询
	 * @param role
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation(value = "角色管理", notes = "获取角色列表", produces = "application/json")
	public Result<IPage<SysRole>> queryPageList(SysRole role,
                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) {
		Result<IPage<SysRole>> result = new Result<IPage<SysRole>>();
		QueryWrapper<SysRole> queryWrapper = new QueryWrapper<SysRole>();
		Page<SysRole> page = new Page<SysRole>(pageNo,pageSize);
		//排序逻辑 处理
		String column = req.getParameter("column");
		String order = req.getParameter("order");
		if(oConvertUtils.isNotEmpty(column) && oConvertUtils.isNotEmpty(order)) {
			if("asc".equals(order)) {
				queryWrapper.orderByAsc(oConvertUtils.camelToUnderline(column));
			}else {
				queryWrapper.orderByDesc(oConvertUtils.camelToUnderline(column));
			}
		}
		log.info("       >>>>>>>>>>>>>>>> 入参column："+column);
		log.info("       >>>>>>>>>>>>>>>> 入参order："+order);
		log.info("       >>>>>>>>>>>>>>>> role："+ JSONObject.toJSON(role));
		// 角色名称
		if(StringUtils.isNotBlank(role.getRoleName())) {
			queryWrapper.and(wrapper -> wrapper.like("role_name", role.getRoleName()));
		}
		// 角色code
		if(StringUtils.isNotBlank(role.getStatus())) {
			queryWrapper.and(wrapper -> wrapper.eq("status", role.getStatus()));
		}
		queryWrapper.and(wrapper -> wrapper.eq("del_flag","0"));
		queryWrapper.and(wrapper -> wrapper.ne("role_code","admin"));
		//TODO 过滤逻辑处理
		//TODO begin、end逻辑处理
		//TODO 一个强大的功能，前端传一个字段字符串，后台只返回这些字符串对应的字段
		//创建时间/创建人的赋值
		IPage<SysRole> pageList = sysRoleService.page(page, queryWrapper);
		log.info("查询当前页："+pageList.getCurrent());
		log.info("查询当前页数量："+pageList.getSize());
		log.info("查询结果数量："+pageList.getRecords().size());
		log.info("数据总数："+pageList.getTotal());
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param role
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "角色管理", notes = "添加角色", produces = "application/json")
	public Result<SysRole> add(@RequestBody SysRole role) {
		Result<SysRole> result = new Result<SysRole>();
		try {
			role.setCreateTime(new Date());
			sysRoleService.save(role);
			result.success("添加成功！");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param role
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	/*@RequiresRoles({"admin"})*/
    @ApiOperation(value = "角色管理", notes = "编辑角色", produces = "application/json")
	public Result<SysRole> eidt(@RequestBody SysRole role) {
		Result<SysRole> result = new Result<SysRole>();
		SysRole sysrole = sysRoleService.getById(role.getId());
		if(sysrole==null) {
			result.error500("未找到对应实体");
		}else {
			role.setUpdateTime(new Date());
			boolean ok = sysRoleService.updateById(role);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	/*@RequiresRoles({"admin"})*/
    @ApiOperation(value = "角色管理", notes = "根据角色id删除角色", produces = "application/json")
	public Result<SysRole> delete(@RequestParam(name="id",required=true) String id) {
		log.info("   start  ........  SysRoleController.delete");
		Result<SysRole> result = new Result<SysRole>();
		SysRole sysrole = sysRoleService.getById(id);
		if(sysrole==null) {
			result.error500("未找到对应实体");
		}else {
			// 查询该角色是否被使用，被使用则不能删除
			SysUserRole userRole = new SysUserRole();
			userRole.setRoleId(id);
			userRole.setDelFlag("0");
			int count = sysUserRoleService.getUserRoleByRoleId(userRole);
			log.info("       入参id："+id);
			log.info("       该角色被使用数量count ："+count);
			if(count>0){
				result.error500("该角色已被使用，不能删除!id"+id);
				result.error500("该角色已被使用，不能删除!");
			}else{
				SysRole role = new SysRole();
				role.setId(id);
				role.setDelFlag("1");// 删除
				role.setStatus("2");// 无效
				boolean ok = sysRoleService.updateById(role);
				if(ok) {
					log.info("       删除成功!id"+id);
					result.success("删除成功!");
				}
			}

		}
		log.info("   end  ........  SysRoleController.delete");
		return result;
	}
    /**
     * 根据状态更新角色信息:  <br/>
     * @auther: zhuchen
     * @param: 
     * @date: 2019/3/26 9:53
     * @return: 
     */
	@RequestMapping(value = "/handleEditStatus", method = RequestMethod.DELETE)
	/*@RequiresRoles({"admin"})*/
    @ApiOperation(value = "角色管理", notes = "根据状态更新角色信息", produces = "application/json")
	public Result<SysRole> handleEditStatus(@RequestParam(name="id",required=true) String id,@RequestParam(name="status",required=true) String status) {
		log.info("   start  ........  SysRoleController.handleEditStatus");
		Result<SysRole> result = new Result<SysRole>();
		SysRole sysrole = sysRoleService.getById(id);
		if(sysrole==null) {
			result.error500("未找到对应实体");
		}else {
			if(StringUtils.isNotBlank(status)&&status.equals("2")){
				// 查询该角色是否被使用，被使用则不能停用
				SysUserRole userRole = new SysUserRole();
				userRole.setRoleId(id);
				userRole.setDelFlag("0");
				userRole.setStatus(1);
				int count = sysUserRoleService.getUserRoleByRoleId(userRole);
				log.info("       入参id："+id);
				log.info("       该角色被使用数量count ："+count);
				if(count>0){
					log.info("该角色已被使用，不能停用!id"+id);
					result.error500("该角色已被使用，不能停用!");
				}else{
					SysRole role = new SysRole();
					role.setId(id);
					//role.setDelFlag("1");// 删除
					role.setStatus("2");// 无效
					boolean ok = sysRoleService.updateById(role);
					if(ok) {
						log.info("       停用成功!id"+id);
						result.success("停用成功!");
					}
				}
			}else if(StringUtils.isNotBlank(status)&&status.equals("1")){
				SysRole role = new SysRole();
				role.setId(id);
				//role.setDelFlag("1");// 删除
				role.setStatus("1");// 启用
				boolean ok = sysRoleService.updateById(role);
				if(ok) {
					log.info("       启用成功!id"+id);
					result.success("启用成功!");
				}
			}

		}
		log.info("   end  ........  SysRoleController.handleEditStatus");
		return result;
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
	/*@RequiresRoles({"admin"})*/
    @ApiOperation(value = "角色管理", notes = "批量删除角色信息", produces = "application/json")
	public Result<SysRole> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysRole> result = new Result<SysRole>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.sysRoleService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/queryById", method = RequestMethod.GET)
    @ApiOperation(value = "角色管理", notes = "通过id查询", produces = "application/json")
	public Result<SysRole> queryById(@RequestParam(name="id",required=true) String id) {
		Result<SysRole> result = new Result<SysRole>();
		SysRole sysrole = sysRoleService.getById(id);
		if(sysrole==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(sysrole);
			result.setSuccess(true);
		}
		return result;
	}
	/**
	 * 查询所有有效的角色:  <br/>
	 * @auther: zhuchen
	 * @param: 
	 * @date: 2019/3/26 9:54
	 * @return: 
	 */
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    @ApiOperation(value = "角色管理", notes = "查询所有有效的角色", produces = "application/json")
	public Result<List<SysRole>> queryall() {
		Result<List<SysRole>> result = new Result<>();
		List<SysRole> list = sysRoleService.list(new QueryWrapper<SysRole>().lambda().eq(SysRole::getDelFlag, "0").eq(SysRole::getStatus, "1").ne(SysRole::getRoleCode, "admin"));
		if(list==null||list.size()<=0) {
			result.error500("未找到角色信息");
		}else {
			result.setResult(list);
			result.setSuccess(true);
		}
		return result;
	}
	/**
	 * 根据userName获取角色信息:  <br/>
	 * @auther: zhuchen
	 * @param: 
	 * @date: 2019/3/17 22:03
	 * @return: 
	 */
	@RequestMapping(value = "/getRoleForUserId", method = RequestMethod.GET)
    @ApiOperation(value = "角色管理", notes = "根据userName获取角色信息", produces = "application/json")
	public Result<String> getRoleForUserId(String userName) {
		log.info("     start   .......    SysRoleController.getRoleForUserId");
		Result<String> result = new Result<>();
		log.info("   >>>>>>>>>>>>>> userName："+userName);
		Map<String,String> map = sysUserService.getRoleInfoByUserName(userName);
		log.info("   >>>>>>>>>>>>>>>出参map："+map);
		if(map.isEmpty()) {
			result.error500("未找到角色信息");
		}else {
			result.setResult(map.get("roleName"));
			result.setSuccess(true);
		}
		log.info("     end   .......    SysRoleController.getRoleForUserId");
		return result;
	}
	
	/**
	  * 校验角色编码唯一
	 */
	@RequestMapping(value = "/checkRoleCode", method = RequestMethod.GET)
    @ApiOperation(value = "角色管理", notes = "校验角色编码唯一", produces = "application/json")
	public Result<Boolean> checkUsername(String id,String roleCode) {
		Result<Boolean> result = new Result<>();
		result.setResult(true);//如果此参数为false则程序发生异常
		log.info("--验证角色编码是否唯一---id:"+id+"--roleCode:"+roleCode);
		try {
			SysRole role = null;
			if(oConvertUtils.isNotEmpty(id)) {
				role = sysRoleService.getById(id);
			}
			// 查询输入的roleCode是否被使用（有效的）
			SysRole newRole = sysRoleService.getOne(new QueryWrapper<SysRole>().lambda().eq(SysRole::getRoleCode, roleCode).eq(SysRole::getDelFlag, "0"));
			if(newRole!=null) {
				//如果根据传入的roleCode查询到信息了，那么就需要做校验了。
				if(role==null) {
					//role为空=>新增模式=>只要roleCode存在则返回false
					result.setSuccess(false);
					result.setMessage("角色编码已存在");
					return result;
				}else if(!id.equals(newRole.getId())) {
					//否则=>编辑模式=>判断两者ID是否一致-
					result.setSuccess(false);
					result.setMessage("角色编码已存在");
					return result;
				}
			}
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(false);
			result.setMessage(e.getMessage());
			return result;
		}
		result.setSuccess(true);
		return result;
	}
	
}
