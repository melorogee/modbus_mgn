package com.xwtec.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xwtec.modules.system.entity.SysLog;
import com.xwtec.modules.system.mapper.SysLogMapper;
import com.xwtec.modules.system.service.ISysLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 系统日志表 服务实现类
 * </p>
 *
 * @author zhangweijian
 * @since 2018-12-26
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

	@Resource
	private SysLogMapper sysLogMapper;
	
	/**
	 * @功能：清空所有日志记录
	 */
	@Override
	public void removeAll() {
		sysLogMapper.removeAll();
	}

	@Override
	public Long findTotalVisitCount() {
		return sysLogMapper.findTotalVisitCount();
	}

	@Override
	public Long findTodayVisitCount() {
		return sysLogMapper.findTodayVisitCount();
	}

	@Override
	public Long findTodayIp() {
		return sysLogMapper.findTodayIp();
	}

}
