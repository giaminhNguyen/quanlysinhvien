package com.example.quanlysinhvien;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.quanlysinhvien.DAO.DAO_Account;
import com.example.quanlysinhvien.DTO.Account;
import com.example.quanlysinhvien.Fragment.Fragment_DaoTao;
import com.example.quanlysinhvien.Fragment.Fragment_Welcome;
import com.example.quanlysinhvien.Other.Action;
import com.example.quanlysinhvien.Other.CustomToast;
import com.example.quanlysinhvien.SharePre.Share;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements Action {
    private EditText matKhauMoi,matKhauCu,nhapLaiMatKhauMoi;
    private Toolbar toolbar;
    private Button sua;
    private ImageButton huy;
    private NavigationView naviD;
    private DrawerLayout drawerLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private DAO_Account db_account;
    private Share share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhXa();
        other();
        init();
    }

    private void other() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        db_account = new DAO_Account(this);
        share = new Share(this);
    }

    private void init(){
        addToolBar();
        addNaviD();
        addFragment(new Fragment_Welcome());
    }

    private void addFragment(Fragment fragment){
        fragmentTransaction.replace(R.id.main_linearLayout,fragment);
        fragmentTransaction.commit();
    }

    private void anhXa() {
        toolbar = findViewById(R.id.main_toolBar);
        naviD = findViewById(R.id.main_naviDraw);
        drawerLayout = findViewById(R.id.mDrawerLayout);
    }
    private void addNaviD() {
        naviD.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navid_home:
                    fragmentTransaction = fragmentManager.beginTransaction();
                    addFragment(new Fragment_Welcome());
                    break;
                case R.id.navid_changePass:
                    actionChangePass();
                    break;
                case R.id.navid_logOut:
                    actionLogOut();
            }
            drawerLayout.closeDrawer(naviD);
            return true;
        });
    }

    private void actionChangePass() {
        Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_change_password);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        //ánh xạ
        matKhauCu = dialog.findViewById(R.id.dialog_change_password_et_matKhauCu);
        matKhauMoi = dialog.findViewById(R.id.dialog_change_password_et_matKhauMoi);
        nhapLaiMatKhauMoi = dialog.findViewById(R.id.dialog_change_password_et_NhapLaiMatKhauMoi);
        sua = dialog.findViewById(R.id.dialog_change_password_btn_sua);
        huy = dialog.findViewById(R.id.dialog_change_password_btnImg_huy);
        //
        huy.setOnClickListener(view -> dialog.cancel());
        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mkCu = matKhauCu.getText().toString(),mkMoi = matKhauMoi.getText().toString(),nhapMkMoi = nhapLaiMatKhauMoi.getText().toString();
                if(check(mkCu,mkMoi,nhapMkMoi)){
                    Account acc = db_account.readObj(share.getUSER_NAME());
                    db_account.update(acc.getUser_Name(),acc.getUser_Name(),mkMoi);
                    CustomToast.makeText(MainActivity.this,"Đổi thành công",0,true).show();
                    if(share.getTK().equals(acc.getUser_Name())){
                        share.putAccount(acc.getUser_Name(),mkMoi);
                    }
                    dialog.cancel();
                }
            }

            private boolean check(String mkCu, String mkMoi, String nhapMkMoi) {
                if(mkCu.isEmpty() || mkMoi.isEmpty() || nhapMkMoi.isEmpty()){
                    CustomToast.makeText(MainActivity.this,"Dữ liệu không được thiếu",0,false).show();
                    return false;
                }
                if(!db_account.readObj(share.getUSER_NAME()).getPass_Word().equals(mkCu)){
                    CustomToast.makeText(MainActivity.this,"Mật khẩu không đúng",0,false).show();
                    return false;
                }
                if(!mkMoi.equals(nhapMkMoi)){
                    CustomToast.makeText(MainActivity.this,"Mật khẩu không trùng nhau",0,false).show();
                    return false;
                }
                return true;
            }
        });
        dialog.show();
    }

    private void actionLogOut() {
        this.finish();
        System.exit(0);
    }

    private void addToolBar() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void chuyen_Fragment(FragmentManager manager,Fragment fragment) {
        fragmentTransaction = manager.beginTransaction();
        addFragment(fragment);
    }
}