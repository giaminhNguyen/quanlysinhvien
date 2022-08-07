package com.example.quanlysinhvien.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlysinhvien.DTO.SinhVien;
import com.example.quanlysinhvien.R;

import java.util.ArrayList;

public class Adapter_DSSinhVienMoiThem extends RecyclerView.Adapter<Adapter_DSSinhVienMoiThem.MyViewHolder>{
    private View view;
    private Context context;
    private ArrayList<SinhVien> list;
    public Adapter_DSSinhVienMoiThem(Context context, ArrayList<SinhVien> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_sinh_vien_moi_them,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SinhVien get = list.get(position);
        holder.ten.setText(get.getTen_SV());
        holder.MSV.setText("MSV: " + get.getMa_SV());
        holder.maLop.setText("Mã Lớp: " + get.getMa_lop());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView ten,MSV,maLop;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ten = itemView.findViewById(R.id.item_sinh_vien_moi_them_ten);
            maLop = itemView.findViewById(R.id.item_sinh_vien_moi_them_maLop);
            MSV = itemView.findViewById(R.id.item_sinh_vien_moi_them_MSV);
        }
    }
}
