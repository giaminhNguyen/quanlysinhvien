package com.example.quanlysinhvien.DTO;

public class ChuyenNganh {
    private String ma_nganh,nganh_hoc;

    public ChuyenNganh(String ma_nganh, String nganh_hoc) {
        this.ma_nganh = ma_nganh;
        this.nganh_hoc = nganh_hoc;
    }
    public ChuyenNganh(){}

    public String getMa_nganh() {
        return ma_nganh;
    }

    public void setMa_nganh(String ma_nganh) {
        this.ma_nganh = ma_nganh;
    }

    public String getNganh_hoc() {
        return nganh_hoc;
    }

    public void setNganh_hoc(String nganh_hoc) {
        this.nganh_hoc = nganh_hoc;
    }
}
