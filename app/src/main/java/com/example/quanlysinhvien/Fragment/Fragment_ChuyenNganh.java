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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlysinhvien.Adapter.Adapter_ChuyenNganh;
import com.example.quanlysinhvien.DAO.DAO_ChuyenNganh;
import com.example.quanlysinhvien.DTO.ChuyenNganh;
import com.example.quanlysinhvien.Other.CustomToast;
import com.example.quanlysinhvien.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Fragment_ChuyenNganh extends Fragment implements View.OnClickListener ,Adapter_ChuyenNganh.OnActionSuaXoa{
    private RecyclerView rv;
    private FloatingActionButton floatAdd,floatAll,floatSearch;
    private boolean checkFloat,checkList;
    private EditText thanhTimKiem;
    private Button timKiem,sua,them;
    private Adapter_ChuyenNganh adapterChuyenNganh;
    private DAO_ChuyenNganh db;
    private EditText ten,ma;;
    private ImageButton huy;
    private String ma_nganh;
    private ArrayList<ChuyenNganh> list,list_tam;
    private TextView title;

    public static Fragment_ChuyenNganh newInstance() {
        Fragment_ChuyenNganh fragment = new Fragment_ChuyenNganh();
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
        db = new DAO_ChuyenNganh(getActivity());
        rv = view.findViewById(R.id.frag_chung_rv);
        floatAdd = view.findViewById(R.id.frag_chung_floatAdd);
        floatAll = view.findViewById(R.id.frag_chung_float);
        floatSearch = view.findViewById(R.id.frag_chung_floatSearch);
        other();
        init();
    }

    private void init() {
        addAction();
    }

    private void other() {
        list = new ArrayList<>();
        checkFloat = false;
        checkList = false;
    }

    private void addAction() {
        floatAdd.setOnClickListener(this);
        floatAll.setOnClickListener(this);
        floatSearch.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        onLoad();
    }

    private void onLoad() {
        addListView();
    }


    private void actionAdd() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_chuyen_nganh);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        // anhxa
        ma = dialog.findViewById(R.id.dialog_chuyen_nganh_et_maChuyenNganh);
        ten = dialog.findViewById(R.id.dialog_chuyen_nganh_et_tenChuyenNganh);
        huy = dialog.findViewById(R.id.dialog_chuyen_nganh_btnImg_huy);
        them = dialog.findViewById(R.id.dialog_chuyen_nganh_btn_themSua);
        //
        ma.requestFocus();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.showSoftInput(ma, InputMethodManager.SHOW_IMPLICIT);
        //
        them.setOnClickListener(view -> {
            String m = ma.getText().toString(), t = ten.getText().toString();
            if(check(m,t)){
                db.create(m,t);
                list.add(new ChuyenNganh(m,t));
                CustomToast.makeText(getActivity(),"Thêm thành công",0,true).show();
                if(checkList)
                    adapterChuyenNganh.setData(list);
                else
                    adapterChuyenNganh.notifyItemInserted(list.size() - 1);
                dialog.cancel();
            }
        });
        huy.setOnClickListener(view -> dialog.cancel());

        dialog.show();
    }
    private boolean check(String ma, String ten) {
        if(ma.isEmpty() || ten.isEmpty()){
            CustomToast.makeText(getActivity(),"Dữ liệu không được để trống", Toast.LENGTH_SHORT,false).show();
            return false;
        }
        if(!ma.matches("^[a-zA-Z\\d]+$")){
            CustomToast.makeText(getActivity(),"Mã không hợp lệ",0,false).show();
            return false;
        }
        if(db.getIndex(ma) >= 0){
            CustomToast.makeText(getActivity(),"Chuyên ngành đã tồn tại",0,false).show();
            return false;
        }
        return true;
    }

    private void addListView() {
        list = db.read();
        adapterChuyenNganh = new Adapter_ChuyenNganh(this);
        rv.setAdapter(adapterChuyenNganh);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterChuyenNganh.setData(list);
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
                adapterChuyenNganh.setData(list);
                checkList = false;
                dialog.cancel();
            }
            for(ChuyenNganh x : list){
                if(x.getMa_nganh().contains(name_search)){
                    list_tam.add(x);
                }
            }
            adapterChuyenNganh.setData(list_tam);
            checkList = true;
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
        ma_nganh = list.get(index).getMa_nganh();
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_chuyen_nganh);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //ánh xạ
        ma = dialog.findViewById(R.id.dialog_chuyen_nganh_et_maChuyenNganh);
        ten = dialog.findViewById(R.id.dialog_chuyen_nganh_et_tenChuyenNganh);
        huy = dialog.findViewById(R.id.dialog_chuyen_nganh_btnImg_huy);
        sua = dialog.findViewById(R.id.dialog_chuyen_nganh_btn_themSua);
        title = dialog.findViewById(R.id.dialog_chuyen_nganh_tv_title);
        //gán
        title.setText("Sửa Chuyên Ngành");
        sua.setText("Sửa");
        ten.setText(list.get(index).getNganh_hoc());
        ma.setText(ma_nganh);
        //
        ma.requestFocus();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.showSoftInput(ma, InputMethodManager.SHOW_IMPLICIT);
        //
        huy.setOnClickListener(view -> dialog.cancel());
        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String m = ma.getText().toString(), t = ten.getText().toString();
                if(check(m,t)){
                    db.update(ma_nganh,m,t);
                    list.set(index,new ChuyenNganh(m,t));
                    adapterChuyenNganh.notifyItemRangeChanged(0,index+1);
                    CustomToast.makeText(getActivity(),"Cập nhật thành công",0,true);
                    dialog.cancel();
                }
            }
            private boolean check(String ma,String ten){
                if(ma.isEmpty() || ten.isEmpty()){
                    CustomToast.makeText(getActivity(),"Nội dung không được bỏ trống",0,false).show();
                    return false;
                }
                if(!ma.matches("^[a-zA-Z\\d]+$")){
                    CustomToast.makeText(getActivity(),"Mã không hợp lệ",0,false).show();
                    return false;
                }
                if(!ma.equals(ma_nganh) && db.getIndex(ma) >= 0){
                    CustomToast.makeText(getActivity(),"Chuyên ngành này đã tồn tại",0,false).show();
                    return false;
                }
                return true;
            }
        });
        dialog.show();
    }

    @Override
    public void actionXoa(int index) {
        ma_nganh = list.get(index).getMa_nganh();
        list.remove(index);
        db.delete(ma_nganh);
        adapterChuyenNganh.notifyItemRemoved(index);
        adapterChuyenNganh.notifyItemRangeChanged(index,list.size());
        CustomToast.makeText(getActivity(),"Xoá thành công",0,true).show();
    }
}
