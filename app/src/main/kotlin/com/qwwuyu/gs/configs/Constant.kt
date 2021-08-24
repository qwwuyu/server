package com.qwwuyu.gs.configs

import com.qwwuyu.lib.ext.tf
import com.qwwuyu.lib.utils.CommUtil

/**
 * 常量管理
 */
object Constant {
    /* ======================== 环境常量 ======================== */
    /** 基础目录(文件管理器、日志、java文件) */
    val SYSTEM_PATH = CommUtil.isWindows().tf("D:\\qwwuyu\\", "/qwwuyu/")

    /** 日志文件 */
    val LOG_PATH = SYSTEM_PATH + CommUtil.isWindows().tf("files\\logs\\", "files/logs/")

    /** java文件 */
    val JAVA_PATH = SYSTEM_PATH + CommUtil.isWindows().tf("files\\java-run\\", "files/java-run/")

    /** java文件 */
    val TMP_PATH = SYSTEM_PATH + CommUtil.isWindows().tf("files\\tmp\\", "files/tmp/")

    /* ======================== 常量 ======================== */
    /** 登录过期时间  */
    const val expiresValue = 30L * 86400000

    /* ======================== 枚举 ======================== */
    /** 游客  */
    const val PERMIT_VISITOR = 1

    /** 普通  */
    const val PERMIT_BASIC = 2

    /** VIP  */
    const val PERMIT_VIP = 3

    /** 管理  */
    const val PERMIT_MANAGE = 4

    /** 超级管理员  */
    const val PERMIT_ADMIN = 5

    /* ======================== response code ======================== */
    /** 处理请求成功  */
    const val HTTP_SUC = 1

    /** 处理请求失败  */
    const val HTTP_ERR = -1

    /** 帐号已存在  */
    const val HTTP_ACC_EXIST = 2

    /** 昵称已存在  */
    const val HTTP_NIKE_EXIST = 2

    /** token不存在  */
    const val HTTP_AUTH_OTHER = 3

    /** 验证过期  */
    const val HTTP_AUTH_EXPIRE = 2

    /** 下载中  */
    const val HTTP_DOWNLOADING = 2

    /* ======================== key ======================== */
    const val KEY_USER = "KEY_USER"

    /* ======================== backups ======================== */
    // {"code":0,"data":{"country":"\u4e2d\u56fd","country_id":"CN","area":"\u534e\u5357","area_id":"800000","region":"\u5e7f\u4e1c\u7701","region_id":"440000","city":"\u6df1\u5733\u5e02","city_id":"440300","county":"\u5b9d\u5b89\u533a","county_id":"440306","isp":"\u7535\u4fe1","isp_id":"100017","ip":"119.137.52.75"}}
    private const val url_find_ip = "http://ip.taobao.com/service/getIpInfo.php?ip=119.137.52.75"

    // {"ret":1,"start":-1,"end":-1,"country":"\u7f8e\u56fd","province":"\u52a0\u5229\u798f\u5c3c\u4e9a","city":"","district":"","isp":"","type":"","desc":""}
    private const val url_find_ip2 = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=119.137.52.75"
}