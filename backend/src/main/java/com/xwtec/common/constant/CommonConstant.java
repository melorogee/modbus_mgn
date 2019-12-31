package com.xwtec.common.constant;

public interface CommonConstant {

	/**
	 * 正常状态
	 */
	Integer STATUS_NORMAL = 0;

	/**
	 * 禁用状态
	 */
	Integer STATUS_DISABLE = -1;

	/**
	 * 删除标志
	 */
	Integer DEL_FLAG_1 = 1;

	/**
	 * 未删除
	 */
	Integer DEL_FLAG_0 = 0;

	/**
	 * 系统日志类型： 登录
	 */
	int LOG_TYPE_1 = 1;

	/**
	 * 系统日志类型： 操作
	 */
	int LOG_TYPE_2 = 2;

	/**
	 * 系统日志类型： 定时任务
	 */
	int LOG_TYPE_3 = 3;
	
	
	/** {@code 500 Server Error} (HTTP/1.0 - RFC 1945) */
    public static final Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
    /** {@code 200 OK} (HTTP/1.0 - RFC 1945) */
    public static final Integer SC_OK_200 = 200;

    
    public static String PREFIX_USER_ROLE = "PREFIX_USER_ROLE";
    public static String PREFIX_USER_PERMISSION  = "PREFIX_USER_PERMISSION ";
    public static int  TOKEN_EXPIRE_TIME  = 3600;
    
    public static String PREFIX_USER_TOKEN  = "PREFIX_USER_TOKEN ";
    
    /**
     *  0：一级菜单
     */
    public static Integer MENU_TYPE_0  = 0;
   /**
    *  1：子菜单 
    */
    public static Integer MENU_TYPE_1  = 1;
    /**
     *  2：按钮权限
     */
    public static Integer MENU_TYPE_2  = 2;
	/**
	 *  1：启用
	 */
    public static Integer STATUS_ENABLED = 1;
	/**
	 *  2：停用
	 */
    public static Integer STATUS_DISABLED = 2;
	/**
	 *  0：学生
	 */
    public static String USER_TYPE_STUDENT = "0";
	/**
	 *  1：临时学生
	 */
    public static String USER_TYPE_TEMPORARY_STUDENT = "1";
	/**
	 *  2：教师
	 */
    public static String USER_TYPE_TEACHER = "2";
	/**
	 *  3：VIP
	 */
    public static String USER_TYPE_VIP = "3";
	/**
	 *  3：公安机关
	 */
    public static String USER_TYPE_POLICE = "4";
    /**默认密码*/
    public static String DEFAULT_PASSWORD = "000000";
}
