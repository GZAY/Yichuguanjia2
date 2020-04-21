package com.example.yichuguanjia2.sql;

import org.litepal.crud.DataSupport;

public class imagePath2 extends DataSupport {
    private int id;
    private String path;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
