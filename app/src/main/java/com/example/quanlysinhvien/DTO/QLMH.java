package com.example.quanlysinhvien.DTO;

public class QLMH {
    private String maMon,maNganh;

    public QLMH(String maMon, String maNganh) {
        this.maMon = maMon;
        this.maNganh = maNganh;
    }

    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(String maMon) {
        this.maMon = maMon;
    }

    public String getMaNganh() {
        return maNganh;
    }

    public void setMaNganh(String maNganh) {
        this.maNganh = maNganh;
    }
}
