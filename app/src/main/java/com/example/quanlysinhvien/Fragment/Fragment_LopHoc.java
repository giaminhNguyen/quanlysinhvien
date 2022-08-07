package com.example.quanlysinhvien.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlysinhvien.Adapter.Adapter_LopHoc;
import com.example.quanlysinhvien.Adapter.Adapter_Spinner;
import com.example.quanlysinhvien.DAO.DAO_ChuyenNganh;
import com.example.quanlysinhvien.DAO.DAO_LopHoc;
import com.example.quanlysinhvien.DTO.LopHoc;
import com.example.quanlysinhvien.Other.CustomToast;
import com.example.quanlysinhvien.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Fragment_LopHoc extends Fragment implements View.OnClickListener ,Adapter_LopHoc.OnActionSuaxoa{
    private View view;
    private Adapter_LopHoc adapter_lopHoc;
    private DAO_LopHoc db;
    private boolean checkFloat,checkList;
    private EditText thanhTimKiem,ma_Lop;
    private Spinner chuyen_Nganh;
    private Button timKiem,sua,them;
    private ImageButton huy;
    private DAO_ChuyenNganh db_Chuyen_Nganh;
    private RecyclerView rv;
    private FloatingActionButton floatAdd,floatAll,floatSearch;
    private Adapter_Spinner adapterSpinner;
    private ArrayList<String> listSpinner;
    private ArrayList<LopHoc> list,list_tam;
    private String ma_lop_old,ma_nganh_old;
    private TextView title;

    public static Fragment_LopHoc newInstance() {
        Fragment_LopHoc fragment = new Fragment_LopHoc();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chung,container,false);
        anhXa();
        other();
        return view;
    }

    private void other() {
        checkFloat = false;
        checkList = false;
        db = new DAO_LopHoc(getActivity());
        db_Chuyen_Nganh = new DAO_ChuyenNganh(getActivity());
        listSpinner = new ArrayList<>();
        list = new ArrayList<>();
    }

    private void addAction() {
        floatAdd.setOnClickListener(this);
        floatSearch.setOnClickListener(this);
        floatAll.setOnClickListener(this);
    }



    private void anhXa() {
        rv = view.findViewById(R.id.frag_chung_rv);
        floatAdd = view.findViewById(R.id.frag_chung_floatAdd);
        floatAll = view.findViewById(R.id.frag_chung_float);
        floatSearch = view.findViewById(R.id.frag_chung_floatSearch);
    }


    private void onLoad() {
        init();
    }

    private void init() {
        addListView();
        addAction();
    }

    private void addListView() {
        list = db.read();
        adapter_lopHoc = new Adapter_LopHoc(this,getActivity());
        rv.setAdapter(adapter_lopHoc);
        rv.setLayoutManager(new GridLayoutManager(getActivity(),1));
        adapter_lopHoc.setData(list);
    }

    @Override
    public void onResume() {
        super.onResume();
        onLoad();
    }

    private boolean check(String maLop, String maChuyenNganh) {
        if(maLop.isEmpty() || maChuyenNganh.isEmpty()){
            CustomToast.makeText(getActivity(),"Dữ liệu không được bỏ trống",0,false).show();
            return false;
        }
        if(!maLop.matches("^[a-zA-Z\\d]+$")){
            CustomToast.makeText(getActivity(),"Mã lớp sai định dạng",0,false).show();
            return false;
        }
        if(db.getIndex(maLop) >= 0){
            CustomToast.makeText(getActivity(),"Lớp đã tồn tại",0,false).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.frag_chung_float:
                if(checkFloat)
                    show_hide_float(View.GONE);
                else
                    show_hide_float(View.VISIBLE);
                break;
            case R.id.frag_chung_floatSearch:
                show_hide_float(View.GONE);
                actionSearch();
                break;
            case R.id.frag_chung_floatAdd:
                show_hide_float(View.GONE);
                actionAdd();
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
                checkList = false;
                adapter_lopHoc.setData(list);
                dialog.cancel();
            }else{
                for(LopHoc x : list){
                    if(x.getMa_lop().contains(name_search)){
                        list_tam.add(x);
                    }
                }
            }
            checkList = true;
            adapter_lopHoc.setData(list_tam);
            dialog.cancel();
        });

        dialog.show();
    }

    private void show_hide_float(int state) {
        floatSearch.setVisibility(state);
        floatAdd.setVisibility(state);
        if(state == View.GONE){
            checkFloat = false;
        }else
            checkFloat = true;
    }

    private void actionAdd() {
        if(db_Chuyen_Nganh.read().size() == 0){
            CustomToast.makeText(getActivity(),"Bạn cần phải tạo thêm chuyên ngành",0,false).show();
            return;
        }

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_lop_hoc);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //
        huy = dialog.findViewById(R.id.dialog_lop_hoc_btnImg_huy);
        them = dialog.findViewById(R.id.dialog_lop_hoc_btn_themSua);
        ma_Lop = dialog.findViewById(R.id.dialog_lop_hoc_et_maLop);
        chuyen_Nganh = dialog.findViewById(R.id.dialog_lop_hoc_sn_chuyenNganh);
        //
        ma_Lop.requestFocus();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.showSoftInput(ma_Lop, InputMethodManager.SHOW_IMPLICIT);
        //
        listSpinner = db_Chuyen_Nganh.readAdapter();
        adapterSpinner = new Adapter_Spinner(getActivity(),listSpinner,null);
        chuyen_Nganh.setAdapter(adapterSpinner);
        huy.setOnClickListener(view -> dialog.dismiss());
        them.setOnClickListener(view ->{
            String maChuyenNganh = db_Chuyen_Nganh.readMaOfIndex(chuyen_Nganh.getSelectedItemPosition())
                    , maLop = ma_Lop.getText().toString();
            if(check(maLop,maChuyenNganh)){
                CustomToast.makeText(getActivity(),"Thêm thành công",0,true).show();
                db.create(maLop,maChuyenNganh);
                list.add(new LopHoc(maLop,maChuyenNganh));
                if(checkList)
                    adapter_lopHoc.setData(list);
                else
                    adapter_lopHoc.notifyItemInserted(list.size() - 1);
                dialog.cancel();
            }
        });
        dialog.show();
    }
    @Override
    public void actionSua(int index) {
        ma_lop_old = list.get(index).getMa_lop();
        ma_nganh_old = list.get(index).getMa_nganh();
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_lop_hoc);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //ánh xạ
        huy = dialog.findViewById(R.id.dialog_lop_hoc_btnImg_huy);
        sua = dialog.findViewById(R.id.dialog_lop_hoc_btn_themSua);
        ma_Lop = dialog.findViewById(R.id.dialog_lop_hoc_et_maLop);
        chuyen_Nganh = dialog.findViewById(R.id.dialog_lop_hoc_sn_chuyenNganh);
        title = dialog.findViewById(R.id.dialog_lop_hoc_tv_title);
        //
        listSpinner = db_Chuyen_Nganh.readAdapter();
        adapterSpinner = new Adapter_Spinner(getActivity(),listSpinner,null);
        chuyen_Nganh.setAdapter(adapterSpinner);
        //
        ma_Lop.requestFocus();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.showSoftInput(ma_Lop, InputMethodManager.SHOW_IMPLICIT);
        //gán
        title.setText("Sửa Lớp Học");
        sua.setText("Sửa");
        ma_Lop.setText(ma_lop_old);
        chuyen_Nganh.setSelection(db_Chuyen_Nganh.getIndex(ma_nganh_old));
        //
        huy.setOnClickListener(view -> dialog.cancel());
        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ma_lop = ma_Lop.getText().toString();
                if(check(ma_lop)){
                    db.update(ma_lop_old,ma_lop,db_Chuyen_Nganh.readMaOfIndex(chuyen_Nganh.getSelectedItemPosition()));
                    CustomToast.makeText(getActivity(),"Cập nhật thành công",0,true).show();
                    list.set(index,new LopHoc(ma_lop,db_Chuyen_Nganh.readMaOfIndex(chuyen_Nganh.getSelectedItemPosition())));
                    adapter_lopHoc.notifyItemChanged(index);
                    dialog.cancel();
                }
            }
            private boolean check(String ma_lop){
                if(ma_lop.isEmpty()){
                    CustomToast.makeText(getActivity(),"Dữ liệu không được bỏ trống",0,false).show();
                    return false;
                }
                if(!ma_lop.matches("^[a-zA-Z\\d]+$")){
                    CustomToast.makeText(getActivity(),"Mã lớp sai định dạng",0,false).show();
                    return false;
                }
                if(!ma_lop.equals(ma_lop_old) && db.getIndex(ma_lop) >= 0){
                    CustomToast.makeText(getActivity(),"Lớp này đã tồn tại",0,false).show();
                    return false;
                }
                return true;
            }
        });
        dialog.show();
    }

    @Override
    public void actionXoa(int index) {
        ma_lop_old = list.get(index).getMa_lop();
        list.remove(index);
        db.delete(ma_lop_old);
        adapter_lopHoc.notifyItemRemoved(index);
        adapter_lopHoc.notifyItemRangeChanged(index,list.size());
        CustomToast.makeText(getActivity(),"Xoá thành công",0,true).show();
    }
}
