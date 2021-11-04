package com.hx.dao.mapper;

import com.hx.pojo.AppxModel;
import org.noear.weed.BaseMapper;
import org.noear.weed.xml.Namespace;

@Namespace("com.hx.dao.mapper")
public interface DemoMapper extends BaseMapper<AppxModel> {
    Integer init() throws Exception;

}
