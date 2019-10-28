package com.qwwuyu.server.configs;

/**
 * 常量管理
 */
public class Constant {


    /* ======================== 常量 ======================== */
    /** 登录过期时间 */
    public static final long expiresValue = 30L * 86400000;
    /** 匹配前缀 */
    public static final String PREFIX = "/html/";

    /* ======================== 枚举 ======================== */
    /** 游客 */
    public static final int PERMIT_VISITOR = 1;
    /** 普通 */
    public static final int PERMIT_BASIC = 2;
    /** VIP */
    public static final int PERMIT_VIP = 3;
    /** 管理 */
    public static final int PERMIT_MANAGE = 4;
    /** 管理 */
    public static final int PERMIT_ADMIN = 5;
    /* ======================== response code ======================== */
    /** 处理请求成功 */
    public static final int HTTP_SUC = 1;
    /** 处理请求失败 */
    public static final int HTTP_ERR = -1;

    /** 帐号已存在 */
    public static final int HTTP_ACC_EXIST = 2;
    /** 昵称已存在 */
    public static final int HTTP_NIKE_EXIST = 2;
    /** token不存在 */
    public static final int HTTP_AUTH_OTHER = 3;
    /** 验证过期 */
    public static final int HTTP_AUTH_EXPIRE = 2;

    /* ======================== key ======================== */
    public static final String KEY_USER = "KEY_USER";


    // {"code":0,"data":{"country":"\u4e2d\u56fd","country_id":"CN","area":"\u534e\u5357","area_id":"800000","region":"\u5e7f\u4e1c\u7701","region_id":"440000","city":"\u6df1\u5733\u5e02","city_id":"440300","county":"\u5b9d\u5b89\u533a","county_id":"440306","isp":"\u7535\u4fe1","isp_id":"100017","ip":"119.137.52.75"}}
    private static String url_find_ip = "http://ip.taobao.com/service/getIpInfo.php?ip=119.137.52.75";
    // {"ret":1,"start":-1,"end":-1,"country":"\u7f8e\u56fd","province":"\u52a0\u5229\u798f\u5c3c\u4e9a","city":"","district":"","isp":"","type":"","desc":""}
    private static String url_find_ip2 = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=119.137.52.75";
}
