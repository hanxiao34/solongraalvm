package com.hx;


import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.core.ExtendLoader;
import org.noear.solon.core.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;


public class App{
    private final static Logger LOG = LoggerFactory.getLogger(App.class);
    public static void main(String[] args){
        Solon.start(App.class, args);
//        String root = "classes/META-INF";
//
//        System.out.println("Walking path: " + Paths.get(root));
//
//        long[] size = {0};
//        long[] count = {0};
//
//        try (Stream<Path> paths = Files.walk(Paths.get(root))) {
//            paths.filter(Files::isRegularFile).forEach((Path p) -> {
//                System.out.println(p.toString());
//                File f = p.toFile();
//                size[0] += f.length();
//                count[0] += 1;
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String [] filepath=new String[]{"META-INF/solon/solon.boot.jlhttp.properties"};
//        List<ClassLoader> loaderList = ExtendLoader.load(Solon.cfg().extend(), false);
//        for(ClassLoader loaer :loaderList) {
//            for (String file:filepath) {
//                URL url = Utils.getResource(loaer, file);
//                Props p = new Props(Utils.loadProperties(url));
//                System.out.println("=============="+file);
//                System.out.println(p.keySet().size());
//
//
//
//            }
//        }
    }
}

