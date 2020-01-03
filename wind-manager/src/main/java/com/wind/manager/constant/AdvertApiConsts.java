package com.wind.manager.constant;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 搜索推广字典常量
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/18 15:24
 **/
public class AdvertApiConsts {

    private AdvertApiConsts() {

    }

    /**
     * http请求头application类型
     */
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";

    /**
     * 返回数据类型
     */
    public static final String CONTENT_TYPE_JSON = "application/json";

    /**
     * 编码
     */
    public static final String ENCODING_FORMAT = "utf-8";

    /**
     * 返回数据成功状态
     */
    public static final Integer API_HTTP_SUCCESS_STATUS = 0;

    /**
     * 等待报告生成时间
     */
    public static final Integer FILE_WAIT_TIME = 1000;

    /**
     * 百度域名
     */
    public static final String API_BAIDU_DNS = "https://api.baidu.com";
    /**
     * 百度搜索推广-数据报表
     */
    public static final String API_REPORT_BAIDU = API_BAIDU_DNS + "/json/sms/v4/ReportService/getRealTimeData";
    /**
     * 360域名
     */
    public static final String API_360_DNS = "https://api.e.360.cn";

    /**
     * 360-登录请求路径
     */
    public static final String API_LOGIN_360 = API_360_DNS + "/account/clientLogin";
    /**
     * 360-推广组列表数据
     */
    public static final String API_REPORT_KEYWORD_360 = API_360_DNS + "/dianjing/report/group";
    /**
     * 360-推广组列表总数
     */
    public static final String API_REPORT_TOTAL_360 = API_360_DNS + "/dianjing/report/groupCount";
    /**
     * 神马域名
     */
    public static final String API_SHENMA_DNS = "https://e.sm.cn";
    /**
     * 神马-获取报告
     */
    public static final String API_SHENMA_GET_REPORT = API_SHENMA_DNS + "/api/report/getReport";
    /**
     * 神马-查看报告的状态
     */
    public static final String API_SHENMA_GET_TASKSTATE = API_SHENMA_DNS + "/api/task/getTaskState";
    /**
     * 神马-下载报告
     */
    public static final String API_SHENMA_DOWNLOAD = API_SHENMA_DNS + "/api/file/download";
    /**
     * 搜狗域名
     */
    public static final String API_SOUGO_DNS = "https://xuriapi.p4p.sogou.com";
    /**
     * 搜狗-获取报告生成id
     */
    public static final String API_SOUGO_REPORT_GETREPORTID = AdvertApiConsts.API_SOUGO_DNS + "/api/v2/report/getReportId";
    /**
     * 搜狗-查看生成报告状态
     */
    public static final String API_SOUGO_REPORT_GETREPORTSTATE = AdvertApiConsts.API_SOUGO_DNS + "/api/v2/report/getReportState";
    /**
     * 搜狗-获取报告下载地址
     */
    public static final String API_SOUGO_REPORT_GETREPORTPATH = AdvertApiConsts.API_SOUGO_DNS + "/api/v2/report/getReportPath";

    /**
     * 神马和搜狗共用-报告返回字段
     */
    public static final List SHENMA_AND_SOUGO_PERFORMANCE_DATA = Lists.newArrayList("impression", "click", "cost", "cpc", "ctr");
    /**
     * 百度-数据报告返回字段
     */
    public static final List BAIDU_PERFORMANCE_DATA = Lists.newArrayList("impression", "click", "cost", "cpc", "cpm", "ctr");

    /**
     * 神马-状态
     */
    public static final String PARAM_STATUS_NAME = "status";

    public static final String PARAM_DATA_NAME = "data";
    /**
     * 神马-报告创建时间
     */
    public static final String SHENMA_PARAM_CREATETIME = "createTime";
    /**
     * 神马-报告生成时间
     */
    public static final String SHENMA_RESULT_STATUS_FINISHED = "FINISHED";
    /**
     * 神马-生成报告的任务id
     */
    public static final String SHENMA_RESULT_COLUMN_TASKID = "taskId";
    /**
     * 神马参数名称-filed
     */
    public static final String SHENMA_RESULT_COLUMN_FILEID = "fileId";

    /**
     * 是否已完成报告生成
     */
    public static final String SOUGO_PARAM_IS_GENERATED = "isGenerated";
    /**
     * 搜狗-报告id
     */
    public static final String SOUGO_PARAM_REPORTID = "reportId";
    /**
     * 搜狗-报告下载路径
     */
    public static final String SOUGO_PARAM_REPORTPATH = "reportPath";


    /**
     * 开始日期的时分秒
     */
    public static final String START_DATE_HSM = " 00:00:00";
    /**
     * 结束日期的时分秒
     */
    public static final String END_DATE_HSM = " 23:59:59";

    /**
     * 账号有效
     */
    public static final Integer ACCOUNT_ACTIVE_YES = 1;
}
