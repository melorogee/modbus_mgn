package com.xwtec.modules.system.controller;


import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.xwtec.common.api.vo.Result;
import com.xwtec.common.aspect.annotation.AutoCatch;
import com.xwtec.common.aspect.annotation.AutoLog;
import com.xwtec.common.util.PasswordUtil;
import com.xwtec.common.util.SysCacheUtil;
import com.xwtec.common.util.oConvertUtils;
import com.xwtec.modules.shiro.authc.util.JwtUtil;
import com.xwtec.modules.system.entity.SysUser;
import com.xwtec.modules.system.entity.SysUserRole;
import com.xwtec.modules.system.service.ISysUserRoleService;
import com.xwtec.modules.system.service.ISysUserService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author scott
 * @since 2018-12-20
 */
@Slf4j
@RestController
@RequestMapping("/sys/user")
public class SysUserController {
	
	@Autowired
	private ISysUserService sysUserService;
	
	@Autowired
	private ISysUserRoleService sysUserRoleService;

	@Autowired
	private SysCacheUtil sysCacheUtil;

	/**
	 * 查询用户列表 联合查询:  <br/>
	 * @auther: zhuchen
	 * @param: 
	 * @date: 2019/3/18 20:47
	 * @return: 
	 */
	@GetMapping(value = "/list")
	@AutoLog("分页列表查询")
	@ApiOperation(value = "查询用户列表", notes = "查询用户列表", produces = "application/json")
	public Result<IPage<SysUser>> queryPageList(SysUser user, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															@RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
		Result<IPage<SysUser>> result = new Result<>();
		Page<SysUser> page = new Page<>(pageNo, pageSize);
		// 排序逻辑 处理
		//排序逻辑 处理
		String column = req.getParameter("column");
		String order = req.getParameter("order");
        String startTime = req.getParameter("createTime_begin");
        String endTime = req.getParameter("createTime_end");
		log.info("       >>>>>>>>>>>>>>>> 入参column："+column);
		log.info("       >>>>>>>>>>>>>>>> 入参order："+order);
        log.info("       >>>>>>>>>>>>>>>> 入参startTime："+startTime);
        log.info("       >>>>>>>>>>>>>>>> 入参endTime："+endTime);
		log.info("       >>>>>>>>>>>>>>>> 入参user："+JSONObject.toJSON(user));
        user.setRemark(startTime);// 开始时间
        user.setRemarkC(endTime);// 结束时间
		// 按照某个字段倒序、或正序
		if(oConvertUtils.isNotEmpty(column) && oConvertUtils.isNotEmpty(order)) {
			if("asc".equals(order)) {
				user.setRemarkA("asc");
			}else {
				user.setRemarkA("desc");
			}
		}
		IPage<SysUser> pageList = sysUserService.getPage(page, user);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	/**
	 * 新增用户信息:  <br/>
	 * @auther: zhuchen
	 * @param: 
	 * @date: 2019/3/14 10:39
	 * @return: 
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	/*@RequiresRoles({"admin"})*/
	@ApiOperation(value = "新增用户信息", notes = "新增用户信息", produces = "application/json")
	public Result<SysUser> add(@RequestBody JSONObject jsonObject) {
		Result<SysUser> result = new Result<SysUser>();
		String selectedRoles = jsonObject.getString("selectedroles");
		try {
			log.info("  start ........   SysUserController.add");
			log.info("  开始添加用户成功！");
			log.info("  入参jsonObject："+jsonObject.toJSONString());
			SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
			user.setCreateTime(new Date());//设置创建时间
			String salt = oConvertUtils.randomGen(8);
			user.setSalt(salt);
			String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
			user.setPassword(passwordEncode);
			user.setStatus(1);
			user.setDelFlag("0");
			sysUserService.addUserWithRole(user, selectedRoles);
			result.success("添加成功！");
			log.info("  添加用户成功！");
			log.info("  end ........   SysUserController.add");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("添加用户失败！",e.getMessage());
			result.error500("操作失败");
		}
		return result;
	}
	/**
	 * 编辑用户信息:  <br/>
	 * @auther: zhuchen
	 * @param: 
	 * @date: 2019/3/14 10:39
	 * @return: 
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	/*@RequiresRoles({"admin"})*/
	@ApiOperation(value = "编辑用户信息", notes = "编辑用户信息", produces = "application/json")
	public Result<SysUser> edit(@RequestBody JSONObject jsonObject,HttpServletRequest request) {
		Result<SysUser> result = new Result<SysUser>();
		try {
			SysUser sysUser = sysUserService.getById(jsonObject.getString("id"));
			if(sysUser==null) {
				result.error500("未找到对应实体");
			}else {
				SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
				user.setUpdateTime(new Date());
				//String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), sysUser.getSalt());
				user.setPassword(sysUser.getPassword());
				//String roles = jsonObject.getString("selectedroles");
				if(user.getRoleId().equals("000002")){
					user.setRemarkA(jsonObject.getString("merchantNo"));
					user.setRemarkB(jsonObject.getString("merchantAddress"));
				}
				// 获取登录用户userId
				String loginUserId = sysCacheUtil.getLoginUserId(request);
				log.info(" >>>>>>>>>>>>>>>>> 缓存中获取loginUserId："+loginUserId);
				user.setUpdateBy(loginUserId);
				sysUserService.editUserWithRole(user, "userEdit");// type  userEdit -- 来自用户管理里的编辑
				result.success("修改成功!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			result.error500("操作失败");
		}
		return result;
	}
	/**
	 * 根据用户id删除用户:  <br/>
	 * @auther: zhuchen
	 * @param: 
	 * @date: 2019/3/16 20:42
	 * @return: 
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	/*@RequiresRoles({"admin"})*/
	@ApiOperation(value = "根据用户id删除用户", notes = "根据用户id删除用户", produces = "application/json")
	public Result<SysUser> delete(@RequestParam(name="id",required=true) String id) {
		log.info("  start ........   SysUserController.add");
		log.info("  开始删除用户");
		Result<SysUser> result = new Result<SysUser>();
		log.info("   入参id："+id);
		SysUser sysUser = sysUserService.getById(id);
		if(sysUser==null) {
			result.error500("未找到对应实体");
			log.info("  未找到对应实体");
		}else {
			// 删除用户
			SysUser delSysUser = new SysUser();
			delSysUser.setId(id);
			delSysUser.setDelFlag("1");
			delSysUser.setRoleId(sysUser.getRoleId());
			sysUserService.editUserWithRole(delSysUser, "userEdit");// type  userEdit -- 来自用户管理里的编辑
			result.success("删除成功!");
		}
		
		return result;
	}
	/**
	 * 批量删除用户:  <br/>
	 * @auther: zhuchen
	 * @param: 
	 * @date: 2019/3/14 10:40
	 * @return: 
	 */
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
	/*@RequiresRoles({"admin"})*/
	public Result<SysUser> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysUser> result = new Result<SysUser>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.sysUserService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	/**
	 * 批量冻结用户:  <br/>
	 * @auther: zhuchen
	 * @param: 
	 * @date: 2019/3/14 10:40
	 * @return: 
	 */
	@RequestMapping(value = "/frozenBatch", method = {RequestMethod.PUT})
	public Result<SysUser> frozenBatch(@RequestParam(name="ids",required=true) String ids,@RequestParam(name="status",required=true) String status,@RequestParam(name="roleCode",required=true) String roleCode) {
		log.info("  start ........   SysUserController.frozenBatch");
		Result<SysUser> result = new Result<SysUser>();
		try {
			log.info("  ======================= ids："+ids);
			log.info("  ======================= status："+status);
			log.info("  ======================= roleCode："+roleCode);
			String[] arr = ids.split(",");
			for (String id : arr) {
				if(oConvertUtils.isNotEmpty(id)) {
					SysUser user = new SysUser();
					user.setId(id);
					user.setStatus(Integer.parseInt(status));
					user.setRoleId(roleCode);
					/*this.sysUserService.update(new SysUser().setStatus(Integer.parseInt(status)),
							new UpdateWrapper<SysUser>().lambda().eq(SysUser::getId,id));*/
					sysUserService.editUserWithRole(user, "userEdit");// type  userEdit -- 来自用户管理里的编辑
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.error500("操作失败"+e.getMessage());
		}
		result.success("操作成功!");
		log.info("  end ........   SysUserController.frozenBatch");
		return result;
	}

	/**
	 * 冻结、解冻用户:  <br/>
	 * @auther: zhuchen
	 * @param:
	 * @date: 2019/3/14 10:40
	 * @return:
	 */
	@RequestMapping(value = "/handleFrozen",method = RequestMethod.DELETE)
	public Result<SysUser> handleFrozen(@RequestParam(name="id",required = true) String id,@RequestParam(name="status",required = true) String status,@RequestParam(name="roleCode",required = true) String roleCode) {
		log.info("  start ........   SysUserController.handleFrozen");
		Result<SysUser> result = new Result<SysUser>();
		try {
			log.info("  ======================= id："+id);
			log.info("  ======================= status："+status);
			log.info("  ======================= roleCode："+roleCode);
			if(oConvertUtils.isNotEmpty(id)) {
				SysUser user = new SysUser();
				user.setId(id);
				user.setStatus(Integer.parseInt(status));
				user.setRoleId(roleCode);
				sysUserService.editUserWithRole(user, "userEdit");// type  userEdit -- 来自用户管理里的编辑
				result.success("操作成功!");
			}else{
				result.error500("操作失败，用户信息为空！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.error500("操作失败"+e.getMessage());
		}
		log.info("  end ........   SysUserController.handleFrozen");
		return result;
	}
	
	
	/**
	 * 根据userid获取用户信息:  <br/>
	 * @auther: zhuchen
	 * @param: 
	 * @date: 2019/3/14 10:40
	 * @return: 
	 */
	@RequestMapping(value = "/queryById", method = RequestMethod.GET)
	@ApiOperation(value = "根据userid获取用户信息", notes = "根据userid获取用户信息", produces = "application/json")
	public Result<SysUser> queryById(@RequestParam(name="id",required=true) String id) {
		Result<SysUser> result = new Result<SysUser>();
		SysUser sysUser = sysUserService.getById(id);
		if(sysUser==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(sysUser);
			result.setSuccess(true);
		}
		return result;
	}
	/**
	 * 根据userid查找用户角色信息:  <br/>
	 * @auther: zhuchen
	 * @param: 
	 * @date: 2019/3/14 10:41
	 * @return: 
	 */
	@RequestMapping(value = "/queryUserRole", method = RequestMethod.GET)
	@ApiOperation(value = "根据userid查找用户角色信息", notes = "根据userid查找用户角色信息", produces = "application/json")
	public Result<List<String>> queryUserRole(@RequestParam(name="userid",required=true) String userid) {
		Result<List<String>> result = new Result<>();
		List<String> list = new ArrayList<String>();
		List<SysUserRole> userRole = sysUserRoleService.list(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId,userid));
		if(userRole==null || userRole.size()<=0) {
			result.error500("未找到用户相关角色信息");
		}else {
			for (SysUserRole sysUserRole : userRole) {
				list.add(sysUserRole.getRoleId());
			}
			result.setSuccess(true);
			result.setResult(list);
		}
		return result;
	}
	
	
	/**
	  * 校验用户账号是否唯一<br>
	  * 可以校验其他 需要检验什么就传什么。。。
	 * @param sysUser
	 * @return
	 */
	@RequestMapping(value = "/checkOnlyUser", method = RequestMethod.GET)
	@ApiOperation(value = "校验用户账号是否唯一", notes = "校验用户账号是否唯一", produces = "application/json")
	public Result<Boolean> checkUsername(SysUser sysUser) {
		Result<Boolean> result = new Result<>();
		result.setResult(true);//如果此参数为false则程序发生异常
		String id = sysUser.getId();
		log.info("--验证用户信息是否唯一---id:"+id);
		try {
			SysUser oldUser = null;
			if(oConvertUtils.isNotEmpty(id)) {
				oldUser = sysUserService.getById(id);
			}else {
				sysUser.setId(null);
			}
			//通过传入信息查询新的用户信息
			sysUser.setDelFlag("0");// 未删除的用户
			SysUser newUser = sysUserService.getOne(new QueryWrapper<SysUser>(sysUser));
			if(newUser!=null) {
				//如果根据传入信息查询到用户了，那么就需要做校验了。
				if(oldUser==null) {
					//oldUser为空=>新增模式=>只要用户信息存在则返回false
					result.setSuccess(false);
					result.setMessage("用户账号已存在");
					return result;
				}else if(!id.equals(newUser.getId())) {
					//否则=>编辑模式=>判断两者ID是否一致-
					result.setSuccess(false);
					result.setMessage("用户账号已存在");
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
	
	/**
	  * 修改密码
	 */
	@RequestMapping(value = "/changPassword", method = RequestMethod.PUT)
	/*@RequiresRoles({"admin"})*/
	public Result<SysUser> changPassword(@RequestBody SysUser sysUser) {
		Result<SysUser> result = new Result<SysUser>();
		String password = sysUser.getPassword();
		sysUser = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, sysUser.getUsername()));
		if(sysUser==null) {
			result.error500("未找到对应实体");
		}else {
			String salt = oConvertUtils.randomGen(8);
			sysUser.setSalt(salt);
			String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(),password, salt);
			sysUser.setPassword(passwordEncode);
			this.sysUserService.updateById(sysUser);
			result.setResult(sysUser);
			result.success("密码修改完成！");
		}
		return result;
	}

	/**
	 * 保存人和知识点关系
	 * @param params
	 * @user jiangjinbao
	 * @date 2019/7/8
	 * @return com.xwtec.common.api.vo.Result
	 */
	@PostMapping("/saveUserKnow")
	@AutoCatch
	public Result saveUserKnow(@RequestBody Map<String, Object> params) throws Exception{
		sysUserService.saveUserKnow(params);
		return Result.getResultForSuccess();
	}
}
