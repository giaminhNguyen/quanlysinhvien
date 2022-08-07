package com.example.quanlysinhvien.DTO;

public class LopHoc {
    private String ma_lop,ma_nganh;

    public LopHoc(String ma_lop, String ma_nganh) {
        this.ma_lop = ma_lop;
        this.ma_nganh = ma_nganh;
    }
    public LopHoc(){}


    public String getMa_lop() {
        return ma_lop;
    }

    public void setMa_lop(String ma_lop) {
        this.ma_lop = ma_lop;
    }

    public String getMa_nganh() {
        return ma_nganh;
    }

    public void setMa_nganh(String ma_nganh) {
        this.ma_nganh = ma_nganh;
    }

    @Override
    public String toString() {
        return "ma_lop=" + ma_lop + "-/-" + "ma_nganh=" + ma_nganh + "\n";
    }
}
