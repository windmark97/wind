package com.wind.manager.constant;

/**
 * 定时任务常量
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/19 18:31
 **/
public class TaskConsts {

    private TaskConsts() {

    }

    /**
     * 百度搜索推广数据job的id
     */
    public static final String TASK_JOB_ADVERT_API_BAIDU = "task_job_advert_api_baidu";
    /**
     * 百度搜索推广数据job时间
     */
    public static final String TASK_JOB_ADVERT_API_BAIDU_CRON = "0 0 4 * * ? ";

    /**
     * 360搜索推广job
     */
    public static final String TASK_JOB_ADVERT_API_360 = "task_job_advert_api_360";

    /**
     * 360搜索推广job时间 360数据八点半之后才生成报告
     */
    public static final String TASK_JOB_ADVERT_API_360_CRON = "0 30 8 * * ? ";
    /**
     * 神马搜索推广job
     */
    public static final String TASK_JOB_ADVERT_API_SHENMA = "task_job_advert_api_shenma";
    /**
     * 神马搜索推广job时间
     */
    public static final String TASK_JOB_ADVERT_API_SHENMA_CRON = "0 0 5 * * ? ";

    /**
     * 搜狗搜索推广job
     */
    public static final String TASK_JOB_ADVERT_API_SOUGO = "task_job_advert_api_sougo";
    /**
     * 搜狗搜索推广job时间
     */
    public static final String TASK_JOB_ADVERT_API_SOUGO_CRON = "0 0 10 * * ? ";
}
