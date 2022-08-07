package com.example.quanlysinhvien.Fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.example.quanlysinhvien.DAO.DAO_MonHoc;
import com.example.quanlysinhvien.DAO.DAO_Point;
import com.example.quanlysinhvien.DTO.MonHoc;
import com.example.quanlysinhvien.DTO.Point;
import com.example.quanlysinhvien.MainActivity;
import com.example.quanlysinhvien.Other.Action;
import com.example.quanlysinhvien.Other.CustomToast;
import com.example.quanlysinhvien.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Fragment_KetQuaHocTap extends Fragment implements View.OnClickListener{
    private TextView GPA,tenMonHoc;
    private EditText diem;
    private Float tong,diemMon;
    private Button troVe;
    private LinearLayout linearLayout;
    private FloatingActionButton floatAction;
    private ArrayList<Point> list;
    private ArrayList<MonHoc> list_monHoc;
    private String ma_SV;
    private int tongTinChi,tinChi;
    private DAO_Point db;
    private DAO_MonHoc db_monHoc;
    private boolean check;
    private Action action;
    private ArrayList<EditText> list_et;
    private int color;

    public static Fragment_KetQuaHocTap newInstance(String ma_SV) {
        Fragment_KetQuaHocTap fragment = new Fragment_KetQuaHocTap(ma_SV);
        return fragment;
    }

    public Fragment_KetQuaHocTap(String ma_SV){
        this.ma_SV = ma_SV;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ket_qua_hoc_tap,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DAO_Point(getActivity());
        db_monHoc = new DAO_MonHoc(getActivity());
        list_et = new ArrayList<>();
        list = db.readWithMaSV(ma_SV);
        list_monHoc = new ArrayList<>();
        troVe = view.findViewById(R.id.frag_ket_qua_hoc_tap_btn_troVe);
        GPA = view.findViewById(R.id.frag_ket_qua_hoc_tap_GPA);
        linearLayout = view.findViewById(R.id.frag_ket_qua_hoc_tap_linearLayout);
        floatAction = view.findViewById(R.id.frag_ket_qua_hoc_tap_floatAction);
        action = new MainActivity();
        check = false;
        addMonHoc();
        troVe.setOnClickListener(this);
        floatAction.setOnClickListener(this);
    }

    private void changeEnbleView(boolean b) {
        if(b)
            color = R.color.black;
        else
            color = R.color.white;
        for(EditText x : list_et){
            x.setEnabled(b);
            DrawableCompat.setTint(x.getBackground(), ContextCompat.getColor(getActivity(), color));
        }
    }

    private void addMonHoc() {
        tong = 0f;
        tongTinChi = 0;
        tinChi = 0;
        for(Point x : list){
            list_monHoc.add(db_monHoc.readObj(x.getMa_mon()));
            View view1 = LayoutInflater.from(linearLayout.getContext()).inflate(R.layout.item_diem_mon_hoc,null);
            tenMonHoc = view1.findViewById(R.id.item_diem_mon_tv_tenMonHoc);
            diem = view1.findViewById(R.id.item_diem_mon_et_diem);
            tenMonHoc.setText(x.getMa_mon());
            diem.setText(String.valueOf(x.getDiem()));
            tinChi = list_monHoc.get(list_monHoc.size() - 1).getTinChi();
            tongTinChi += tinChi;
            tong += x.getDiem() * tinChi;
            linearLayout.addView(view1);
            diem.setEnabled(false);
            list_et.add(diem);
        }
        setGPA(tong,tongTinChi);
    }

    private void setGPA(float tong, int tongTinChi){
        GPA.setText(String.valueOf(Math.round((tong/(tongTinChi * 10)) * 400 )* 1.0/100));
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.frag_ket_qua_hoc_tap_floatAction:
                actionFloat();
                break;
            case R.id.frag_ket_qua_hoc_tap_btn_troVe:
                actionTroVe();
        }
    }

    private void actionTroVe() {
        changeEnbleView(false);
        action.chuyen_Fragment(getActivity().getSupportFragmentManager(),new Fragment_Lop(1));
    }

    private void actionFloat() {
        if(check){
            if(actionValidate()){
                check = false;
                setGPA(tong,tongTinChi);
                upDiem();
                changeEnbleView(false);
                floatAction.setImageResource(R.drawable.fix_30);
                CustomToast.makeText(getActivity(),"Lưu thành công",0,true).show();
            }else{
                check = true;
                CustomToast.makeText(getActivity(),"Dữ liệu sai",0,true).show();
            }
            return;
        }
        check = true;
        changeEnbleView(true);
        floatAction.setImageResource(R.drawable.save_file_32);
    }

    private void upDiem() {
        diemMon = 0f;
        for(int i = 0; i < list.size(); i++){
            diemMon = Float.parseFloat(list_et.get(i).getText().toString());
            db.updateDiemMonWithMaSV(ma_SV,list.get(i).getMa_mon(),diemMon);
        }
    }

    private boolean actionValidate() {
        tong = 0f;
        diemMon = 0f;
        String regex = "(^\\d(\\.\\d+)?$)|(^10(.0+)?$)";
        for(int i = 0; i < list_et.size(); i ++){
            EditText x = list_et.get(i);
            x.setBackground(new ColorDrawable(Color.WHITE));
            if(x.getText().toString().isEmpty())
                x.setText("0.0");
            else if(!x.getText().toString().matches(regex)){
                x.setBackground(new ColorDrawable(Color.RED));
                check = false;
            }else{
                diemMon = Float.parseFloat(x.getText().toString());
                tong += diemMon * list_monHoc.get(i).getTinChi();
                x.setText(String.valueOf(Math.round(diemMon * 100 )* 1.0/100));
            }
        }
        return check;
    }
}
