package com.hx.controller;

import com.hx.config.Config;
import com.hx.dao.mapper.SqlAnnotation;
import com.hx.dto.AppxDTO;
import com.hx.pojo.AppxModel;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.weed.DbContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/*
 * mvc控制器
 */
@Controller
@Mapping("/test")
public class DemoController{
   private static Logger LOG = LoggerFactory.getLogger(DemoController.class);

    DbContext db1= Config.db1;
    SqlAnnotation  mapper=db1.mapper(SqlAnnotation.class);



    //for http
    @Mapping("/hallo/{u_u}")
    public Map hallo(String u_u){
        Map m=new HashMap();
        LOG.info("http request rec:hallo/{}",u_u);
        try {
            int i = mapper.appx_get();
            m.put("i",i);
            LOG.info("appx_get :{}",i);
        } catch (Exception e) {
            e.printStackTrace();
        }


        m.put("uu",u_u);
        return m;
    }

    @Mapping("/add/{appname}")
    public Map add(String appname){
        Map m=new HashMap();
        LOG.info("http request rec:add/{}",appname);
        try {
            long i = db1.table("appx").set("app_key","123").set("name",appname).insert();
            m.put("i",i);
            LOG.info("appx_add :{}",i);
        } catch (Exception e) {
            e.printStackTrace();
        }

        m.put("appname",appname);
        return m;
    }

    @Mapping("/get/{appname}")
    public Map get(String appname){
        Map m=new HashMap();
        LOG.info("http request rec:get/{}",appname);

        try {
            AppxModel i = db1.table("appx").whereEq("name",appname).limit(1).selectItem("*",AppxModel.class);
            m.put("app",new AppxDTO(i));
            LOG.info("appx_get :{}",i);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return m;
    }
    @Mapping("/addapp")
    public Map addapp(AppxModel a){
        Map m=new HashMap();
        LOG.info("http request rec:add/ {}",a);

        try {
            long i = db1.table("appx").insert(a);
            m.put("id",i);
            LOG.info("appx_add :{}",i);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return m;
    }

    /*
    //for web socket （需添加：solon.boot.websocket 插件）
    @Mapping(value="/hallo/{u_u}", method = MethodType.WEBSOCKET)
    public ModelAndView hallo_ws(String u_u){
        return new ModelAndView("hallo");
    }
    */
}