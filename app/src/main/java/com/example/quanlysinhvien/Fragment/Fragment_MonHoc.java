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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlysinhvien.Adapter.Adapter_MonHoc;
import com.example.quanlysinhvien.DAO.DAO_MonHoc;
import com.example.quanlysinhvien.DTO.MonHoc;
import com.example.quanlysinhvien.Other.CustomToast;
import com.example.quanlysinhvien.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Fragment_MonHoc extends Fragment implements View.OnClickListener,Adapter_MonHoc.OnActionSuaXoa {
    private View view;
    private RecyclerView rv;
    private DAO_MonHoc db;
    private Adapter_MonHoc adapter_monHoc;
    private boolean checkFloat,checkList;
    private FloatingActionButton floatAdd,floatAll,floatSearch;
    private EditText ten,ma,tinChi,thanhTimKiem;
    private Button them,timKiem,sua;
    private ImageButton huy;
    private ArrayList<MonHoc> list,list_tam;
    private MonHoc obj_old;
    private TextView title;

    public static Fragment_MonHoc newInstance() {
        Fragment_MonHoc fragment = new Fragment_MonHoc();
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
        db = new DAO_MonHoc(getActivity());
        list = new ArrayList<>();
        checkFloat = false;
        checkList = false;
    }

    private void anhXa() {
        rv = view.findViewById(R.id.frag_chung_rv);
        floatAdd = view.findViewById(R.id.frag_chung_floatAdd);
        floatAll = view.findViewById(R.id.frag_chung_float);
        floatSearch = view.findViewById(R.id.frag_chung_floatSearch);
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
        addListView();
        addAction();
    }

    private void addAction() {
        floatAdd.setOnClickListener(this);
        floatAll.setOnClickListener(this);
        floatSearch.setOnClickListener(this);
    }

    private void actionAdd() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_mon_hoc);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //anhXa
        ten = dialog.findViewById(R.id.dialog_mon_hoc_et_tenMonHoc);
        ma = dialog.findViewById(R.id.dialog_mon_hoc_et_maMonHoc);
        tinChi = dialog.findViewById(R.id.dialog_mon_hoc_et_tinChi);
        them = dialog.findViewById(R.id.dialog_mon_hoc_btn_themSua);
        huy = dialog.findViewById(R.id.dialog_mon_hoc_btnImg_huy);
        //
        ma.requestFocus();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.showSoftInput(ma, InputMethodManager.SHOW_IMPLICIT);
        //
        them.setOnClickListener(view -> {
            String t = ten.getText().toString(),m = ma.getText().toString(),tin_chi = tinChi.getText().toString();
            if(check(t,m,tin_chi)){
                int tc = Integer.parseInt(tin_chi);
                db.create(m,t,tc);
                list.add(new MonHoc(m,t,tc));
                CustomToast.makeText(getActivity(),"Thêm thành công",0,true).show();
                if(checkList)
                    adapter_monHoc.setData(list);
                else
                    adapter_monHoc.notifyItemInserted(list.size() - 1);
                dialog.cancel();
            }
        });
        huy.setOnClickListener(view -> dialog.cancel());
        dialog.show();
    }

    private boolean check(String ten, String ma,String tin_chi) {
        if(ma.isEmpty() || ten.isEmpty() || tin_chi.isEmpty()){
            CustomToast.makeText(getActivity(),"Dữ liệu không được bỏ trống",0,false).show();
            return false;
        }
        if(!ma.matches("^[a-zA-Z\\d]+$")){
            CustomToast.makeText(getActivity(),"Mã môn không hợp lệ",0,false).show();
            return false;
        }
        if(!tin_chi.matches("^[1-9]\\d*$")){
            CustomToast.makeText(getActivity(),"Số tín chỉ nhỏ nhất là 1",0,false).show();
            return false;
        }
        if(db.getIndex(ma) >= 0){
            CustomToast.makeText(getActivity(),"Môn học đã tồn tại",0,false).show();
            return false;
        }
        return true;
    }

    private void addListView() {
        list = db.read();
        adapter_monHoc = new Adapter_MonHoc(this);
        rv.setAdapter(adapter_monHoc);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter_monHoc.setData(list);
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
                adapter_monHoc.setData(list);
                dialog.cancel();
            }else{
                for(MonHoc x : list){
                    if(x.getTen_mon().contains(name_search)){
                        list_tam.add(x);
                    }
                }
            }
            checkList = true;
            adapter_monHoc.setData(list_tam);
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

    @Override
    public void actionSua(int index) {
        obj_old = list.get(index);
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_mon_hoc);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //
        ten = dialog.findViewById(R.id.dialog_mon_hoc_et_tenMonHoc);
        ma = dialog.findViewById(R.id.dialog_mon_hoc_et_maMonHoc);
        tinChi = dialog.findViewById(R.id.dialog_mon_hoc_et_tinChi);
        sua = dialog.findViewById(R.id.dialog_mon_hoc_btn_themSua);
        huy = dialog.findViewById(R.id.dialog_mon_hoc_btnImg_huy);
        title = dialog.findViewById(R.id.dialog_mon_hoc_tv_title);
        //
        ma.requestFocus();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.showSoftInput(ma, InputMethodManager.SHOW_IMPLICIT);
        //
        title.setText("Sửa Môn Học");
        ten.setText(obj_old.getTen_mon());
        ma.setText(obj_old.getMa_mon());
        tinChi.setText(String.valueOf(obj_old.getTinChi()));
        //
        huy.setOnClickListener(view -> dialog.cancel());
        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ten.getText().toString(),ma_mon = ma.getText().toString(),tc = tinChi.getText().toString();
                if(check(name,ma_mon,obj_old.getMa_mon(),tc)){
                    db.update(obj_old.getMa_mon(),ma_mon,name,Integer.parseInt(tc));
                    list.set(index,new MonHoc(ma_mon,name,Integer.parseInt(tc)));
                    adapter_monHoc.notifyItemChanged(index);
                    CustomToast.makeText(getActivity(),"Cập nhật thành công !",0,true);
                    dialog.cancel();
                }
            }
            private boolean check(String name, String ma_mon,String ma_mon_old, String tc) {
                if(name.isEmpty() || ma_mon.isEmpty() || tc.isEmpty()){
                    CustomToast.makeText(getActivity(),"Dữ liệu không được để trống",0,false).show();
                    return false;
                }
                if(!ma_mon.matches("^[a-zA-Z\\d]+$")){
                    CustomToast.makeText(getActivity(),"Mã môn không hợp lệ",0,false).show();
                    return false;
                }
                if(!ma_mon.equals(ma_mon_old) && db.getIndex(ma_mon) >= 0){
                    CustomToast.makeText(getActivity(),"Môn này đã tồn tại",0,false);
                    return false;
                }
                if(!tc.matches("\\d+") || Integer.parseInt(tc) < 1){
                    CustomToast.makeText(getActivity(),"Tín chỉ phải là số có giá trị nhỏ nhất là 1",0,false).show();
                    return false;
                }
                return true;
            }
        });
        dialog.show();
    }

    @Override
    public void actionXoa(int index) {
        db.delete(list.get(index).getMa_mon());
        list.remove(index);
        adapter_monHoc.notifyItemRemoved(index);
        adapter_monHoc.notifyItemRangeChanged(index,list.size());
    }
}
