package com.xwtec.common.aspect;

import com.alibaba.fastjson.JSONObject;
import com.xwtec.common.aspect.annotation.AutoLog;
import com.xwtec.common.util.IPUtils;
import com.xwtec.common.util.SpringContextUtils;
import com.xwtec.modules.system.entity.SysLog;
import com.xwtec.modules.system.entity.SysUser;
import com.xwtec.modules.system.service.ISysLogService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;


/**
 * 系统日志，切面处理类
 * 
 * @author scott
 * @email jeecgos@163.com
 * @date 2018年1月14日
 */
@Aspect
@Component
public class AutoLogAspect {
	@Autowired
	private ISysLogService sysLogService;
	
	@Pointcut("@annotation(com.xwtec.common.aspect.annotation.AutoLog)")
	public void logPointCut() { 
		
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		//执行方法
		Object result = point.proceed();
		//执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;

		//保存日志
		saveSysLog(point, time);
		return result;
	}

	private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		SysLog sysLog = new SysLog();
		AutoLog syslog = method.getAnnotation(AutoLog.class);
		if(syslog != null){
			//注解上的描述,操作日志内容
			sysLog.setLogContent(syslog.value());
			sysLog.setLogType(syslog.logType());
		}

		//请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		sysLog.setMethod(className + "." + methodName + "()");

		//请求的参数
		Object[] args = joinPoint.getArgs();
		SysUser sysUser = null;
		try{
			String params ="";
			int length=args.length;
			for (int i=0;i<length;i++ ) {
				if(!(args[i] instanceof ShiroHttpServletRequest)) {
					params = params + JSONObject.toJSONString(args[i]);
				}
			}

			sysLog.setRequestParam(params);
			//获取request
			HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
			//设置IP地址
			sysLog.setIp(IPUtils.getIpAddr(request));
			//获取登录用户信息
			sysUser = (SysUser)SecurityUtils.getSubject().getPrincipal();
		}catch (Exception e){

		}

		if(sysUser!=null){
			sysLog.setUserid(sysUser.getUsername());
			sysLog.setUsername(sysUser.getRealname());
		}
		//耗时
		sysLog.setCostTime(time);
		sysLog.setCreateTime(new Date());
		//保存系统日志
		sysLogService.save(sysLog);
	}
}
