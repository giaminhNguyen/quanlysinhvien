package com.example.quanlysinhvien.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlysinhvien.DAO.DAO_ChuyenNganh;
import com.example.quanlysinhvien.DAO.DAO_LopHoc;
import com.example.quanlysinhvien.DAO.DAO_SinhVien;
import com.example.quanlysinhvien.DTO.ChuyenNganh;
import com.example.quanlysinhvien.Other.CustomToast;
import com.example.quanlysinhvien.R;

import java.util.ArrayList;

public class Adapter_ChuyenNganh extends RecyclerView.Adapter<Adapter_ChuyenNganh.MyViewHolder> {
    private ArrayList<ChuyenNganh> list;
    private View view;
    private OnActionSuaXoa action;

    public Adapter_ChuyenNganh(OnActionSuaXoa action){
        this.action = action;
    }

    public void setData(ArrayList<ChuyenNganh> list){
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chung,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ChuyenNganh get = list.get(position);
        holder.STT.setText(position + ": ");
        holder.maChuyenNganh.setText(get.getMa_nganh());
        holder.tenChuyenNganh.setText(get.getNganh_hoc());
        holder.xoa.setOnClickListener(view -> action.actionXoa(holder.getAdapterPosition()));
        holder.sua.setOnClickListener(view -> action.actionSua(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView STT,maChuyenNganh,tenChuyenNganh;
        private ImageButton xoa,sua;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            STT = itemView.findViewById(R.id.item_chung_tv_STT);
            maChuyenNganh = itemView.findViewById(R.id.item_chung_tv_ma);
            tenChuyenNganh = itemView.findViewById(R.id.item_chung_tv_ten);
            xoa = itemView.findViewById(R.id.item_chung_btnImg_xoa);
            sua = itemView.findViewById(R.id.item_chung_btnImg_sua);
        }
    }

    public interface OnActionSuaXoa{
        void actionSua(int index);
        void actionXoa(int index);
    }
}
