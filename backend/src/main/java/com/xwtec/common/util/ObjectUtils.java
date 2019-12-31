package com.xwtec.common.util;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: ObjectUtils
 * @Auther: Jerry
 * @Date: 2019/5/21 10:33
 * @Desctiption: 判断Object、List、Map是否为空工具类
 * @Version: 1.0
 */
public class ObjectUtils {

	/**
	 * 
	 * 方法描述 如果对象为非空返回true 否则返回false
	 *
	 * @param obj
	 * @return
	 */
	public static boolean isNotNull(Object obj) {
		return obj != null ? true : false;
	}

	/**
	 * 
	 * 方法描述 如果对象为空返回 true 否则返回false
	 *
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj) {
		return obj == null ? true : false;
	}

	/**
	 * 
	 * 方法描述 判断Map集合非null 非空 返回true 否则返回false
	 *
	 * @param map
	 * @return
	 */
	public static boolean isNotNull(Map<Object, Object> map){
		if(map != null && !map.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 方法描述 判断Map集合是null或者空 返回true 否则返回false
	 *
	 * @param map
	 * @return
	 */
	public static boolean isNull(Map<Object, Object> map){
		if(map == null || map.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 方法描述 判断Map集合非null 非空 返回true 否则返回false
	 *
	 * @param list
	 * @return
	 */
	public static boolean isNotNull(List<Object> list){
		if(list != null && !list.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 方法描述 判断Map集合是null或者空 返回true 否则返回false
	 *
	 * @param list
	 * @return
	 */
	public static boolean isNull(List<Object> list){
		if(list == null || list.isEmpty()) {
			return true;
		}
		return false;
	}
}
