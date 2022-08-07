package com.example.quanlysinhvien.Fragment;

import android.app.DatePickerDialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlysinhvien.Adapter.Adapter_SinhVien;
import com.example.quanlysinhvien.Adapter.Adapter_Spinner;
import com.example.quanlysinhvien.DAO.DAO_LopHoc;
import com.example.quanlysinhvien.DAO.DAO_Point;
import com.example.quanlysinhvien.DAO.DAO_SinhVien;
import com.example.quanlysinhvien.DTO.SinhVien;
import com.example.quanlysinhvien.MainActivity;
import com.example.quanlysinhvien.Other.Action;
import com.example.quanlysinhvien.Other.CustomToast;
import com.example.quanlysinhvien.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class Fragment_SinhVien extends Fragment implements View.OnClickListener, Adapter_SinhVien.OnActionSuaXoa{
    private RecyclerView rv;
    private Adapter_SinhVien adapter_sinhVien;
    private DAO_SinhVien db;
    private DAO_LopHoc db_lopHoc;
    private DAO_Point db_point;
    private ArrayList<SinhVien> list,list_tam;
    private FloatingActionButton floatAdd,floatAll,floatSearch;
    private Action action;
    private FragmentManager manager;
    private ImageButton thoat;
    private Button sua_luu_1,timKiem;
    private EditText ma,ten,ngaySinh,SDT,thanhTimKiem;
    private TextView gioiTinh,KQHT;
    private Spinner lopHoc;
    private ArrayList<String> list_spinner;
    private Adapter_Spinner adapterSpinner;
    private String lopHoc_selected;
    private boolean check,checkFloat;
    private DatePickerDialog datePickerDialog;

    public static Fragment_SinhVien newInstance() {
        Fragment_SinhVien fragment = new Fragment_SinhVien();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chung,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = view.findViewById(R.id.frag_chung_rv);
        floatAdd = view.findViewById(R.id.frag_chung_floatAdd);
        floatAll = view.findViewById(R.id.frag_chung_float);
        floatSearch = view.findViewById(R.id.frag_chung_floatSearch);
        other();
    }

    private void other() {
        db = new DAO_SinhVien(getActivity());
        db_lopHoc = new DAO_LopHoc(getActivity());
        db_point = new DAO_Point(getActivity());
        list_spinner = new ArrayList<>();
        list = new ArrayList<>();
        action = new MainActivity();
        manager = getActivity().getSupportFragmentManager();
        action = new MainActivity();
        checkFloat = false;
    }

    private void addAction() {
        floatAll.setOnClickListener(this);
        floatSearch.setOnClickListener(this);
        floatAdd.setOnClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        onLoad();
    }

    private void onLoad() {
        init();
    }

    private void init() {
        addRecycler();
        addAction();
    }

    private void addRecycler() {
        list = db.read();
        adapter_sinhVien = new Adapter_SinhVien(this);
        rv.setAdapter(adapter_sinhVien);
        rv.setLayoutManager(new GridLayoutManager(getActivity(),2));
        adapter_sinhVien.setData(list);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.frag_chung_float:
                if(checkFloat)
                    show_hide_float(View.GONE);
                else
                    show_hide_float(View.VISIBLE);
                break;
            case R.id.frag_chung_floatAdd:
                show_hide_float(View.GONE);
                actionFloatAdd();
                break;
            case R.id.frag_chung_floatSearch:
                show_hide_float(View.GONE);
                actionSearch();
        }
    }

    private void actionSearch() {
        list_tam = new ArrayList<>();
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_search);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        //ánh Xạ
        thanhTimKiem = dialog.findViewById(R.id.dialog_search_et_timKiem);
        timKiem = dialog.findViewById(R.id.dialog_search_btn_timKiem);
        //
        timKiem.setOnClickListener(view -> {
            String name_search = thanhTimKiem.getText().toString();
            if(name_search.length() == 0){
                adapter_sinhVien.setData(list);
                dialog.cancel();
            }else{
                for(SinhVien x : list){
                    if(x.getTen_SV().contains(name_search)){
                        list_tam.add(x);
                    }
                }
            }
            adapter_sinhVien.setData(list_tam);
            dialog.cancel();
        });

        dialog.show();
    }

    private void show_hide_float(int state) {
        floatSearch.setVisibility(state);
        floatAdd.setVisibility(state);
        if(state == View.GONE)
            checkFloat = false;
        else
            checkFloat = true;

    }
    
    private void actionFloatAdd() {
        if(db_lopHoc.read().size() > 0){
            action.chuyen_Fragment(manager,Fragment_ThemSinhVien.newInstance());
            return;
        }
        CustomToast.makeText(getActivity(),"Bạn cần phải tạo thêm lớp học",0,false).show();
    }

    @Override
    public void actionSua(int index) {
        SinhVien sv = list.get(index);
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_thong_tin_sinh_vien);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //ánh  xạ
        thoat = dialog.findViewById(R.id.dialog_thong_tin_sinh_vien_btnImg_huy);
        sua_luu_1 = dialog.findViewById(R.id.dialog_thong_tin_sinh_vien_btn_suaLuu);
        ma = dialog.findViewById(R.id.dialog_thong_tin_sinh_vien_et_MSV);
        ten = dialog.findViewById(R.id.dialog_thong_tin_sinh_vien_et_ten);
        lopHoc = dialog.findViewById(R.id.dialog_thong_tin_sinh_vien_sp_lopHoc);
        ngaySinh = dialog.findViewById(R.id.dialog_thong_tin_sinh_vien_et_ngaySinh);
        gioiTinh = dialog.findViewById(R.id.dialog_thong_tin_sinh_vien_tv_gioiTinh);
        SDT = dialog.findViewById(R.id.dialog_thong_tin_sinh_vien_et_SDT);
        KQHT = dialog.findViewById(R.id.dialog_thong_tin_sinh_vien_tv_ketQuaHocTap);
        //gán
        list_spinner = db_lopHoc.readMa();
        adapterSpinner = new Adapter_Spinner(getActivity(),list_spinner,null);
        lopHoc.setAdapter(adapterSpinner);
        ma.setText(sv.getMa_SV());
        ten.setText(sv.getTen_SV());
        gioiTinh.setText(sv.getGioi_tinh());
        ngaySinh.setText(sv.getNgay_sinh());
        lopHoc.setSelection(list_spinner.indexOf(sv.getMa_lop()));
        SDT.setText(sv.getSDT());
        lopHoc_selected = list_spinner.get(0);
        //
        ma.setEnabled(false);
        ten.setEnabled(false);
        gioiTinh.setEnabled(false);
        ngaySinh.setEnabled(false);
        lopHoc.setEnabled(false);
        SDT.setEnabled(false);
        //sự kiện
        thoat.setOnClickListener(view -> {
            check = false;
            DrawableCompat.setTint(ma.getBackground(), ContextCompat.getColor(getActivity(), R.color.white));
            DrawableCompat.setTint(ten.getBackground(), ContextCompat.getColor(getActivity(), R.color.white));
            DrawableCompat.setTint(SDT.getBackground(), ContextCompat.getColor(getActivity(), R.color.white));
            DrawableCompat.setTint(ngaySinh.getBackground(), ContextCompat.getColor(getActivity(), R.color.white));
            DrawableCompat.setTint(lopHoc.getBackground(),ContextCompat.getColor(getActivity(),R.color.white));
            dialog.cancel();
        });

        lopHoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lopHoc_selected = list_spinner.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        gioiTinh.setOnClickListener(view12 -> {
            if(gioiTinh.getText().toString().equalsIgnoreCase("Nam"))
                gioiTinh.setText("Nữ");
            else
                gioiTinh.setText("Nam");
        });

        ngaySinh.setOnClickListener(view13 -> {
            datePickerDialog = new DatePickerDialog(getActivity(), (datePicker, i, i1, i2) -> ngaySinh.setText(i2 + "/" + (i1 + 1) + "/" + i), Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DATE));
            datePickerDialog.show();
        });

        KQHT.setOnClickListener(view14 -> {
            if(db_point.readWithMaSV(sv.getMa_SV()).size() == 0){
                CustomToast.makeText(getActivity(),"Hãy thêm môn học vào chuyên ngành",0,false).show();
                return;
            }
            action.chuyen_Fragment(manager,Fragment_KetQuaHocTap.newInstance(sv.getMa_SV()));
            dialog.cancel();
        });
        sua_luu_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check){
                    String ten_sv = ten.getText().toString(),ma_sv = ma.getText().toString(),
                            gioiTinh_sv = gioiTinh.getText().toString(),
                            ngaySinh_sv = ngaySinh.getText().toString(),
                            sdt = SDT.getText().toString();
                    if(validate(ma_sv,ten_sv,sdt)){
                        sua_luu_1.setText("Sửa");
                        sua_luu_1.setTextColor(getActivity().getResources().getColor(R.color.purple_700));
                        sua_luu_1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fix_30, 0, 0, 0);
                        actionSuaSinhVien(ma_sv,ten_sv,sdt,gioiTinh_sv,ngaySinh_sv);
                        changeColorTint(R.color.white);
                        check = false;
                        dialog.cancel();
                    }
                }else{
                    sua_luu_1.setText("Lưu");
                    sua_luu_1.setTextColor(Color.RED);
                    changeColorTint(R.color.black);
                    sua_luu_1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.save_file_32, 0, 0, 0);
                    setChangeEnable(true);
                    check = true;
                }
            }

            private void actionSuaSinhVien(String ma_sv, String ten_sv, String sdt, String gioiTinh_sv, String ngaySinh_sv) {
                SinhVien newSV = new SinhVien(ma_sv,ten_sv,lopHoc_selected,
                        db_lopHoc.readObj(lopHoc_selected).getMa_nganh(),sdt,gioiTinh_sv,ngaySinh_sv);
                db.update(sv.getMa_SV(),ma_sv,ten_sv,lopHoc_selected,
                        db_lopHoc.readObj(lopHoc_selected).getMa_nganh(),sdt,gioiTinh_sv,ngaySinh_sv);
                list.set(index,newSV);
                adapter_sinhVien.notifyDataSetChanged();
                CustomToast.makeText(getActivity(),"Update !",0,true).show();
            }

            private boolean validate(String ma_sv, String ten_sv, String sdt) {
                DrawableCompat.setTint(ma.getBackground(), ContextCompat.getColor(getActivity(), R.color.white));
                DrawableCompat.setTint(ten.getBackground(), ContextCompat.getColor(getActivity(), R.color.white));
                DrawableCompat.setTint(SDT.getBackground(), ContextCompat.getColor(getActivity(), R.color.white));
                if(!ma_sv.matches("^[\\da-zA-z]+$")){
                    CustomToast.makeText(getActivity(),"Mã không hợp lệ",0,false).show();
                    focusView(ma);
                    DrawableCompat.setTint(ma.getBackground(), ContextCompat.getColor(getActivity(), R.color.red));
                    return false;
                }
                if(!ten_sv.matches("^[^\\d]+$")){
                    CustomToast.makeText(getActivity(),"Tên không hợp lệ",0,false).show();
                    focusView(ten);
                    DrawableCompat.setTint(ten.getBackground(), ContextCompat.getColor(getActivity(), R.color.red));
                    return false;
                }
                if(!sdt.matches("0\\d{9}")){
                    CustomToast.makeText(getActivity(),"Số điện thoại không hợp lệ",0,false).show();
                    focusView(SDT);
                    DrawableCompat.setTint(SDT.getBackground(), ContextCompat.getColor(getActivity(), R.color.red));
                    return false;
                }
                if(db.getIndex(ma_sv) >= 0 && !ma_sv.equals(sv.getMa_SV())){
                    CustomToast.makeText(getActivity(),"Sinh Viên đã tồn tại",0,false).show();
                    DrawableCompat.setTint(ma.getBackground(), ContextCompat.getColor(getActivity(), R.color.red));
                    return false;
                }
                return true;
            }

            private void setChangeEnable(boolean b) {
                ma.setEnabled(b);
                ten.setEnabled(b);
                gioiTinh.setEnabled(b);
                ngaySinh.setEnabled(b);
                lopHoc.setEnabled(b);
                SDT.setEnabled(b);
            }

            private void changeColorTint(int color){
                DrawableCompat.setTint(ma.getBackground(), ContextCompat.getColor(getActivity(), color));
                DrawableCompat.setTint(ten.getBackground(), ContextCompat.getColor(getActivity(), color));
                DrawableCompat.setTint(SDT.getBackground(), ContextCompat.getColor(getActivity(), color));
                DrawableCompat.setTint(ngaySinh.getBackground(), ContextCompat.getColor(getActivity(), color));
                DrawableCompat.setTint(lopHoc.getBackground(),ContextCompat.getColor(getActivity(),color));
            }

            private void focusView(EditText et){
                et.requestFocus();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        dialog.show();
    }

    @Override
    public void acitonXoa(int index) {
        db.delete(list.get(index).getMa_SV());
        list.remove(index);
        adapter_sinhVien.notifyItemRemoved(index);
        CustomToast.makeText(getActivity(),"Xoá thành công",0,true);
    }
}
