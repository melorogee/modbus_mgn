package com.xwtec.modules.switchcontrol.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * SwitchMapper
 *
 * @author spj
 * @since 2019-10-28
 */
@Mapper
public interface SwitchMapper {

    /**
     * 开关列表
     *
     * @return
     */
    List<Map<String, Object>> getSwitchList(String boxid);

    List<Map<String, Object>> getAllBox();

    void changeSwitchSate(String code,String value);

    void insertSwitchLog(String code,String slaveID, String address,String value);

    List<Map<String,Object>> getSwitchLog();

    void insertElecticLog(Map<String,Object> map);

    List<Map<String,Object>> getElectConfig();

    List<Map<String,Object>> getElecList();


    Map<String,Object> getSwitchIpAndPort(String code);

    int  getUser (Map<String,Object> param);





}
