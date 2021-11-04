package com.hx.dao.mapper;

import com.hx.pojo.AppxModel;
import org.noear.weed.annotation.Sql;


import java.util.List;
import java.util.Map;

//
// 注解 sql
//
public interface SqlAnnotation {
    @Sql("select app_id from appx limit 1")
    int appx_get() throws Exception;



    //添加了缓存处理
    @Sql(value = "select * from appx where app_id = @{app_id} limit 1", caching = "test", cacheTag = "app_${app_id}")
    AppxModel appx_get2(int app_id) throws Exception;

    @Sql(value = "select * from ${tb} where app_id = @{app_id} limit 1" , cacheClear = "test")
    Map<String,Object> appx_get3(String tb, int app_id) throws Exception;

    @Sql("select * from appx where app_id>@{app_id} order by app_id asc limit 4")
    List<AppxModel> appx_getlist(int app_id) throws Exception;

    @Sql("select app_id from appx limit 4")
    List<Integer> appx_getids() throws Exception;


   @Sql("CREATE TABLE if not EXISTS `appx` (\n" +
           "  `app_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '应用ID',\n" +
           "  `app_key` varchar(40) DEFAULT NULL COMMENT '应用访问KEY',\n" +
           "  `akey` varchar(40) DEFAULT NULL COMMENT '（用于取代app id 形成的唯一key）',\n" +
           "  `ugroup_id` int(11) DEFAULT '0' COMMENT '加入的用户组ID',\n" +
           "  `agroup_id` int(11) DEFAULT NULL COMMENT '加入的应用组ID',\n" +
           "  `name` varchar(50) DEFAULT NULL COMMENT '应用名称',\n" +
           "  `note` varchar(50) DEFAULT NULL COMMENT '应用备注',\n" +
           "  `ar_is_setting` int(1) NOT NULL DEFAULT '0' COMMENT '是否开放设置',\n" +
           "  `ar_is_examine` int(1) NOT NULL DEFAULT '0' COMMENT '是否审核中(0: 没审核 ；1：审核中)',\n" +
           "  `ar_examine_ver` int(11) NOT NULL DEFAULT '0' COMMENT '审核 中的版本号',\n" +
           "  `log_fulltime` datetime DEFAULT NULL,\n" +
           "  PRIMARY KEY (`app_id`)\n" +
           ")")
    int initTable() throws Exception;
}
