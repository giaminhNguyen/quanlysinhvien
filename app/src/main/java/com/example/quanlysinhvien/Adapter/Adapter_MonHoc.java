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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlysinhvien.DAO.DAO_MonHoc;
import com.example.quanlysinhvien.DAO.DAO_QLMH;
import com.example.quanlysinhvien.DTO.MonHoc;
import com.example.quanlysinhvien.Other.CustomToast;
import com.example.quanlysinhvien.R;

import java.util.ArrayList;

public class Adapter_MonHoc extends RecyclerView.Adapter<Adapter_MonHoc.MyViewHolder> {
    private ArrayList<MonHoc> list;
    private OnActionSuaXoa action;

    public Adapter_MonHoc(OnActionSuaXoa action){
        this.action = action;

    }

    public void setData(ArrayList<MonHoc> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chung,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MonHoc get = list.get(position);
        holder.STT.setText(position + ": ");
        holder.maMon.setText(get.getMa_mon());
        holder.tenMon.setText(get.getTen_mon());
        holder.tinChi.setText(String.valueOf(get.getTinChi()));
        holder.xoa.setOnClickListener(view -> action.actionXoa(position));
        holder.sua.setOnClickListener(view -> action.actionSua(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView STT,maMon,tenMon,tinChi;
        ImageButton xoa,sua;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            STT = itemView.findViewById(R.id.item_chung_tv_STT);
            maMon = itemView.findViewById(R.id.item_chung_tv_ma);
            tenMon = itemView.findViewById(R.id.item_chung_tv_ten);
            xoa = itemView.findViewById(R.id.item_chung_btnImg_xoa);
            sua = itemView.findViewById(R.id.item_chung_btnImg_sua);
            tinChi = itemView.findViewById(R.id.item_chung_tv_tinChi);
            linearLayout = itemView.findViewById(R.id.item_chung_linearLayout_tinChi);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }
    public interface OnActionSuaXoa{
        void actionSua(int index);
        void actionXoa(int index);
    }
}
