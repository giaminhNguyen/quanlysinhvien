package com.example.quanlysinhvien.DTO;

public class Point {
    private String ma_SV,ma_mon,ma_nganh;
    private Float diem;

    public Point(String ma_SV, String ma_mon,String ma_nganh, Float diem) {
        this.ma_SV = ma_SV;
        this.ma_mon = ma_mon;
        this.diem = diem;
        this.ma_nganh = ma_nganh;
    }

    public Point() {

    }


    public String getMa_SV() {
        return ma_SV;
    }

    public void setMa_SV(String ma_SV) {
        this.ma_SV = ma_SV;
    }

    public String getMa_mon() {
        return ma_mon;
    }

    public void setMa_mon(String ma_mon) {
        this.ma_mon = ma_mon;
    }

    public Float getDiem() {
        return diem;
    }

    public void setDiem(Float diem) {
        this.diem = diem;
    }

    public String getMa_nganh() {
        return ma_nganh;
    }

    public void setMa_nganh(String ma_nganh) {
        this.ma_nganh = ma_nganh;
    }

    @Override
    public String toString() {
        return "Point{" +
                "ma_SV='" + ma_SV + '\'' +
                ", ma_mon='" + ma_mon + '\'' +
                ", ma_nganh='" + ma_nganh + '\'' +
                ", diem=" + diem +
                '}';
    }
}
