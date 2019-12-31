package com.xwtec.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xwtec.modules.system.entity.SysDict;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @author zhangweijian
 * @since 2018-12-28
 */
public interface SysDictMapper extends BaseMapper<SysDict> {
	
	public List<Map<String,String>> queryDictItemsByCode(@Param("code") String code);
	
	public String queryDictTextByKey(@Param("code") String code,@Param("key") String key);

}
