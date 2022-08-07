package com.example.quanlysinhvien.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlysinhvien.DAO.DAO_ChuyenNganh;
import com.example.quanlysinhvien.DAO.DAO_LopHoc;
import com.example.quanlysinhvien.DAO.DAO_SinhVien;
import com.example.quanlysinhvien.DTO.LopHoc;
import com.example.quanlysinhvien.Other.CustomToast;
import com.example.quanlysinhvien.R;

import java.util.ArrayList;

public class Adapter_LopHoc extends RecyclerView.Adapter<Adapter_LopHoc.MyViewHolder>{
    private ArrayList<LopHoc> list;
    private DAO_ChuyenNganh db_ChuyenNganh;
    private int index;
    private OnActionSuaxoa action;

    public Adapter_LopHoc(OnActionSuaxoa action,Context context){
         this.action = action;
         db_ChuyenNganh = new DAO_ChuyenNganh(context);
    }

    public void setData(ArrayList<LopHoc> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chung,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,int position) {
        LopHoc get = list.get(position);
        holder.STT.setText(position + ": ");
        holder.ma.setText(get.getMa_lop());
        index = holder.getAdapterPosition();
        holder.ten.setText(db_ChuyenNganh.readDongOfMa(get.getMa_nganh()));
        holder.xoa.setOnClickListener(view -> action.actionXoa(position));
        holder.sua.setOnClickListener(view -> action.actionSua(position));
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView ten,ma,STT;
        private ImageButton xoa,sua;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            STT = itemView.findViewById(R.id.item_chung_tv_STT);
            ma = itemView.findViewById(R.id.item_chung_tv_ma);
            ten = itemView.findViewById(R.id.item_chung_tv_ten);
            xoa = itemView.findViewById(R.id.item_chung_btnImg_xoa);
            sua = itemView.findViewById(R.id.item_chung_btnImg_sua);
        }
    }

    public interface OnActionSuaxoa{
        void actionSua(int index);
        void actionXoa(int index);
    }
}
