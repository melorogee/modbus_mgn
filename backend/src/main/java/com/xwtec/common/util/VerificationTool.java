package com.xwtec.common.util;

import com.xwtec.common.exception.VerificationException;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * 校验工具
 *
 * @author jiangjinbao
 * @date 2018-5-23
 */
public class VerificationTool {

    /**
     * 校验非空
     *
     * @param source
     * @param isContainSpecial
     * @throws VerificationException
     */
    public static boolean verifyNotEmpty(String source, boolean isContainSpecial) {
        if (isContainSpecial) {
            if (StringUtils.isBlank(source) || source.toLowerCase().matches("^(undefined)|(null)$")) {
                return false;
            }
        } else {
            if (StringUtils.isBlank(source)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 校验非空
     *
     * @param source
     * @throws VerificationException
     */
    public static boolean verifyNotEmpty(String source) {
        return verifyNotEmpty(source, false);
    }

    /**
     * 校验手机号
     *
     * @param source
     * @throws VerificationException
     */
    public static boolean verifyPhoneNumber(String source) {
        String reg = "^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$";
        return verifyByReg(source, reg);
    }

    /**
     * 校验邮箱
     *
     * @param source
     * @throws VerificationException
     */
    public static boolean verifyMail(String source) {
        String reg = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        return verifyByReg(source, reg);
    }

    /**
     * 校验身份证
     *
     * @param source
     * @throws VerificationException
     */
    public static boolean verifyIDCard(String source) {
        String reg = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$";
        return verifyByReg(source, reg);
    }

    /**
     * 根据正则校验
     *
     * @param source
     * @param reg
     * @throws VerificationException
     */
    public static boolean verifyByReg(String source, String reg) {
        if (StringUtils.isBlank(source) || !source.matches(reg)) {
            return false;
        }
        return true;
    }

    /**
     * 校验字符串最大长度
     *
     * @param source
     * @param maxLength
     */
    public static boolean checkMaximumLength(String source, int maxLength, boolean isTrim) {
        if (!verifyNotEmpty(source)) return false;
        if (isTrim) {
            source = StringUtils.trim(source);
        }
        if (source.length() > maxLength)
            return false;
        else
            return true;
    }

    /**
     * 校验字符串最大长度
     *
     * @param source
     * @param maxLength
     */
    public static boolean checkMaximumLength(String source, int maxLength) {
        return checkMaximumLength(source, maxLength, false);
    }


    public static boolean verifyNotEmptyForType(Object obj) {
        if (null == obj) return false;
        if (obj instanceof Collection && ((Collection) obj).isEmpty()) return false;
        if (obj instanceof Map && ((Map) obj).isEmpty()) return false;
        if (obj instanceof String) return verifyNotEmpty((String) obj);
        return true;
    }

    public static Boolean verifyNum(String str) {
        Pattern pattern = Pattern.compile("^[0-9]\\d*$");
        if(!pattern.matcher(str).matches()){
            //数字
            return true;
        } else {
            //非数字
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println(verifyIDCard("52242519950612841x"));
    }
}
