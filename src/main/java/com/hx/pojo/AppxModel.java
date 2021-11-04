package com.hx.pojo;

import org.noear.weed.DataItem;

public class AppxModel  extends DataItem {
    public int agroup_id;
    public String note;
    public String app_key;
    public int app_id;
    public int ar_is_examine;
    public String name;

    @Override
    public String toString() {
        return "AppxModel{" +
                "agroup_id=" + agroup_id +
                ", note='" + note + '\'' +
                ", app_key='" + app_key + '\'' +
                ", app_id=" + app_id +
                ", ar_is_examine=" + ar_is_examine +
                ", name='" + name + '\'' +
                '}';
    }

    public int getAgroup_id() {
        return agroup_id;
    }

    public void setAgroup_id(int agroup_id) {
        this.agroup_id = agroup_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public int getApp_id() {
        return app_id;
    }

    public void setApp_id(int app_id) {
        this.app_id = app_id;
    }

    public int getAr_is_examine() {
        return ar_is_examine;
    }

    public void setAr_is_examine(int ar_is_examine) {
        this.ar_is_examine = ar_is_examine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
