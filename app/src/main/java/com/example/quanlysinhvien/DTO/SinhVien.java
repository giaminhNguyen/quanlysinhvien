package com.example.quanlysinhvien.DTO;

public class SinhVien {
    private String ma_SV,ten_SV,ma_lop,ma_nganh,SDT,gioi_tinh,ngay_sinh;

    public SinhVien(String ma_SV,String ten_SV, String ma_lop,String ma_nganh, String SDT, String gioi_tinh, String ngay_sinh) {
        this.ma_SV = ma_SV;
        this.ten_SV = ten_SV;
        this.ma_lop = ma_lop;
        this.ma_nganh = ma_nganh;
        this.SDT = SDT;
        this.gioi_tinh = gioi_tinh;
        this.ngay_sinh = ngay_sinh;
    }
    public SinhVien(){}

    public String getMa_SV() {
        return ma_SV;
    }

    public void setMa_SV(String ma_SV) {
        this.ma_SV = ma_SV;
    }

    public String getMa_lop() {
        return ma_lop;
    }

    public void setMa_lop(String ma_lop) {
        this.ma_lop = ma_lop;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getGioi_tinh() {
        return gioi_tinh;
    }

    public void setGioi_tinh(String gioi_tinh) {
        this.gioi_tinh = gioi_tinh;
    }


    public String getNgay_sinh() {
        return ngay_sinh;
    }

    public void setNgay_sinh(String ngay_sinh) {
        this.ngay_sinh = ngay_sinh;
    }

    public String getTen_SV() {
        return ten_SV;
    }

    public void setTen_SV(String ten_SV) {
        this.ten_SV = ten_SV;
    }

    public String getMa_nganh() {
        return ma_nganh;
    }

    public void setMa_nganh(String ma_nganh) {
        this.ma_nganh = ma_nganh;
    }

    @Override
    public String toString() {
        return "ma_SV='" + ma_SV +
                ", ten_SV='" + ten_SV +
                ", ma_lop='" + ma_lop +
                "\n, ma_nganh='" + ma_nganh  +
                ", SDT='" + SDT +
                ", gioi_tinh='" + gioi_tinh +
                ", ngay_sinh='" + ngay_sinh;
    }
}
