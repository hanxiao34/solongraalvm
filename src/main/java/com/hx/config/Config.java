package com.hx.config;

import com.hx.proxy.ProxyServer;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.core.Aop;
import org.noear.weed.DbContext;
import org.noear.weed.DbDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 静态模式演示
 * */
@Configuration
public class Config {
    private final static Logger LOG = LoggerFactory.getLogger(Config.class);


    //
    //缓存服务配置:: //新建个缓存服务，并通过nameSet 注册到 全局 libOfCache
    //
//    public static ICacheServiceEx cache1 = new LocalCache("test",60).nameSet("cache1");


    //
    //使用连接池 配置 数据库上下文
    //
    private final static DbContext build() {
        try {
            InputSource i = null;
            LOG.info("init db......");
            Properties prop = Solon.cfg().getProp("dbconfig.db1");
            String schema = prop.getProperty("schema");
            LOG.info("db config:{}", prop);

            //利于一个solon的内部工具
            //
//        HikariDataSource hikariDataSource = new HikariDataSource();
            DbDataSource source = new DbDataSource(prop.getProperty("jdbcUrl"), prop.getProperty("username"), prop.getProperty("password"));
            DataSource dataSource = Aop.inject(source, prop);
            DbContext dbContext = new DbContext(schema, dataSource);
            init(dbContext);
            return dbContext;
        } catch (Exception e) {
            LOG.error("db conn init fail", e);
        }
        return null;
    }

    private static void init(DbContext dbContext) {
        try {
            dbContext.exe("CREATE TABLE if not EXISTS `appx` (\n" +
                    "        `app_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '应用ID',\n" +
                    "        `app_key` varchar(40) DEFAULT NULL COMMENT '应用访问KEY',\n" +
                    "        `akey` varchar(40) DEFAULT NULL COMMENT '（用于取代app id 形成的唯一key）',\n" +
                    "        `ugroup_id` int(11) DEFAULT '0' COMMENT '加入的用户组ID',\n" +
                    "        `agroup_id` int(11) DEFAULT NULL COMMENT '加入的应用组ID',\n" +
                    "        `name` varchar(50) DEFAULT NULL COMMENT '应用名称',\n" +
                    "        `note` varchar(50) DEFAULT NULL COMMENT '应用备注',\n" +
                    "        `ar_is_setting` int(1) NOT NULL DEFAULT '0' COMMENT '是否开放设置',\n" +
                    "        `ar_is_examine` int(1) NOT NULL DEFAULT '0' COMMENT '是否审核中(0: 没审核 ；1：审核中)',\n" +
                    "        `ar_examine_ver` int(11) NOT NULL DEFAULT '0' COMMENT '审核 中的版本号',\n" +
                    "        `log_fulltime` datetime DEFAULT NULL,\n" +
                    "        PRIMARY KEY (`app_id`)\n" +
                    "        )");
            LOG.info("init table.....");
        } catch (Exception e) {
            LOG.error("init table error", e);
        }
    }

    public static String proxyserver="";
    public static int proxyport=80;
    private static void startProxy(){
        new Thread(()->{
            proxyserver=Solon.cfg().getProperty("proxy.server","127.0.0.1");

            proxyport=Solon.cfg().getInt("proxy.port",Solon.cfg().getInt("server.port",8080));
            System.out.println("start proxy server: "+proxyserver+" "+proxyport);
//            ProxyBootstrap.startServer(9999);
//            Proxy2.startProxy(proxyserver,proxyport);
            try {
                ProxyServer.main(new String[]{""});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }
    /**
     * 使用静态模式
     */
    public final static DbContext db1 = build();
     static {
         startProxy();
     }
    //初始化一些反射类，便于grallvm探查
//    static {
//        for (String c : new String[]{
//                "org.h2.store.fs.FilePathDisk",
//                "org.h2.store.fs.FilePathMem",
//                "org.h2.store.fs.FilePathMemLZF",
//                "org.h2.store.fs.FilePathNioMem",
//                "org.h2.store.fs.FilePathNioMemLZF",
//                "org.h2.store.fs.FilePathSplit",
//                "org.h2.store.fs.FilePathNio",
//                "org.h2.store.fs.FilePathNioMapped",
//                "org.h2.store.fs.FilePathAsync",
//                "org.h2.store.fs.FilePathZip",
//                "org.h2.store.fs.FilePathRetryOnInterrupt"
//        }) {
//            try {
////                System.out.println("test====================== " +c);
//                FilePath p = (FilePath) Class.forName(c).getDeclaredConstructor().newInstance();
////                FilePath f = p.getPath("/root/data/hx1");
////                FilePath parent = f.getParent();
////                if (parent != null && !parent.exists()) {
////                   System.out.println("Directory does not exist:"+parent);
////                }
//            } catch (Exception e) {
//
//            }
//        }
//    }
}
