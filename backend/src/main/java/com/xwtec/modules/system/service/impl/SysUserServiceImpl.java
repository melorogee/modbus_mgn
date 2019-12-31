package com.xwtec.modules.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xwtec.common.constant.RoleInfoConstant;
import com.xwtec.common.exception.VerificationException;
import com.xwtec.common.util.PasswordUtil;
import com.xwtec.common.util.oConvertUtils;
import com.xwtec.modules.system.entity.SysUser;
import com.xwtec.modules.system.entity.SysUserRole;
import com.xwtec.modules.system.mapper.SysUserMapper;
import com.xwtec.modules.system.mapper.SysUserRoleMapper;
import com.xwtec.modules.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleInfo;
import java.util.*;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author scott
 * @since 2018-12-20
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
	
	@Autowired
	private SysUserMapper userMapper;

	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;

//	@Autowired
//	private IEmployeeEntityService employeeEntityService;
//
//	@Autowired
//	private IMerchantService merchantService;
	
	@Override
	public SysUser getUserByName(String username) {
		return userMapper.getUserByName(username);
	}


	@Override
	public synchronized void addUserWithRole(SysUser user, String roles) {
		String id =UUID.randomUUID().toString().replace("-", "");
		user.setId(id);
        String rolecode = sysUserRoleMapper.getRoleCodeByRoleId(roles);
		user.setRoleId(rolecode);
		this.save(user);
		if(oConvertUtils.isNotEmpty(roles)) {
			String[] arr = roles.split(",");
			for (String roleId : arr) {
				SysUserRole userRole = new SysUserRole(id, roleId);
				sysUserRoleMapper.insert(userRole);
			}
		}
	}

	@Override
	public synchronized void addUserInfoForBase(SysUser user) throws Exception{
		log.info("  start ........   ISysUserService.addUserInfoForBase");
		log.info("  入参："+ JSONObject.toJSON(user));
		log.info("  开始添加用户成功！");
		/*String id =UUID.randomUUID().toString().replace("-", "");
		user.setId(id);*/
		user.setCreateTime(new Date());//设置创建时间
		String salt = oConvertUtils.randomGen(8);
		user.setSalt(salt);
		String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
		user.setPassword(passwordEncode);
		user.setStatus(user.getStatus()==null?1:user.getStatus());//状态(1：正常  2：冻结 ）
		user.setDelFlag("0");//删除状态（0，正常，1已删除）
		this.save(user);//保存用户
		// 角色id
		String roleCodes = user.getRoleId();
		// 支持多角色或单角色
		if(oConvertUtils.isNotEmpty(roleCodes)) {
			String[] arr = roleCodes.split(",");
			for (String roleCode : arr) {
				String roleId = sysUserRoleMapper.getRoleByRoleCode(roleCode);
				SysUserRole userRole = new SysUserRole(user.getId(), roleId);
				sysUserRoleMapper.insert(userRole);
			}
		}
		log.info("  end ........   ISysUserService.addUserInfoForBase");
	}

	@Override
	public synchronized void editUserWithRole(SysUser user, String type) {
		log.info("   start ....... ISysUserService.editUserWithRole");
		/*this.updateById(user);*/
		//先删后加    取消物理删除  modify by ZHUCHEN 2019-03-13
		/*sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, user.getId()));
		if(oConvertUtils.isNotEmpty(roles)) {
			String[] arr = roles.split(",");
			for (String roleId : arr) {
				SysUserRole userRole = new SysUserRole(user.getId(), roleId);
				sysUserRoleMapper.insert(userRole);
			}
		}*/
		SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
		String loginUserId = sysUser.getId();// 当前登录人的userId
		user.setUpdateBy(loginUserId);
        String delFlag = user.getDelFlag()==null?"":user.getDelFlag();// 是否删除操作
		// 删除状态（0，正常，1已删除）
		if(delFlag!=null && delFlag.equals("1")){
			// 删除
            user.setStatus(2);//删除时同时停用该账号
			SysUserRole userRole = new SysUserRole();
			userRole.setUserId(user.getId());
			userRole.setUpdateBy(loginUserId);
			sysUserRoleMapper.updateRoleByUserId(userRole);
		}
		log.info("   入参user ======"+JSONObject.toJSON(user));
		// type 如果来自修改外呼人员、外呼主管调用，则不走以下方法
		// 根据用户角色code更新外呼人员或外呼主管信息
        if(type.equals("userEdit")&&user.getRoleId()!=null){
//			log.info("  系统管理员修改用户信息同步外呼人员、外呼主管");
//			// 外呼主管
//			if(user.getRoleId().equals("000002")){
//				if(delFlag.equals("1")){// 删除外呼主管
//					user.setRemarkA("adminDel");
//					merchantService.settingsMerchantByUserAndRole(user);
//				}else{
//					Map<String, String> param = new HashMap<>();
//					param.put("userStatus",user.getStatus().toString());
//					param.put("merchantUserId",user.getId());
//					param.put("merchantNo",user.getRemarkA()); // 外呼商名称
//					param.put("merchantContacts",user.getRealname());
//					param.put("merchantTel",user.getPhone());
//					param.put("merchantAddress",user.getRemarkB()); // 公司地址
//					param.put("merchantRemark",user.getRemark()); // 备注
//					// 更新外呼主管信息
//					merchantService.updateMerchantByMap(param);
//				}
//			}else if(user.getRoleId().equals("000003")){// 外呼人员
//				EmployeeEntity employeeEntity = new EmployeeEntity();
//				employeeEntity.setIsF("1");// 管理员修改用户信息 isF = 1
//				employeeEntity.setEmployeeUserId(user.getId());// 用户id
//				if(delFlag.equals("1")){// 删除外呼人员
//					employeeEntity.setEmployeeIsDelete("1");
//					employeeEntityService.modifyEmployeeEntityById(employeeEntity);
//				}else{// 更新外呼人员信息（包括修改状态）
//					employeeEntity.setEmployeeName(user.getRealname()); // 姓名
//					employeeEntity.setEmployeeTel(user.getPhone()); // 联系手机号码
//					employeeEntity.setEmployeeStatus(user.getStatus().toString()); // 状态
//					employeeEntity.setEmployeeRemark(user.getRemark());//备注
//					employeeEntityService.modifyEmployeeEntity(employeeEntity);
//				}
//			}
		}
		// 更新用户信息
		userMapper.updateUserInfo(user);
		log.info("   end ....... ISysUserService.editUserWithRole");
	}


	@Override
	public List<String> getRole(String username) {
		return sysUserRoleMapper.getRoleByUserName(username);
	}

	@Override
	public Map<String,String> getRoleInfoByUserName(String userName) {
		return sysUserRoleMapper.getRoleInfoByUserName(userName);
	}

	@Override
	public IPage<SysUser> getPage(Page<SysUser> page, SysUser user) {
		// 判断时间是否为空

		List<SysUser> list = userMapper.getPageList(user);
		//若是试题相关角色查询其关联知识点
		list.forEach(sysUser->{
			if (RoleInfoConstant.QUESTION_ENTRANT.equals(sysUser.getRoleCode())
					|| RoleInfoConstant.AUDITOR.equals(sysUser.getRoleCode())
					|| RoleInfoConstant.TEACHER.equals(sysUser.getRoleCode())
					||"533b1de94a68288c4a2f40361c37bbbd".equals(sysUser.getRoleCode())) {  //试卷管理员角色
				sysUser.setKnowList(userMapper.selectKnowListByUser(sysUser.getId()));
				sysUser.setKnowNameList(userMapper.selectKnowNameListByUser(sysUser.getId()));
			}
		});
		page.setRecords(list);
		return page;
	}

	/**
	 * 保存用户知识点关系
	 * @param params
	 */
	@Override
	public void saveUserKnow(Map<String, Object> params) throws Exception{
		//获取用户id
		String userId = (String) params.get("userId");
		String knowList = (String) params.get("knowList");
		//校验
		if(StringUtils.isBlank(userId)) throw new VerificationException("用户id不可为空");
		if(StringUtils.isBlank(knowList)) throw new VerificationException("知识点不可为空");
		//先清除原来的knowId
		userMapper.deleteUserKnow(userId);
		//再新增现在的KnowId
		params.put("knowList", Arrays.asList(knowList.split(",")));
		userMapper.insertUserKnow(params);
	}

}
