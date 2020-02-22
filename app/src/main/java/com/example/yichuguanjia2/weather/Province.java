package com.example.yichuguanjia2.weather;

import org.litepal.crud.DataSupport;

/**
 * Created by jjy on 2017/2/15.
 */

public class Province extends DataSupport {
    private int id;
    private String provinceName;
    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public Province(int id, String provinceName) {
        this.id = id;
        this.provinceName = provinceName;
    }

    Province() {
    }

    public Province(int id, String provinceName, int provinceCode) {
        this.id = id;
        this.provinceName = provinceName;
        this.provinceCode = provinceCode;
    }

    @Override
    public String toString() {
        return provinceName;
    }
}
