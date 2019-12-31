package com.xwtec.modules.switchcontrol;

import java.util.List;
import java.util.Map;

/**
 * 开关Service
 *
 * @author spj
 * @since 2019-10-28
 */
public interface ISwitchService {

    /**
     * getSwitchList
     *
     * @return
     */
    List<Map<String, Object>> getSwitchList();

    void changeSwitchSate(String code,String value);

    void insertSwitchLog(String code,String slaveID,String address,String value);

    List<Map<String,Object>> getSwitchLog();

    void insertElecticLog(Map<String,Object> map);

    List<Map<String,Object>> getElectConfig();

    List<Map<String,Object>> getElecList();

    Map<String,Object> getSwitchIpAndPort(String code);

    int  getUser (Map<String,Object> param);

}
