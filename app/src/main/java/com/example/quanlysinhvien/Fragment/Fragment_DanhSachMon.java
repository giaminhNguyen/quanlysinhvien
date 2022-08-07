package com.example.quanlysinhvien.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlysinhvien.Adapter.Adapter_DanhSachMon;
import com.example.quanlysinhvien.Adapter.Adapter_Spinner;
import com.example.quanlysinhvien.DAO.DAO_ChuyenNganh;
import com.example.quanlysinhvien.DAO.DAO_MonHoc;
import com.example.quanlysinhvien.DAO.DAO_QLMH;
import com.example.quanlysinhvien.DTO.MonHoc;
import com.example.quanlysinhvien.Other.CustomToast;
import com.example.quanlysinhvien.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Fragment_DanhSachMon extends Fragment implements View.OnClickListener {
    private Spinner spinner_Nganh,spinner_MonHoc;
    private Button them;
    private ImageButton huy;
    private RecyclerView rv;
    private FloatingActionButton floatAdd;
    private DAO_ChuyenNganh db_ChuyenNganh;
    private DAO_QLMH db_QLMH;
    private DAO_MonHoc db_MonHoc;
    private String maNganh,maMon;
    private ArrayList<String> list_spinner_Nganh,list_spinner_Mon_Hoc;
    private Adapter_DanhSachMon adapterDanhSachMon;
    private Adapter_Spinner adapter_spinner_Nganh,adapter_spinner_Mon_Hoc;
    private ArrayList<MonHoc> list;

    public static Fragment_DanhSachMon newInstance() {
        Fragment_DanhSachMon fragment = new Fragment_DanhSachMon();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_danh_sach_mon,container,false);
    }

    private void other() {
        db_ChuyenNganh = new DAO_ChuyenNganh(getActivity());
        db_QLMH = new DAO_QLMH(getActivity());
        list_spinner_Nganh = new ArrayList<>();
        list_spinner_Mon_Hoc = new ArrayList<>();
        list = new ArrayList<>();
        db_MonHoc = new DAO_MonHoc(getActivity());
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
        addSpinner();
        addRecyclerView();
        addAction();
    }

    private void addAction() {
        floatAdd.setOnClickListener(this);
    }


    private void addRecyclerView() {
        list = db_MonHoc.readObjMa(db_QLMH.readMaMon(maNganh));
        adapterDanhSachMon = new Adapter_DanhSachMon(getActivity());
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapterDanhSachMon);
        adapterDanhSachMon.setData(list,maNganh);
    }

    private void addSpinner() {
        list_spinner_Nganh = db_ChuyenNganh.readAdapter();
        if(list_spinner_Nganh.size() > 0){
            maNganh = list_spinner_Nganh.get(0).substring(0,list_spinner_Nganh.get(0).indexOf("-"));
            adapter_spinner_Nganh = new Adapter_Spinner(getActivity(),list_spinner_Nganh,".");
            spinner_Nganh.setAdapter(adapter_spinner_Nganh);
            spinner_Nganh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    maNganh = list_spinner_Nganh.get(i).substring(0,list_spinner_Nganh.get(i).indexOf("-"));
                    list = db_MonHoc.readObjMa(db_QLMH.readMaMon(maNganh));
                    adapterDanhSachMon.setData(list,maNganh);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner_Nganh = view.findViewById(R.id.frag_danh_sach_mon_sp);
        rv = view.findViewById(R.id.frag_danh_sach_mon_rv);
        floatAdd = view.findViewById(R.id.frag_danh_sach_mon_FloatAdd);
        other();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.frag_danh_sach_mon_FloatAdd:
                actionAdd();
                break;
        }
    }

    private void actionAdd() {
        if(db_ChuyenNganh.read().size() == 0){
            CustomToast.makeText(getActivity(),"Bạn cần phải tạo thêm chuyên ngành",0,false).show();
            return;
        }
        if(db_MonHoc.read().size() == 0){
            CustomToast.makeText(getActivity(),"Bạn cần phải tạo thêm môn học",0,false).show();
            return;
        }
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_danh_sach_mon);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // anhXa
        spinner_MonHoc = dialog.findViewById(R.id.dialog_danh_sach_lop_sn_monHoc);
        them = dialog.findViewById(R.id.dialog_danh_sach_lop_btn_them);
        huy = dialog.findViewById(R.id.dialog_danh_sach_lop_btnImg_huy);
        //gán
        list_spinner_Mon_Hoc = db_MonHoc.readAdapter();
        String check;
        for(MonHoc x :list){
            check = db_MonHoc.readDongWithMa(x.getMa_mon());
            if(list_spinner_Mon_Hoc.contains(check))
                list_spinner_Mon_Hoc.remove(check);
        }
        if(list_spinner_Mon_Hoc.size() > 0){
            adapter_spinner_Mon_Hoc = new Adapter_Spinner(getActivity(),list_spinner_Mon_Hoc,null);
            spinner_MonHoc.setAdapter(adapter_spinner_Mon_Hoc);
            //
            spinner_MonHoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    maMon = list_spinner_Mon_Hoc.get(i);
                    maMon = maMon.substring(0,maMon.indexOf("-"));
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            huy.setOnClickListener(view -> dialog.cancel());
            them.setOnClickListener(view -> {
                if(list_spinner_Mon_Hoc.size() > 0){
                    list.add(db_MonHoc.readObj(maMon));
                    db_QLMH.create(maNganh,maMon);
                    adapterDanhSachMon.notifyItemInserted(list.size() -1);
                }

                dialog.cancel();
            });
            dialog.show();
        }else
            CustomToast.makeText(getActivity(),"Chuyên ngành đã có hết các môn học",0,true).show();

    }
}
