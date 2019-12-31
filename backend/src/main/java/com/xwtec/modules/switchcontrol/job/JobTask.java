package com.xwtec.modules.switchcontrol.job;

import com.xwtec.modules.switchcontrol.ISwitchService;
import com.xwtec.modules.switchcontrol.tools.ModbusUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@EnableScheduling
public class JobTask {


    @Autowired
    private ISwitchService switchService;

    @Scheduled(cron = "0/10 * * * * *")
    public void run() throws InterruptedException {
        //获取十项配置的
        List<Map<String,Object>> config = switchService.getElectConfig();
        Map<String,Object> paramMap = new HashMap<>();

        for(int i = 0; i <config.size(); i++){
            String key = config.get(i).get("key").toString();
            String slaveId = config.get(i).get("slaveid").toString();
            String address = config.get(i).get("address").toString();
            String ip = config.get(i).get("ip").toString();
            String port = config.get(i).get("port").toString();

            //真实表用获取值
            try {
                float value = ModbusUtil.readRegister(ip, Integer.parseInt(port), Integer.parseInt(address), Integer.parseInt(slaveId));
                if(key.equals("freq")){
                    value = value * 0.1f;
                }
                paramMap.put(key,value);

            }catch (Exception e){
                e.printStackTrace();
            }


        }

        //插入记录表
        if (paramMap.size() >0 ) {
            switchService.insertElecticLog(paramMap);
        }
    }
}
