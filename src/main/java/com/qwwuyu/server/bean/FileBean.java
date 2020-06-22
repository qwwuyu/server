package com.qwwuyu.server.bean;

public class FileBean {
    public String name;
    public boolean dir;
    public String size;

    public FileBean() {
    }

    public FileBean(String name, boolean dir, String size) {
        this.name = name;
        this.dir = dir;
        this.size = size;
    }
}
