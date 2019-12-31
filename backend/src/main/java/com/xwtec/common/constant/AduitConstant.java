package com.xwtec.common.constant;

/**
 * @ClassName: AduitConstant
 * @Auther: Jerry
 * @Date: 2019/6/27 15:43
 * @Desctiption: TODO
 * @Version: 1.0
 */
public class AduitConstant {

    /**
     * 审核员
     */
    public static String AUDITOR = "6e4c56654669754c6021cdca45d1ca5b";

    /**
     * 管理员
     */
    public static String ADMIN = "db2ea2210717d3c12a9c0fa209540b0f";

    /**
     * 审核状态
     */
    public interface AduitStatus {
        /**
         * 草稿
         */
        String ADUIT_INIT = "0";
        /**
         * 待审核
         */
        String ADUIT_FIRST_TRIAL = "1";
        /**
         * 通过
         */
        String ADUIT_PASS = "2";
        /**
         * 驳回
         */
        String ADUIT_REJECT = "3";
        /**
         * 发布
         */
        String ADUIT_PUBLISH = "4";
        /**
         * 过期
         */
        String ADUIT_EXPIRED = "5";
    }

    /**
     * 审核对象
     */
    public interface AduitObjectType{
        /**
         * 试题
         */
        String ADUIT_OBJECTTYPE_SUBJECT = "0";
        /**
         * 试卷
         */
        String ADUIT_OBJECTTYPE_PAPER = "1";
        /**
         * 考试
         */
        String ADUIT_OBJECTTYPE_EXAM = "2";
        /**
         * 任务
         */
        String ADUIT_OBJECTTYPE_TASK = "3";
        /**
         * 学习
         */
        String ADUIT_OBJECTTYPE_LEARN = "4";
        /**
         * banner
         */
        String ADUIT_OBJECTTYPE_BANNER = "5";
    }
}
