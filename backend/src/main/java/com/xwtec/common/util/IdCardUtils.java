package com.xwtec.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: IdCardUtils
 * @Auther: Jerry
 * @Date: 2019/5/22 14:41
 * @Desctiption: 身份证处理工具类
 * @Version: 1.0
 */
public class IdCardUtils {

    /**
     * 正则表达式
     */
    private static final String REGEX = "[0-9]+[X|x]{1}";

    /**
     * getIdCardAfterSixNum: 获取身份证后六位数字，如果最后一位是字母则略过字母取后六位数字 <br/>
     * @auther: Jerry
     * @param: [id]
     * @date: 2019/5/22 14:42
     * @return: java.lang.String
     */
    public static String getIdCardAfterSixNum(String id){
        Pattern pattern = Pattern.compile(REGEX);
        Matcher m = pattern.matcher(id);
        boolean b = m.matches();
        if(b){
            id = id.substring(id.length()-7,id.length()-1);
        }else{
            id = id.substring(id.length()-6);
        }
        return id;
    }

    /**
     * getSixNum: 截取身份证后六位 <br/>
     * @auther: Jerry
     * @param: [idCard]
     * @date: 2019/5/23 14:01
     * @return: java.lang.String
     */
    public static String getSixNum(String idCard){
        idCard = idCard.substring(idCard.length()-6);
        return idCard;
    }
}
