package com.qwwuyu.server.configs;

public class FieldConfig {
	// {"code":0,"data":{"country":"\u4e2d\u56fd","country_id":"CN","area":"\u534e\u5357","area_id":"800000","region":"\u5e7f\u4e1c\u7701","region_id":"440000","city":"\u6df1\u5733\u5e02","city_id":"440300","county":"\u5b9d\u5b89\u533a","county_id":"440306","isp":"\u7535\u4fe1","isp_id":"100017","ip":"119.137.52.75"}}
	public static String url_find_ip = "http://ip.taobao.com/service/getIpInfo.php?ip=119.137.52.75";
	// {"ret":1,"start":-1,"end":-1,"country":"\u7f8e\u56fd","province":"\u52a0\u5229\u798f\u5c3c\u4e9a","city":"","district":"","isp":"","type":"","desc":""}
	public static String url_find_ip2 = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=119.137.52.75";
}
