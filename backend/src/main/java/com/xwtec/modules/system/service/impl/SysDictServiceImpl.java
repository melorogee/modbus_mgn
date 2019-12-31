package com.xwtec.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xwtec.modules.system.entity.SysDict;
import com.xwtec.modules.system.mapper.SysDictMapper;
import com.xwtec.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author zhangweijian
 * @since 2018-12-28
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

	@Autowired
	private SysDictMapper sysDictMapper;
	@Override
	public List<Map<String, String>> queryDictItemsByCode(String code) {
		return sysDictMapper.queryDictItemsByCode(code);
	}
	
	@Override
	public String queryDictTextByKey(String code, String key) {
		return sysDictMapper.queryDictTextByKey(code, key);
	}

}
