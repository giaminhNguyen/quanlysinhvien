package com.example.quanlysinhvien.DTO;

public class MonHoc {
    private String ma_mon,ten_mon;
    private int tinChi;

    public MonHoc(String ma_mon, String ten_mon,int tinChi) {
        this.ma_mon = ma_mon;
        this.ten_mon = ten_mon;
        this.tinChi = tinChi;
    }

    public MonHoc(){}

    public String getMa_mon() {
        return ma_mon;
    }

    public void setMa_mon(String ma_mon) {
        this.ma_mon = ma_mon;
    }

    public String getTen_mon() {
        return ten_mon;
    }

    public void setTen_mon(String ten_mon) {
        this.ten_mon = ten_mon;
    }

    public int getTinChi() {
        return tinChi;
    }

    public void setTinChi(int tinChi) {
        this.tinChi = tinChi;
    }
}
