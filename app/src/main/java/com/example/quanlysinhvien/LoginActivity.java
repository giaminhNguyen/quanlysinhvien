package com.example.quanlysinhvien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanlysinhvien.DAO.DAO_Account;
import com.example.quanlysinhvien.DTO.Account;
import com.example.quanlysinhvien.Other.CustomToast;
import com.example.quanlysinhvien.SharePre.Share;

public class LoginActivity extends AppCompatActivity {
    private EditText taiKhoan,matKhau;
    private CheckBox luuThongTin;
    private DAO_Account db;
    private Share share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DAO_Account(this);
        share = new Share(this);
        anhXa();
        upCheck();

    }

    private void upCheck() {
        if(share.getCheck()){
            taiKhoan.setText(share.getTK());
            matKhau.setText(share.getMK());
            luuThongTin.setChecked(true);
        }
    }

    private void anhXa() {
        taiKhoan = findViewById(R.id.login_et_taiKhoan);
        matKhau = findViewById(R.id.login_et_matKhau);
        luuThongTin = findViewById(R.id.login_cb_luuThongTin);
    }

    public void actionLogin(View view) {
        String tk = taiKhoan.getText().toString(),mk = matKhau.getText().toString();
        if(check(tk,mk)){
            CustomToast.makeText(LoginActivity.this, "Đăng nhập thành công ", 0, true).show();
            if (luuThongTin.isChecked()) {
                share.putAccount(tk, mk);
            }
            share.putData(tk);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
    }

    private boolean check(String tk, String mk) {
        if(tk.isEmpty() || mk.isEmpty()){
            CustomToast.makeText(this,"Nội dụng không được để trống",0,false).show();
            return false;
        }
        if(db.getIndex(tk) < 0 || !db.readObj(tk).getPass_Word().equals(mk)){
            CustomToast.makeText(this,"Tài khoản hoặc mật khẩu sai",0,false).show();
            return false;
        }
        return true;
    }

    public void actionRegister(View view) {
        Intent intent = new Intent(this, DangKyActivity.class);
        startActivity(intent);
    }
}