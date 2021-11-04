package com.hx;


import org.noear.solon.Solon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class App{
    private final static Logger LOG = LoggerFactory.getLogger(App.class);
    public static void main(String[] args){
        Solon.start(App.class, args);

    }
}

