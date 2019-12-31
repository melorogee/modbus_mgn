package com.xwtec.modules.switchcontrol;


import com.xwtec.modules.switchcontrol.tools.ModbusUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.*;

/**
 * 开关控制
 *
 * @author wufanxin
 * @since 2019-10-28
 */
@RestController
@RequestMapping("/switch")
@Slf4j
public class SwitchController {



    @Autowired
    private ISwitchService switchService;



    /**
     * 获取开关信息列表
     *
     * @return
     */
    @GetMapping("/getSwitchList")
    @CrossOrigin
    public List<Map<String,Object>> getSwitchList() {

        List<Map<String, Object>> list =  this.switchService.getSwitchList();

        return list;
    }





    //调用api控制modbus开关
    @PostMapping("/goControl")
    @CrossOrigin
    public String getSwitchList(String code) {
        //解析code
        String slaveID =  code.split(" ")[0];
        String address =  code.split(" ")[1];
        String value =  code.split(" ")[2];

        Map<String,Object> confMap = switchService.getSwitchIpAndPort(code);

        try {
            ModbusUtil.writeDigitalOutput(value.equals("0") ? confMap.get("ip_off").toString() : confMap.get("ip_on").toString(),
                    value.equals("0") ? Integer.parseInt(confMap.get("port_off").toString()) : Integer.parseInt(confMap.get("port_on").toString()),
                    Integer.parseInt(slaveID), Integer.parseInt(address), Integer.parseInt(value));

            //改变现有状态
            switchService.changeSwitchSate(code, value);
            //插入日志
            switchService.insertSwitchLog(code, slaveID, address, value);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            //如果更改失败 不改状态
            value = "2";
        }
        return value;
    }


    /**
     * 获取开关信息日志列表
     *
     * @return
     */
    @GetMapping("/getSwitchLog")
    @CrossOrigin
    public List<Map<String,Object>> getSwitchLog() {

        List<Map<String, Object>> list =  this.switchService.getSwitchLog();

        return list;
    }

    @GetMapping("/getElecLog")
    @CrossOrigin
    public List<Map<String,Object>> getElecList() {

        List<Map<String, Object>> list =  this.switchService.getElecList();

        return list;
    }




    //登录
    @PostMapping("/login")
    @CrossOrigin
    public Map<String,Object> login(@RequestParam Map<String,Object> params) {
        //查询数据库用户，返回结果
        int count =  switchService.getUser(params);
        Map<String,Object> retMap = new HashMap<>();

        if(count > 0 ){
            //成功
            Map<String,Object> retSubMap = new HashMap<>();
            retSubMap.put("current_authority","user");
            retSubMap.put("user_name",params.get("user_name"));
            retMap.put("data",retSubMap);
        }
        //如果查询到返回
        return retMap;
    }



}
