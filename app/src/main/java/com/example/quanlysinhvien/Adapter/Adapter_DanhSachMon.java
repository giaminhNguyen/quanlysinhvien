package com.example.quanlysinhvien.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlysinhvien.DAO.DAO_Point;
import com.example.quanlysinhvien.DAO.DAO_QLMH;
import com.example.quanlysinhvien.DAO.DAO_SinhVien;
import com.example.quanlysinhvien.DTO.MonHoc;
import com.example.quanlysinhvien.R;

import java.util.ArrayList;

public class Adapter_DanhSachMon extends RecyclerView.Adapter<Adapter_DanhSachMon.MyViewHolder> {
    private Context context;
    private DAO_QLMH db;
    private ArrayList<MonHoc> list;
    private String maNganh;
    private View view;
    public Adapter_DanhSachMon(Context context){
        this.context = context;

    }
    public void setData(ArrayList<MonHoc> list,String maNganh){
        this.maNganh = maNganh;
        db = new DAO_QLMH(context);
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
        MonHoc get = list.get(position);
        holder.STT.setText(position + ": ");
        holder.tenMon.setText(get.getTen_mon());
        holder.maMon.setText(get.getMa_mon());
        holder.xoa.setOnClickListener(view -> actionXoa(holder.getAdapterPosition(), get.getMa_mon()));
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView STT,maMon,tenMon;
        private ImageButton xoa;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            STT = itemView.findViewById(R.id.item_chung_tv_STT);
            maMon = itemView.findViewById(R.id.item_chung_tv_ma);
            tenMon = itemView.findViewById(R.id.item_chung_tv_ten);
            xoa = itemView.findViewById(R.id.item_chung_btnImg_xoa);
            itemView.findViewById(R.id.item_chung_btnImg_sua).setVisibility(View.GONE);
        }
    }
    private void actionXoa(int index, String maMon) {
        list.remove(index);
        db.delete(maNganh,maMon);
        notifyItemRemoved(index);
    }
}
