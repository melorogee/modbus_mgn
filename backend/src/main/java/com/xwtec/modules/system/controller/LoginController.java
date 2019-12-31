package com.xwtec.modules.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.xwtec.common.api.vo.Result;
import com.xwtec.common.constant.CommonConstant;
import com.xwtec.common.system.api.ISysBaseAPI;
import com.xwtec.common.util.PasswordUtil;
import com.xwtec.common.util.RedisUtil;
import com.xwtec.modules.shiro.authc.util.JwtUtil;
import com.xwtec.modules.system.entity.SysUser;
import com.xwtec.modules.system.model.SysLoginModel;
import com.xwtec.modules.system.service.ISysLogService;
import com.xwtec.modules.system.service.ISysPermissionService;
import com.xwtec.modules.system.service.ISysUserRoleService;
import com.xwtec.modules.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author scott
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/sys")
@Api("用户登录")
public class LoginController {
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private ISysUserRoleService sysUserRoleService;
	@Autowired
	private ISysBaseAPI sysBaseAPI;
	@Autowired
	private ISysLogService logService;
	@Autowired
    private RedisUtil redisUtil;
	@Autowired
	private ISysPermissionService sysPermissionService;


	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ApiOperation("登录接口")
	public Result<JSONObject> login(@RequestBody SysLoginModel sysLoginModel, HttpServletRequest request, HttpServletResponse response) {
		Result<JSONObject> result = new Result<JSONObject>();
		String username = sysLoginModel.getUsername();
		String password = sysLoginModel.getPassword();
		SysUser sysUser = sysUserService.getUserByName(username);
		if(sysUser==null) {
			result.error500("该用户不存在");
			sysBaseAPI.addLog("登录失败，用户名:"+username+"不存在！", CommonConstant.LOG_TYPE_1, null);
			return result;
		}else {
			int status = sysUser.getStatus();
			if(status==2){
				result.error500("该用户账号被冻结，暂时无法登录");
				sysBaseAPI.addLog("登录失败，用户名:"+username+"账号被冻结！", CommonConstant.LOG_TYPE_1, null);
				return result;
			}
			//密码验证
			String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
			String syspassword = sysUser.getPassword();
			if(!syspassword.equals(userpassword)) {
				result.error500("用户名或密码错误");
				return result;
			}
			Map<String,String> userRoleInfo = sysUserService.getRoleInfoByUserName(username);
			//生成token
			String token = JwtUtil.sign(username, syspassword);
			//当前登陆人Token存入redis
			redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
			//当前登陆人userId存入redis
			redisUtil.set(token + "LoginUserId", sysUser.getId());
			//当前登陆人角色信息存入redis
			redisUtil.set(token + "LoginRoleInfo", userRoleInfo);
			 //设置Token超时时间
			redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME/1000);
			//设置userId超时时间
			redisUtil.expire(token + "LoginUserId", JwtUtil.EXPIRE_TIME/1000);
			//设置登陆人角色信息超时时间
			redisUtil.expire(token + "LoginRoleInfo", JwtUtil.EXPIRE_TIME/1000);

			List<String> btnList = sysPermissionService.queryByUserBtn(username);

			JSONObject obj = new JSONObject();
			obj.put("token", token);
			obj.put("userInfo", sysUser);
			obj.put("userRoleInfo",userRoleInfo);
			obj.put("login_UserRoleBtn",btnList);
			result.setResult(obj);
			result.success("登录成功");
			sysBaseAPI.addLog("用户名: "+username+",登录成功！", CommonConstant.LOG_TYPE_1, null);
		}
		return result;
	}
	
	/**
	 * 获取访问量
	 * @return
	 */
	@GetMapping("loginfo")
	@ApiOperation("获取访问量接口")
	public Result<JSONObject> loginfo() {
		Result<JSONObject> result = new Result<JSONObject>();
		JSONObject obj = new JSONObject();
		// 获取系统访问记录
		Long totalVisitCount = logService.findTotalVisitCount();
		obj.put("totalVisitCount", totalVisitCount);
		Long todayVisitCount = logService.findTodayVisitCount();
		obj.put("todayVisitCount", todayVisitCount);
		Long todayIp = logService.findTodayIp();
		obj.put("todayIp", todayIp);
		result.setResult(obj);
		result.success("登录成功");
		return result;
	}

}
