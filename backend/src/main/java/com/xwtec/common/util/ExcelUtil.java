package com.xwtec.common.util;

/**
 * @ClassName: ExcelUtil
 * @Auther: taogang
 * @Date: 2019/3/14 10:17
 * @Desctiption: excel操作的工具类
 * @Version: 1.0
 */
public class ExcelUtil {

    /**
     * isExcelFile
     * @auther: taogang
     * @param: fileName 文件名
     * @date: 2019/3/14 10:24
     * @desc:  判断是不是excel文件
     * @return: boolean
     */
    public static boolean isExcelFile(String fileName){
        if (fileName == null || !(isExcel2003(fileName) || isExcel2007(fileName))){
            return false;
        }
        return true;
    }

    /**
     * isExcel2003
     * @auther: taogang
     * @param: fileName
     * @date: 2019/3/14 10:25
     * @desc: 判断excel的版本是不是2003
     * @return: boolean
     */
    public static boolean isExcel2003(String fileName){
        return fileName.matches("^.+\\.(?i)(xls)$");
    }

    /**
     * isExcel2007
     * @auther: taogang
     * @param: fileName
     * @date: 2019/3/14 10:25
     * @desc: 判断excel的版本是不是2007
     * @return: boolean
     */
    public static boolean isExcel2007(String fileName){
        return fileName.matches("^.+\\.(?i)(xlsx)$");
    }
    
    /**
     * isCsv
     * @auther: taogang
     * @param: fileName
     * @date: 2019/3/17 16:28
     * @desc: 判断文件是否是csv文件
     * @return: boolean
     */
    public static boolean isCsv(String fileName){
        return fileName.matches("^.+\\.(?i)(csv)$");
    }

}