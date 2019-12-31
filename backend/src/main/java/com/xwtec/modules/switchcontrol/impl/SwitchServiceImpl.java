package com.xwtec.modules.switchcontrol.impl;

import com.xwtec.modules.switchcontrol.ISwitchService;
import com.xwtec.modules.switchcontrol.mapper.SwitchMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SwitchServiceImpl
 *
 * @author spj
 * @since 2019-10-28
 */
@Service
public class SwitchServiceImpl implements ISwitchService {

    @Resource
    private SwitchMapper switchMapper;


    @Override
    public List<Map<String, Object>> getSwitchList() {
        List<Map<String, Object>> boxList = switchMapper.getAllBox();
        List<Map<String, Object>> finalList = new ArrayList<>();
        for(int i = 0; i< boxList.size();i++){
            Map<String,Object> tempBoxMap = boxList.get(i);
            List<Map<String, Object>> dataMap = switchMapper.getSwitchList(boxList.get(i).get("boxid").toString());
            tempBoxMap.put("boxdetail",dataMap);
            finalList.add(tempBoxMap);
        }
        return finalList;
    }

    @Override
    public void changeSwitchSate(String code, String value) {
        switchMapper.changeSwitchSate(code,value);
    }

    @Override
    public void insertSwitchLog(String code,String slaveID, String address, String value) {
        switchMapper.insertSwitchLog(code,slaveID,address,value);

    }

    @Override
    public List<Map<String, Object>> getSwitchLog() {
        return switchMapper.getSwitchLog();
    }

    @Override
    public void insertElecticLog(Map<String, Object> map) {
         switchMapper.insertElecticLog(map);

    }

    @Override
    public List<Map<String, Object>> getElectConfig() {
        return switchMapper.getElectConfig();
    }

    @Override
    public List<Map<String, Object>> getElecList() {
        return switchMapper.getElecList();
    }

    @Override
    public Map<String, Object> getSwitchIpAndPort(String code) {
        return switchMapper.getSwitchIpAndPort(code);
    }

    @Override
    public int getUser(Map<String, Object> param) {
        return switchMapper.getUser(param);
    }
}
