package com.xwtec.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 类 {@code LocalDateUtils}
 *
 * <p> 日期工具类
 *
 * @author jiangjinbao
 * @version 1.0.0
 * @since 2019年04月02日 19:08
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalDateUtils {

    public static String nowFormat(String format) {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(format));
    }

    public static String nowFormatDefault() {
        return nowFormat("yyyy-MM-dd");
    }

    public static String nowTimeFormatDefault(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static final String YEAR = "year";

    public static final String MONTH = "month";

    public static final String WEEK = "week";

    public static final String DAY = "day";

    /**
     * getStartEndTime
     * @param time 开始时间  "yyyy-MM-dd"
     * @param chronon 计时单位
     * @param num  差值
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @author taogang
     * @date 2019/10/24 09:46
     * @description 获取给定的计时单位前num个时间的自然开始计量单位的第一天
     */
    public static String getStartTime(String time,String chronon,int num){
        String[] times = time.split("-");
        LocalDate ldStart = LocalDate.of(Integer.parseInt(times[0]),
                Integer.parseInt(times[1]),Integer.parseInt(times[2]));
        switch (chronon){
            case LocalDateUtils.YEAR:
                ldStart = ldStart.plusYears(-num);
                ldStart = ldStart.withDayOfYear(1);
                break;
            case LocalDateUtils.MONTH:
                ldStart = ldStart.plusMonths(-num);
                ldStart = ldStart.withDayOfMonth(1);
                break;
            case LocalDateUtils.WEEK:
                ldStart = ldStart.plusWeeks(-num);
                ldStart = ldStart.with(DayOfWeek.MONDAY);
                break;
            case LocalDateUtils.DAY:
                ldStart = ldStart.plusDays(-num);
                break;
        }
        return ldStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static void main(String[] fad) {
        System.out.println(getStartTime(LocalDateUtils.nowFormatDefault(), LocalDateUtils.WEEK, 4));
    }

    /**
     * getLastDate
     * @param time
     * @param chronon
     * @param num
     * @return java.lang.String
     * @author taogang
     * @date 2019/11/6 14:59
     * @description 获取前num个周期的周期最后一天日期
     */
    public static String getLastDate(String time,String chronon,int num){

        String[] times = time.split("-");
        LocalDate ldStart = LocalDate.of(Integer.parseInt(times[0]),
                Integer.parseInt(times[1]),Integer.parseInt(times[2]));
        switch (chronon){
            case LocalDateUtils.YEAR:
                ldStart = ldStart.plusYears(-num);
                ldStart = ldStart.with(TemporalAdjusters.lastDayOfYear());
                break;
            case LocalDateUtils.MONTH:
                ldStart = ldStart.plusMonths(-num);
                ldStart = ldStart.with(TemporalAdjusters.lastDayOfMonth());
                break;
            case LocalDateUtils.WEEK:
                ldStart = ldStart.plusWeeks(-num);
                ldStart = ldStart.with(DayOfWeek.SUNDAY);
                break;
            case LocalDateUtils.DAY:
                ldStart = ldStart.plusDays(-num);
                break;
        }
        return ldStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @return 当前日期是星期几
     */
    public static int getWeekOfDate() {
        Integer[] weekDays = {7, 1, 2, 3, 4, 5, 6};
        Calendar calendar=Calendar.getInstance();
        return weekDays[calendar.get(Calendar.DAY_OF_WEEK)-1];
    }

    /**
     * 获取本周的开始时间
     *
     * @param parrten
     * @return
     */
    public static String getBeginDayOfWeek(String parrten) {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return new SimpleDateFormat(parrten).format(cal.getTime());
    }

    /**
     * 获取前一天的时间
     *
     * @param parrten
     * @return
     */
    public static String getBeforeDay(String parrten){
        SimpleDateFormat format = new SimpleDateFormat(parrten);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);;
        String beforeDay = format.format(cal.getTime());
        return beforeDay;
    }

    /**
     * formatDate
     * @param time
     * @param oldFormat
     * @param newFormat
     * @return java.lang.String
     * @author taogang
     * @date 2019/10/31 15:45
     * @description 把一个时间格式的字符串转换成另外一个时间格式的字符串
     */
    public static String formatDate(String time,String oldFormat,String newFormat){

        LocalDate ld = LocalDate.parse(time,DateTimeFormatter.ofPattern(oldFormat));

        return ld.format(DateTimeFormatter.ofPattern(newFormat));
    }

}
