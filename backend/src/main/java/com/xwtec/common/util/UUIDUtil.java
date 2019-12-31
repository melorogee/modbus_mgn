package com.xwtec.common.util;

import java.util.UUID;

/**
 * 生成编码
 * 
 * @author spj
 * @date 2018-5-23
 */
public class UUIDUtil {

	private UUIDUtil() {

	}

	/**
	 * 生成32位编码
	 * 
	 * @return
	 */
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		return uuid.replace("-", "");
	}
	
	/**
	 * 获取指定长度编码：编码长度1-32位
	 * 
	 * @param length
	 * @return
	 */
	public static String getSpecifiedLengthUUID(int length) {
		if (length <= 0) {
			return getUUID();
		}
		return getUUID().substring(0, length);
	}
	
	/**
     * 
     * <Description>根据传入长度生成响应的随机数 <br> 
     *  
     * @author zhanleai<br>
     * @CreateDate 2018-6-3 <br>
     * @param len 随机的位数
     * @return 随机数
     */
    public static String generateRandomStr(int len) {
        // 字符源，可以根据需要删减
        String generateSource = "0123456789abcdefghigklmnopqrstuvwxyz";
        String rtnStr = "";
        for (int i = 0; i < len; i++) {
            // 循环随机获得当次字符，并移走选出的字符
            String nowStr = String.valueOf(generateSource.charAt((int) Math.floor(Math.random() * generateSource.length())));
            rtnStr += nowStr;
            generateSource = generateSource.replaceAll(nowStr, "");
        }
        return rtnStr;
    }
}
