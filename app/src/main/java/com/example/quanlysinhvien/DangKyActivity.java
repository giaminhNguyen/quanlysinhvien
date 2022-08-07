package com.example.quanlysinhvien;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlysinhvien.DAO.DAO_Account;
import com.example.quanlysinhvien.Other.CustomToast;

import java.util.Calendar;

public class DangKyActivity extends AppCompatActivity {
    private EditText taiKhoan,matKhau,nhapLaiMatKhau;
    private DAO_Account db;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        db = new DAO_Account(this);
        anhXa();
    }

    private void anhXa() {
        taiKhoan = findViewById(R.id.dang_ky_et_taiKhoan);
        matKhau = findViewById(R.id.dang_ky_et_matKhau);
        nhapLaiMatKhau = findViewById(R.id.dang_ky_et_nhapLaiMatKhau);
    }

    public void actionRegister(View view) {
        String tk = taiKhoan.getText().toString(),mk = matKhau.getText().toString(),
                mk2 = nhapLaiMatKhau.getText().toString();
        if(check(tk,mk,mk2)){
            CustomToast.makeText(this,"Đăng ký thành công",0,true).show();
            db.create(tk,mk);
            xoaForm();
        }
    }

    private void xoaForm() {
        matKhau.setText("");
        nhapLaiMatKhau.setText("");
    }

    private boolean check(String tk,String mk,String mk2) {
        if(tk.isEmpty() || mk.isEmpty() || mk2.isEmpty()){
            CustomToast.makeText(this,"Thông tin không được bỏ trống",0,false).show();
            return false;
        }
        if(!mk.equals(mk2)){
            CustomToast.makeText(this,"Mật khẩu không trùng khớp",0,false);
            return false;
        }
        if(db.getIndex(tk) >= 0){
            CustomToast.makeText(this,"Tài khoản đã tồn tại",0,false).show();
            return false;
        }
        return true;
    }


    public void actionThoat(View view) {
        Intent intent = new Intent(DangKyActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
