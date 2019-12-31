package com.xwtec.common.aspect.annotation;

import com.xwtec.common.constant.CommonConstant;

import java.lang.annotation.*;

/**
 * 系统日志注解
 * 
 * @author scott
 * @email jeecgos@163.com
 * @date 2019年1月14日
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoLog {

	/**
	 * 日志内容
	 * 
	 * @return
	 */
	String value() default "";

	/**
	 * 日志类型
	 * 
	 * @return 1:登录日志; 2:操作日志; 3:定时任务;
	 * 默认是：操作日志
	 */
	int logType() default CommonConstant.LOG_TYPE_2;
}
