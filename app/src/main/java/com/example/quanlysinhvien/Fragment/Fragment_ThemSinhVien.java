package com.example.quanlysinhvien.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.quanlysinhvien.Adapter.Adapter_Spinner;
import com.example.quanlysinhvien.Adapter.Adapter_DSSinhVienMoiThem;
import com.example.quanlysinhvien.DAO.DAO_ChuyenNganh;
import com.example.quanlysinhvien.DAO.DAO_LopHoc;
import com.example.quanlysinhvien.DAO.DAO_Point;
import com.example.quanlysinhvien.DAO.DAO_QLMH;
import com.example.quanlysinhvien.DAO.DAO_SinhVien;
import com.example.quanlysinhvien.DTO.SinhVien;
import com.example.quanlysinhvien.MainActivity;
import com.example.quanlysinhvien.Other.Action;
import com.example.quanlysinhvien.Other.CustomToast;
import com.example.quanlysinhvien.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Fragment_ThemSinhVien extends Fragment {
    private EditText MSV,ten,ngaySinh,SDT;
    private RadioButton nam;
    private Spinner maLop;
    private Button them,troVe;
    private RecyclerView rv;
    private Adapter_DSSinhVienMoiThem adapter;
    private ArrayList<SinhVien> list;
    private ArrayList<String> list_spinner;
    private String ma_lop;
    private DAO_SinhVien db;
    private DAO_LopHoc db_lopHoc;
    private Adapter_Spinner adapterSpinner;
    private DatePickerDialog pickerDialog;
    private Action action_;

    public Fragment_ThemSinhVien() {
    }

    public static Fragment_ThemSinhVien newInstance() {
        Fragment_ThemSinhVien fragment = new Fragment_ThemSinhVien();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment__them_sinh_vien, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        maLop = view.findViewById(R.id.frag_them_sinh_vien_sp_maLop);
        ten = view.findViewById(R.id.frag_them_sinh_vien_et_tenSV);
        MSV = view.findViewById(R.id.frag_them_sinh_vien_et_MSV);
        SDT = view.findViewById(R.id.frag_them_sinh_vien_et_SDT);
        ngaySinh = view.findViewById(R.id.frag_them_sinh_vien_et_ngaySinh);
        them = view.findViewById(R.id.frag_them_sinh_vien_btn_them);
        troVe =  view.findViewById(R.id.frag_them_sinh_vien_btn_troVe);
        nam = view.findViewById(R.id.frag_them_sinh_vien_rdo_nam);
        rv = view.findViewById(R.id.frag_them_sinh_vien_rv);
        list = new ArrayList<>();
        list_spinner = new ArrayList<>();
        db = new DAO_SinhVien(getActivity());
        db_lopHoc = new DAO_LopHoc(getActivity());
        action_ = new MainActivity();
        //
        focusView(ten);
        //
        init();
    }

    private void init() {
        addSpinner();
        addRecyclerView();
        addAction();
    }

    private void addRecyclerView() {
        adapter = new Adapter_DSSinhVienMoiThem(getActivity(),list);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void addSpinner() {
        list_spinner = db_lopHoc.readMa();
        adapterSpinner = new Adapter_Spinner(getActivity(),list_spinner,"");
        maLop.setAdapter(adapterSpinner);
        if(list_spinner.size() > 0){
            ma_lop = list_spinner.get(0);
        }
    }

    private void addAction() {
        them.setOnClickListener(view -> {
            String name = ten.getText().toString(),sdt = SDT.getText().toString(),msv = MSV.getText().toString(),ns = ngaySinh.getText().toString();
            if(check(name,msv,sdt,ns)){
                String ma_nganh = db_lopHoc.readObj(ma_lop).getMa_nganh();
                db.create(msv,name,ma_lop,ma_nganh,sdt,getGioiTinh(),ns);
                list.add(new SinhVien(msv,name,ma_lop,ma_nganh,sdt,getGioiTinh(),ns));
                CustomToast.makeText(getActivity(),"Thêm Thành công",0,true).show();
                adapter.notifyItemInserted(list.size()-1);
                xoaForm();
            }
        });
        maLop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ma_lop = list_spinner.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ngaySinh.setOnClickListener(view -> {
            pickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    ngaySinh.setText(i2 + "/" + (i1 + 1) + "/" + i);
                }
            }, Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DATE));
            pickerDialog.show();
        });
        troVe.setOnClickListener(view -> action_.chuyen_Fragment(getActivity().getSupportFragmentManager(),Fragment_Lop.newInstance(1)));
    }

    private void xoaForm() {
        ten.setText("");
        MSV.setText("");
        SDT.setText("");
        ngaySinh.setText("");
    }

    private boolean check(String name, String msv, String sdt, String ns) {
        if(name.isEmpty() || msv.isEmpty() || sdt.isEmpty() || ns.isEmpty()){
            CustomToast.makeText(getActivity(),"Dữ liệu không được bỏ trống",0,false).show();
            return false;
        }
        if(!name.matches("^[^\\d]+$")){
            CustomToast.makeText(getActivity(),"Tên không hợp lệ",0,false).show();
            focusView(ten);
            return false;
        }
        if(!msv.matches("^[a-zA-Z\\d]+$")){
            CustomToast.makeText(getActivity(),"Mã không hợp lệ",0,false).show();
            focusView(MSV);
            return false;
        }
        if(!sdt.matches("0\\d{9}")){
            CustomToast.makeText(getActivity(),"Số điện thoại không hợp lệ",0,false).show();
            focusView(SDT);
            return false;
        }
        if(db.getIndex(msv) >= 0){
            CustomToast.makeText(getActivity(),"Sinh viên đã tồn tại",0,false).show();
            focusView(MSV);
            return false;
        }

        return true;
    }
    private String getGioiTinh(){
        if(nam.isChecked())
            return "Nam";
        return "Nữ";
    }
    private void focusView(EditText et){
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }
}