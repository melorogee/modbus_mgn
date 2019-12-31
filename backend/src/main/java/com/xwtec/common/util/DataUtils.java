package com.xwtec.common.util;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @Author taogang
 * @ClassName DataUtils
 * @Date 2019/5/21 16:14
 * @Description 数据处理工具类
 */
public class DataUtils {

    /**
     * checkObjectIsNull
     * @param object
     * @return boolean
     * @author taogang
     * @date 2019/5/21 16:16
     * @description 判断传入的对象是否为空,空就是true，不根据长度判断
     */
    public static boolean checkObjectIsNull(Object object){
        if(null != object){
            String temp = object.toString().toLowerCase();
            if(!"".equals(temp) && !"undefined".equals(temp) && !"null".equals(temp)){
                return false;
            }
        }
        return true;
    }

    /**
     * getObjectToString
     * @param object
     * @return java.lang.String
     * @author taogang
     * @date 2019/6/12 09:39
     * @description 把objec变成String类型
     */
    public static String getObjectToString(Object object){
        if(null != object){
            String temp = object.toString().toLowerCase();
            if(!"".equals(temp) && !"undefined".equals(temp) && !"null".equals(temp)){
                return object.toString();
            }
        }
        return null;
    }

    /**
     * getRandNum
     * @param end 随机数的最大范围
     * @param num 随机数的个数
     * @return java.util.List<java.lang.Integer>
     * @author taogang
     * @date 2019/6/12 20:52
     * @description 生成1到end之间，num个随机数,当end和num一样大的时候，那么久取全量的0到end之间的所有正整数
     */
    public synchronized static List<Integer> getRandNum(int end,int num){
        return getRandNum(1,end+1,num);
    }

    /**
     * getRandNum
     * @param start
     * @param end
     * @param num
     * @return java.util.List<java.lang.Integer>
     * @author taogang
     * @date 2019/7/12 09:38
     * @description TO DO
     */
    public synchronized static List<Integer> getRandNum(int start,int end,int num){
        Random rd = new Random();
        List<Integer> result = new ArrayList<Integer>();
        List<Integer> temp = new ArrayList<Integer>();
        for(int i = start;i < end;i++){
            temp.add(i);
        }

        if(end == num){
            result = temp;
        }else{
            for(int j = 0; j < num ; j++){
                int index = rd.nextInt((end - start)- j);
                result.add(temp.get(index));
                temp.remove(index);
            }
        }

        return result;
    }

    /**
     * getOneRandNum
     * @param start 开始值
     * @param end  结束值
     * @return java.lang.Integer
     * @author taogang
     * @date 2019/6/25 14:54
     * @description 获取从start到end直接的一个随机数
     */
    public synchronized static Integer getOneRandNum(int start,int end){
        Integer result = null;
        Random rd = new Random();
        List<Integer> temp = new ArrayList<Integer>();
        for(int i = start;i <= end;i++){
            temp.add(i);
        }
        for(int j = 0; j < temp.size() ; j++){
            int index = rd.nextInt(end - j);
            result = temp.get(index);
            temp.remove(index);
        }
        return result;
    }

    /**
     * entityToMap
     * @param object
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author taogang
     * @date 2019/6/19 14:03
     * @description 实体类转map
     */
    public static Map<String, Object> entityToMap(Object object) {
        Map<String, Object> map = new HashMap();
        for (Field field : object.getClass().getDeclaredFields()){
            try {
                boolean flag = field.isAccessible();
                field.setAccessible(true);
                Object o = field.get(object);
                map.put(field.getName(), o);
                field.setAccessible(flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
