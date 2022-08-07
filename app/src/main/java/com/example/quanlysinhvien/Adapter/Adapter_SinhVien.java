package com.example.quanlysinhvien.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlysinhvien.DTO.SinhVien;
import com.example.quanlysinhvien.R;

import java.util.ArrayList;

public class Adapter_SinhVien extends RecyclerView.Adapter<Adapter_SinhVien.MyViewHolder>{
    private ArrayList<SinhVien> list;
    private View view;
    private OnActionSuaXoa action;

    public Adapter_SinhVien(OnActionSuaXoa action){
        this.action = action;
    }

    public void setData(ArrayList<SinhVien> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sinh_vien,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SinhVien sv = list.get(position);
        holder.lop.setText(sv.getMa_lop());
        holder.ten.setText(sv.getTen_SV());
        holder.MSV.setText(sv.getMa_SV());
        if(sv.getGioi_tinh().equalsIgnoreCase("nam")){
            holder.avatar.setImageResource(R.drawable.child_boy_100);
            holder.background.setImageResource(android.R.color.holo_orange_dark);
        }else{
            holder.avatar.setImageResource(R.drawable.child_girl_100);
            holder.background.setImageResource(R.color.violet);
        }
        view.setOnLongClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Xác nhận xoá sinh viên ?");
            builder.setCancelable(false);
            builder.setPositiveButton("Huỷ", (dialogInterface, i) -> {
            });
            builder.setNegativeButton("Xoá", (dialogInterface, i) -> action.acitonXoa(position));
            builder.show();
            return true;
        });
        view.setOnClickListener(view -> action.actionSua(position));
    }

    @Override
    public int getItemCount() {
        if(list == null)
            return 0;
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatar,background;
        private TextView ten,MSV,lop;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.item_sinh_vien_img_avatar);
            ten = itemView.findViewById(R.id.item_sinh_vien_tv_ten);
            MSV = itemView.findViewById(R.id.item_sinh_vien_tv_MSV);
            lop = itemView.findViewById(R.id.item_sinh_vien_tv_maLop);
            background = itemView.findViewById(R.id.item_sinh_vien_img_background);
        }
    }

    public interface OnActionSuaXoa{
        void actionSua(int index);
        void acitonXoa(int index);
    }
}
