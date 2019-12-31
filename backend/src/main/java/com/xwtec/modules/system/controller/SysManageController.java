package com.xwtec.modules.system.controller;

import com.xwtec.common.api.vo.Result;
import com.xwtec.common.aspect.annotation.AutoLog;
import com.xwtec.common.util.SysCacheUtil;
//import com.xwtec.modules.employee.service.IEmployeeEntityService;
//import com.xwtec.modules.employee.service.IMerchantService;
//import com.xwtec.modules.employee.service.INumberEntityService;
//import com.xwtec.modules.settlement.service.ISettlementService;
//import com.xwtec.modules.task.service.ITaskEntityService;
//import com.xwtec.modules.wish.service.IWishOrderInfoEntityService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统管理 前端控制器:  <br/>
 * @ClassName: SysManageController
 * @Auther: zhuchen
 * @Date: 2019/3/28 11:30
 * @Desctiption: TODO
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/sys/manage")
public class SysManageController {

    @Autowired
    private SysCacheUtil sysCacheUtil;

    /**
     * 获取首页数据:  <br/>
     * @auther: zhuchen
     * @param:
     * @date: 2019/3/28 12:23
     * @return:
     */
    @GetMapping(value = "/queryHomeDataManage")
    @AutoLog("获取首页数据")
    @ApiOperation(value = "获取首页数据", notes = "首页", produces = "application/json")
    public Result<Map<String,Object>> queryHomeDataManage(HttpServletRequest req){
        Result<Map<String,Object>> result = new Result<>();
        Map<String,Object> resultMap = new HashMap<>();
        // 获取登录用户信息
        Map<String,String> loginUserMap = sysCacheUtil.getLoginRoleInfo(req);
        resultMap.put("userId",loginUserMap.get("userId"));
        resultMap.put("roleCode",loginUserMap.get("roleCode"));
        log.info(" >>>>>>>>>>>>>>>>> 缓存中获取登录用户信息loginMap："+loginUserMap);
//        // 今日意向单统计    hkOrder 号卡数量   kdOrder 宽带数量   -- 石亚雄 2019-03-28
//        Map<String,Object> orderCountsMap = wishOrderInfoEntityService.homeOrderCount(req,resultMap);
//        // -- 石亚雄 2019-03-28
//        // Map<String, Object> orderTreeReport = wishOrderInfoEntityService.orderTreeReport(req, resultMap);
//        /*酬金首页数据统计： com.xwtec.modules.settlement.service.impl.SettlementServiceImpl   -- 江金宝 2019-03-28
//        出参{fixReward（固定酬金），overReward（达量酬金），dayReward（每日号卡酬金）}，每日宽带和其他业务默认0*/
//        Map<String,Object> settleInfoForHomeMap = settlementService.querySettleInfoForHome(resultMap);
//        // 外呼商：{count=4, list=[{sum=2, day=2019-03-21}, {sum=1, day=2019-03-22}, {sum=1, day=2019-03-25}]} -- 郭梦怀 2019-03-28
//        Map<String, Object> merchantCountByDay = merchantService.queryMerchantCountByDay();
//        // 外呼商员工：{count=3, list=[{sum=1, day=2019-03-18}, {sum=1, day=2019-03-21}, {sum=1, day=2019-03-25}]}  -- 郭梦怀 2019-03-28
//        Map<String, Object> employeeCount = employeeEntityService.queryEmployeeCount(resultMap);
//        // 工号：{count=6}   -- 郭梦怀 2019-03-28
//        Map<String, Object> numberCount = numberEntityService.queryNumberCount(resultMap);
//        Map<String, Object> taskTotalAndDetail = taskEntityService.getTaskTotalAndDetail(resultMap);
//
//        log.info(" >>>>>>>>>>>>>>>>> 今日意向单统计orderCountsMap："+orderCountsMap);
//        log.info(" >>>>>>>>>>>>>>>>> 酬金首页数据统计settleInfoForHomeMap："+settleInfoForHomeMap);
//        log.info(" >>>>>>>>>>>>>>>>> 外呼商数据统计merchantCountByDay："+merchantCountByDay);
//        log.info(" >>>>>>>>>>>>>>>>> 外呼商员工数据统计employeeCount："+employeeCount);
//        log.info(" >>>>>>>>>>>>>>>>> 工号数据统计numberCount："+numberCount);
//        log.info(" >>>>>>>>>>>>>>>>> 任务数据统计taskTotalAndDetail："+taskTotalAndDetail);
//
//        resultMap.put("orderCountsMap",orderCountsMap);
//        resultMap.put("settleInfoMap",settleInfoForHomeMap);
//        resultMap.put("merchantCountByDay",merchantCountByDay);
//        resultMap.put("employeeCount",employeeCount);
//        resultMap.put("numberCount",numberCount);
//        resultMap.put("taskTotalAndDetail",taskTotalAndDetail);
        result.setSuccess(true);
        result.setResult(resultMap);
        return result;
    }

    /**
     * 意向单总数:  <br/>
     * @auther: zhuchen
     * @param:
     * @date: 2019/3/28 12:23
     * @return:
     */
    @GetMapping(value = "/queryOrderTreeReport")
    @AutoLog("意向单总数")
    @ApiOperation(value = "意向单总数", notes = "首页", produces = "application/json")
    public Result<Map<String,Object>> queryOrderTreeReport(HttpServletRequest req){
        Result<Map<String,Object>> result = new Result<>();
        Map<String,Object> resultMap = new HashMap<>();
        // 获取登录用户信息
        Map<String,String> loginUserMap = sysCacheUtil.getLoginRoleInfo(req);
        resultMap.put("userId",loginUserMap.get("userId"));
        resultMap.put("roleCode",loginUserMap.get("roleCode"));
        log.info(" >>>>>>>>>>>>>>>>> 缓存中获取登录用户信息loginMap："+loginUserMap);
        // 意向单总数 -- 石亚雄 2019-03-28
//        Map<String, Object> orderTreeReport = wishOrderInfoEntityService.orderTreeReport(req, resultMap);
//        log.info(" >>>>>>>>>>>>>>>>> 意向单总数orderTreeReport："+orderTreeReport);
//
//        resultMap.put("orderTreeReport",orderTreeReport);
        result.setSuccess(true);
        result.setResult(resultMap);
        return result;
    }
}
