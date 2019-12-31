package com.xwtec.common.util;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName: ExamTaskTimeUtils
 * @Auther: Jerry
 * @Date: 2019/7/3 14:40
 * @Desctiption: 考试任务时间计算工具类
 * @Version: 1.0
 */
public class ExamTaskTimeUtils {

    /**
     * getAFewDaysLater: 获取截至有效期结束的时间 <br/>
     * @auther: Jerry
     * @param: [param：now-当前时间；day-天数]
     * @date: 2019/7/3 15:10
     * @return: java.lang.String
     */
    public static String getAFewDaysLater(String now,String day){
        Date dt = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            dt = sdf.parse(now);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            // 日期加xx天
            rightNow.add(Calendar.DAY_OF_YEAR,Integer.parseInt(day));
            // 返回计算后的时间
            return sdf.format(rightNow.getTime());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * getTime: 获取计算后的时间 <br/>
     * @auther: Jerry
     * @param: [param：format-时间格式；now-当前时间；day-天数；examTime-考试时长]
     * @date: 2019/7/3 14:41
     * @return: java.lang.String
     */
    public static String getTime(Map<String,String> param){
        Date dt = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(param.get("format"));
            dt = sdf.parse(param.get("now"));
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            // 日期加xx天
            if (StringUtils.isNotBlank(param.get("day"))){
                rightNow.add(Calendar.DAY_OF_YEAR,Integer.parseInt(param.get("day")));
            }
            // 日期加上考试时长
            if (StringUtils.isNotBlank(param.get("examTime"))){
                rightNow.add(Calendar.MINUTE,Integer.parseInt(param.get("examTime")));
            }
            // 返回计算后的时间
            return sdf.format(rightNow.getTime());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
